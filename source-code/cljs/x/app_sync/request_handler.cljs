
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v2.4.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.request-handler
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.reader :as reader]
              [mid-fruits.time   :as time]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [x.app-sync.engine :as engine]
              [x.app-sync.response-handler :as response-handler]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
;  Mennyi ideig várjon a szerver válaszára
(def DEFAULT-REQUEST-TIMEOUT 15000)

; @constant (ms)
(def DEFAULT-IDLE-TIMEOUT 500)

; @constant (metamorphic-content)
(def DEFAULT-FAILURE-MESSAGE :synchronization-error)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-status
  ; @param (keyword) request-id
  ;
  ; @return (keyword)
  [db [_ request-id]]
  (r a/get-process-status db request-id))

(defn get-request-activity
  ; @param (keyword) request-id
  ;
  ; @return (keyword)
  [db [_ request-id]]
  (r a/get-process-activity db request-id))

(defn get-request-progress
  ; @param (keyword) request-id
  ;
  ; @return (keyword)
  [db [_ request-id]]
  (r a/get-process-progress db request-id))

(defn request-active?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (r a/process-active? db request-id))

(defn request-sent?
  ; @param (keyword) request-id
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [sent-time (get-in db (db/path :sync/requests request-id :sent-time))]
       (some? sent-time)))

(defn request-successed?
  ; @param (keyword) request-id
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [request-status (r a/get-process-status db request-id)]
       (= request-status :success)))

(defn request-failured?
  ; @param (keyword) request-id
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [request-status (r a/get-process-status db request-id)]
       (= request-status :failure)))

(defn request-aborted?
  ; @param (keyword) request-id
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (get-in db (db/path :sync/requests request-id :aborted?)))

(defn listening-to-request?
  ; @param (keyword) request-id
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [request-activity (r a/get-process-activity db request-id)]
       (or (= request-activity :active)
           (= request-activity :idle))))

(defn- silent-mode?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (get-in db (db/path :sync/requests request-id :silent-mode?)))

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
  ; @param (*) server-response
  ;
  ; @return (metamorphic-event)
  [db [_ request-id server-response]]
  (if-let [on-stalled-event (get-in db (db/path :sync/requests request-id :on-stalled))]
          (a/metamorphic-event<-params on-stalled-event server-response)))

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
  ;   :silent-mode? (boolean)
  ;   :timeout (integer)}
  [db [_ {:keys [response-action] :as request-props}]]
  (merge {:error-handler-event    :sync/->request-failure
          :handler-event          :sync/->request-success
          :progress-handler-event :core/set-process-progress!
          :response-action        :store
          :silent-mode?           true
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
             ; DEBUG
             (r db/set-item! % (db/path :sync/requests request-id :debug)
                               [:sync/get-request-state request-id])
             ; DEBUG
             (r db/update-data-history! % :sync/requests request-id)))

(defn ->request-aborted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (map)
  [db [_ request-id]]
  (assoc-in db (db/path :sync/requests request-id :aborted?) true))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :sync/show-request-failure-message!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (keyword or string) status-text
  (fn [{:keys [db]} [_ request-id status-text]]
      [:ui/blow-bubble! request-id
                        {:content (or status-text DEFAULT-FAILURE-MESSAGE)
                         :color   :warning}]))

(a/reg-event-fx
  :sync/abort-request!
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  [:sync/abort-request! :my-request]
  (fn [{:keys [db]} [_ request-id]]
      {:db (r ->request-aborted db request-id)
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
  ;   :silent-mode? (boolean)(opt)
  ;    A csendes mód jelentése, hogy hibás teljesítés esetén nem jelenik meg
  ;    hibaüzenet.
  ;    Default: true
  ;   :source-path (item-path vector)(opt)
  ;    Milyen Re-Frame adatbázis útvonalon tárolt adatot küldjön el paraméterként
  ;    Only w/ {:method :post}
  ;   :target-path (item-path vector)(opt)
  ;    Milyen Re-Frame adatbázis útvonalra mentse el a szerver válaszát
  ;    Only w/ {:response-action :store :target-paths nil}
  ;   :target-paths (map)(opt)
  ;    Milyen Re-Frame adatbázis útvonalakra mentse el a szerver válaszának elemeit
  ;    Only w/ {:response-action :store :target-path nil}
  ;   :uri (string)
  ;    "/sample-uri"}
  ;
  ; @usage
  ;  [:sync/send-request! {...}]
  ;
  ; @usage
  ;  [:sync/send-request! :my-request {...}]
  ;
  ; @usage
  ;  (defn download-our-data [request] (http/map-wrap {:my-data-item "..." :your-data-item "..."}))
  ;  [:sync/send-request! {:method :get
  ;                        :target-paths {:my-data-item   [:db :my   :data :item :path]
  ;                                       :your-data-item [:db :your :data :item :path]
  ;                                       :my-item {:my-nested-item [:db :my :nested :item]}}
  ;                        :uri "/get-our-data"}]
  (fn [{:keys [db]} event-vector]
      (let [request-id    (a/event-vector->second-id   event-vector)
            request-props (a/event-vector->first-props event-vector)
            request-props (r request-props-prototype db request-props)]
           (if (r a/start-process? db request-id)
               {:db                 (r store-request-props! db request-id request-props)
                :sync/send-request! [request-id request-props]
                :dispatch           [:sync/->request-sent request-id]}))))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :sync/->request-sent
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  (fn [{:keys [db]} [_ request-id]]
                      ; Set request status
      {:db (as-> db % (r a/set-process-status!   % request-id :progress)
                      ; Set request activity
                      (r a/set-process-activity! % request-id :active)
                      ; Set request progress
                      ; Szükséges a process-progress értékét nullázni!
                      ; A szerver-válasz megérkezése után a process-progress értéke 100%-on marad.
                      (r a/set-process-progress! % request-id 0))
       ; Dispatch request on-sent event
       :dispatch (r get-request-on-sent-event db request-id)}))

(a/reg-event-fx
  :sync/->request-success
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (string) server-response-body
  ;  "{...}"
  (fn [{:keys [db]} [_ request-id server-response-body]]
      (let [server-response (reader/string->mixed server-response-body)]
            ; TEMP
            ; Fontos, hogy a szerver-válasz minél hamarabb a Re-Frame adatbázisba íródjon!
           {:db (r response-handler/store-request-response! db request-id server-response)
            :sync/remove-reference! [request-id]
            :dispatch-n [;[:sync/handle-request-response!      request-id server-response]
                         [:core/set-process-status!           request-id :success]
                         [:core/set-process-activity!         request-id :idle]
                         (r get-request-on-success-event   db request-id server-response)
                         (r get-request-on-responsed-event db request-id server-response)]
            :dispatch-later [{:ms       (r get-request-idle-timeout     db request-id)
                              :dispatch [:core/set-process-activity!       request-id :stalled]}
                             {:ms       (r get-request-idle-timeout     db request-id)
                              :dispatch (r get-request-on-stalled-event db request-id server-response)}]})))

(a/reg-event-fx
  :sync/->request-failure
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
      {:sync/remove-reference! [request-id]
       :dispatch-n [[:core/set-process-status!           request-id :failure]
                    [:core/set-process-activity!         request-id :idle]
                    (r get-request-on-failure-event   db request-id server-response)
                    (r get-request-on-responsed-event db request-id server-response)
                    (if-not (r silent-mode? db request-id)
                            [:sync/show-request-failure-message! request-id status-text])]
       :dispatch-later [{:ms       (r get-request-idle-timeout db request-id)
                         :dispatch [:core/set-process-activity!   request-id :stalled]}]}))
                         ; A [:sync/->request-failure ...] esemény nem hívja meg az {:on-stalled ...}
                         ; tulajdonságként átadott eseményt, így az nem történik meg hibás teljesítés
                         ; esetén, ezért az {:on-stalled ...] esemény használható az {:on-success ...}
                         ; esemény alternatívájaként!
