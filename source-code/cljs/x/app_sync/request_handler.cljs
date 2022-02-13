
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v2.9.4
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.request-handler
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.reader :as reader]
              [mid-fruits.time   :as time]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [x.app-sync.engine :as engine]
              [x.app-ui.api      :as ui]
              [x.app-sync.response-handler :as response-handler]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
;  Mennyi ideig várjon a szerver válaszára
(def DEFAULT-REQUEST-TIMEOUT 15000)

; @constant (ms)
(def DEFAULT-IDLE-TIMEOUT 250)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-status
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/get-request-status db :my-request)
  ;
  ; @return (keyword)
  [db [_ request-id]]
  (r a/get-process-status db request-id))

(defn get-request-activity
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/get-request-activity db :my-request)
  ;
  ; @return (keyword)
  [db [_ request-id]]
  (r a/get-process-activity db request-id))

(defn get-request-progress
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/get-request-progress db :my-request)
  ;
  ; @return (keyword)
  [db [_ request-id]]
  (r a/get-process-progress db request-id))

(defn request-active?
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/request-active? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (r a/process-active? db request-id))

(defn request-sent?
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/request-sent? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [sent-time (get-in db (db/path :sync/requests request-id :sent-time))]
       (some? sent-time)))

(defn request-successed?
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/request-successed? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [request-status (r a/get-process-status db request-id)]
       (= request-status :success)))

(defn request-failured?
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/request-failured? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [request-status (r a/get-process-status db request-id)]
       (= request-status :failure)))

(defn request-aborted?
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/request-aborted? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (get-in db (db/path :sync/requests request-id :aborted?)))

(defn- request-resent?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:sent-time (string)}
  ;
  ; @return (boolean)
  [db [_ request-id {:keys [sent-time]}]]
  (not= sent-time (get-in db (db/path :sync/requests request-id :sent-time))))

(defn listening-to-request?
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/listening-to-request? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [request-activity (r a/get-process-activity db request-id)]
       (or (= request-activity :active)
           (= request-activity :idle))))

(defn- get-request-on-failure-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) server-response
  ;
  ; @return (metamorphic-event)
  [db [_ request-id server-response]]
  (if-let [on-failure-event(get-in db (db/path :sync/requests request-id :on-failure))]
          (a/metamorphic-event<-params on-failure-event server-response)))

(defn- get-request-on-success-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (*) server-response
  ;
  ; @return (metamorphic-event)
  [db [_ request-id server-response]]
  (if-let [on-success-event (get-in db (db/path :sync/requests request-id :on-success))]
          (a/metamorphic-event<-params on-success-event server-response)))

(defn- get-request-on-sent-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (metamorphic-event)
  [db [_ request-id]]
  (get-in db (db/path :sync/requests request-id :on-sent)))

(defn- get-request-on-responsed-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (*) server-response
  ;
  ; @return (metamorphic-event)
  [db [_ request-id server-response]]
  (if-let [on-responsed-event (get-in db (db/path :sync/requests request-id :on-responsed))]
          (a/metamorphic-event<-params on-responsed-event server-response)))

(defn- get-request-on-stalled-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:on-stalled (metamorphic-event)(opt)
  ;   :request-successed? (boolean)(opt)}
  ; @param (*) server-response
  ;
  ; @return (metamorphic-event)
  [db [_ request-id {:keys [on-stalled request-successed?]} server-response]]
  ; Az {:on-stalled ...} esemény használható az {:on-success ...} esemény alternatívájaként,
  ; mert hibás teljesítés esetén nem történik meg.
  (if (and on-stalled request-successed?)
      (a/metamorphic-event<-params on-stalled server-response)))

(defn- get-request-idle-timeout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (integer)
  [db [_ request-id]]
  (get-in db (db/path :sync/requests request-id :idle-timeout) DEFAULT-IDLE-TIMEOUT))

(defn get-request-state
  ; @param (keyword) request-id
  ;
  ; @return (map)
  ;  {:request-status (keyword)
  ;   :request-activity (keyword)
  ;   :request-progress (percent)
  ;   :request-sent? (boolean)
  ;   :request-successed? (boolean)
  ;   :request-failured? (boolean)
  ;   :listening-to-request? (boolean)}
  [db [_ request-id]]
  {:request-status        (r get-request-status    db request-id)
   :request-activity      (r get-request-activity  db request-id)
   :request-progress      (r get-request-progress  db request-id)
   :request-sent?         (r request-sent?         db request-id)
   :request-successed?    (r request-successed?    db request-id)
   :request-failured?     (r request-failured?     db request-id)
   :listening-to-request? (r listening-to-request? db request-id)})

(a/reg-sub :sync/get-request-state get-request-state)



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- request-props<-source-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Ha a (POST) request tartalmazza a :source-path tulajdonságot, akkor
  ; hozzáfűzi a request-hez paraméterként a :source-path Re-Frame adatbázis
  ; útvonalon található adatot.
  ;
  ; @param (map) request-props
  ;  {:source-path (item-path vector)(opt)}
  ;
  ; @return (map)
  ;  {:params (map)
  ;   {:source (*)}}
  [db [_ {:keys [source-path] :as request-props}]]
  (if source-path (assoc-in request-props [:params :source] (get-in db source-path))
                  (return   request-props)))

(defn- request-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request-props
  ;
  ; @return (map)
  ;  {:response-action (keyword)
  ;   :sent-time (object)
  ;   :timeout (integer)}
  [db [_ {:keys [response-action] :as request-props}]]
  (merge {:error-handler-event    :sync/request-failured
          :handler-event          :sync/request-successed
          :progress-handler-event :core/set-process-progress!
          :response-action        :store
          :timeout DEFAULT-REQUEST-TIMEOUT
          :sent-time (time/timestamp-string)}
         (r request-props<-source-data db request-props)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-request-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;
  ; @return (map)
  [db [_ request-id request-props]]
  (as-> db % (r db/set-item! % (db/path :sync/requests request-id) request-props)
             (r db/set-item! % (db/path :sync/requests request-id :debug) ; DEBUG
                               [:sync/get-request-state request-id])
             (r db/update-data-history! % :sync/requests request-id)))    ; DEBUG

(defn request-aborted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (map)
  [db [_ request-id]]
  (assoc-in db (db/path :sync/requests request-id :aborted?) true))

(defn- request-successed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (*) server-response
  ;
  ; @return (map)
  [db [_ request-id server-response]]
  (as-> db % (r a/set-process-status!   % request-id :success)
             (r a/set-process-activity! % request-id :idle)
             (if (r response-handler/store-request-response? % request-id)
                 (r response-handler/store-request-response! % request-id server-response)
                 (return %))))

(defn- request-failured
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ request-id server-response]]
  (as-> db % (r a/set-process-status!   % request-id :failure)
             (r a/set-process-activity! % request-id :idle)))

(defn- request-stalled
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:display-progress? (boolean)(opt)}
  ;
  ; @return (map)
  [db [_ request-id {:keys [display-progress?] :as request-props}]]
  (if (r request-resent? db request-id request-props)
      ; Ha az {:on-stalled [...]} esemény megtörténése előtt a request újra el lett küldve ...
      (if-let [display-progress? (get-in db (db/path :sync/requests request-id :display-progress?))]
              (return                          db)
              (r ui/stop-listening-to-process! db request-id))
      ; Ha az {:on-stalled [...]} esemény megtörténése előtt a request NEM lett újra elküldve ...
      (if display-progress? (as-> db % (r a/set-process-activity!       % request-id :stalled)
                                       (r ui/stop-listening-to-process! % request-id))
                            (r a/set-process-activity! db request-id :stalled))))

(defn- reset-request-process!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (map)
  [db [_ request-id]]
  (as-> db % ; Set request status
             (r a/set-process-status!   % request-id :progress)
             ; Set request activity
             (r a/set-process-activity! % request-id :active)
             ; Set request progress
             ; Szükséges a process-progress értékét nullázni!
             ; A szerver-válasz megérkezése után a process-progress értéke 100%-on marad.
             (r a/set-process-progress! % request-id 0)))

(defn- send-request!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:display-progress? (boolean)}
  ;
  ; @return (map)
  [db [_ request-id {:keys [display-progress?] :as request-props}]]
  (if display-progress? (as-> db % (r reset-request-process! % request-id)
                                   (r store-request-props!   % request-id request-props)
                                   (r ui/listen-to-process!  % request-id))
                        (as-> db % (r reset-request-process! % request-id)
                                   (r store-request-props!   % request-id request-props))))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :sync/abort-request!
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  [:sync/abort-request! :my-request]
  (fn [{:keys [db]} [_ request-id]]
      {:db (r request-aborted db request-id)
       :sync/abort-request! [request-id]}))

(a/reg-event-fx
  :sync/send-request!
  ; @param (keyword)(opt) request-id
  ;  A request a saját request-id azonosítójával párhuzamosan elindít egy process
  ;  folyamatot, ennek köszönhetően a request státuszát, aktivitását
  ;  és folyamatállapotát lehetséges ellenőrizni a request azonosítójára
  ;  {:process-id ...} tulajdonságként hivatkozva.
  ; @param (map) request-props
  ;  {:body (*)(opt)
  ;   :display-process? (boolean)(opt)
  ;    Default: false
  ;   :filename (string)(opt)
  ;    Only w/ {:response-action :save}
  ;   :idle-timeout (ms)(opt)
  ;    Default: DEFAULT-IDLE-TIMEOUT
  ;    A szerver-válasz megérkezése után mennyi ideig maradjon a request-et kezelő process
  ;    :idle állapotban. Idle állapotban a request már újraindítható de még UI-on megjelenített
  ;    process állapotát visszajelző elemek aktívak.
  ;   :method (keyword)
  ;    :post, :get
  ;   :on-failure (metamorphic-event)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a szerver-válasz értékét.
  ;   :on-responsed (metamorphic-event)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a szerver-válasz értékét.
  ;   :on-sent (metamorphic-event)(opt)
  ;   :on-stalled (metamorphic-event)(opt)
  ;    A szerver-válasz visszaérkezése utáni idle-timeout időtartam lejárta után megtörténő esemény.
  ;    Az esemény-vektor utolsó paraméterként megkapja a szerver-válasz értékét.
  ;   :on-success (metamorphic-event)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a szerver-válasz értékét.
  ;   :params (map)(opt)
  ;    Only w/ {:method :post}
  ;   :response-action (keyword)(opt)
  ;    :save (save to file), :store (store to db)
  ;    Default: :store
  ;   :source-path (item-path vector)(opt)
  ;    Milyen Re-Frame adatbázis útvonalon tárolt adatot küldjön el paraméterként
  ;    Only w/ {:method :post}
  ;   :target-path (item-path vector)(opt)
  ;    Milyen Re-Frame adatbázis útvonalra mentse el a szerver válaszát
  ;    Only w/ {:response-action :store}
  ;   :uri (string)
  ;    "/sample-uri"}
  ;
  ; @usage
  ;  [:sync/send-request! {...}]
  ;
  ; @usage
  ;  [:sync/send-request! :my-request {...}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ request-id request-props]]
      (let [request-props (r request-props-prototype db request-props)]
           (if (r a/start-process? db request-id)
               {:db                 (r send-request! db request-id request-props)
                :sync/send-request! [request-id request-props]
                :dispatch           [:sync/request-sent request-id]}))))

(a/reg-event-fx
  :sync/request-sent
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  (fn [{:keys [db]} [_ request-id]]
      ; Dispatch request on-sent event
      (r get-request-on-sent-event db request-id)))

(a/reg-event-fx
  :sync/request-successed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (string) server-response-body
  ;  "{...}"
  (fn [{:keys [db]} [_ request-id server-response-body]]
      (let [server-response (reader/string->mixed server-response-body)
            request-props (assoc (get-in db (db/path :sync/requests request-id)) :request-successed? true)]
           {:db (r request-successed db request-id server-response)
            :sync/remove-reference! [request-id]
            :dispatch-n     [(r get-request-on-success-event   db request-id server-response)
                             (r get-request-on-responsed-event db request-id server-response)]
            :dispatch-if    [(r response-handler/save-request-response? db request-id)
                             [:sync/save-request-response! request-id server-response-body]]
            :dispatch-later [{:ms (r get-request-idle-timeout db request-id)
                              :dispatch [:sync/request-stalled request-id request-props server-response]}]})))

(a/reg-event-fx
  :sync/request-failured
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) server-response
  ;  {:status (integer)
  ;   :status-text (string)
  ;   :failure (keyword)
  ;    :error, :parse, :aborted, :timeout}
  ;   :response (string)
  ;    server-response-body}
  (fn [{:keys [db]} [_ request-id {:keys [status-text] :as server-response}]]
      (let [request-props (assoc (get-in db (db/path :sync/requests request-id)) :request-failured? true)]
           {:db (r request-failured db request-id server-response)
            :sync/remove-reference! [request-id]
            :dispatch-n     [(r get-request-on-failure-event   db request-id server-response)
                             (r get-request-on-responsed-event db request-id server-response)]
            :dispatch-later [{:ms (r get-request-idle-timeout db request-id)
                              :dispatch [:sync/request-stalled request-id request-props server-response]}]})))

(a/reg-event-fx
  :sync/request-stalled
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ; @param (map) server-response
  (fn [{:keys [db]} [_ request-id request-props server-response]]
      ; - A [:sync/request-stalled ...] esemény paraméterként kapja meg a request-props térképet
      ;   mert előfordulhat, hogy az {:on-stalled [...]} esemény megtörténése előtt a request újra
      ;   el lett küldve eltérő tulajdonságokkal!
      ; - A request-props térkép esetlegesen tartalmazza a {:request-successed? ...} tulajdonságot,
      ;   ami alapján megállapítható, hogy szükséges-e az {:on-stalled [...]} esemény meghívása.
      ; - A request-props térkép tartalmazza az {:sent-time "..."} tulajdonságot, ami alapján
      ;   megállapítható, hogy a request újra el lett-e küldve.
      {:db       (r request-stalled              db request-id request-props)
       :dispatch (r get-request-on-stalled-event db request-id request-props server-response)}))
