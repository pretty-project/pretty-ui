
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
              [x.app-utils.http]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
;  Mennyi ideig várjon a szerver válaszára
(def DEFAULT-REQUEST-TIMEOUT 15000)

; @constant (ms)
(def DEFAULT-IDLE-TIMEOUT 500)

; @constant (metamorphic-content)
(def DEFAULT-FAILURE-MESSAGE :synchronization-error)

; @constant (map)
(def REQUEST-HANDLERS
     {:error-handler-event    :sync/->request-failure
      :handler-event          :sync/->request-success
      :progress-handler-event :sync/->request-progressed})



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-requests-history
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (r db/get-partition-history db ::requests))

(defn get-request-history
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (map)
  [db [_ request-id]]
  (r db/get-data-history db ::requests request-id))

(defn get-requests
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (get-in db (db/path ::requests)))

(defn get-request-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (map)
  [db [_ request-id]]
  (get-in db (db/path ::requests request-id)))

(defn get-request-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (keyword) prop-id
  ;
  ; @return (*)
  [db [_ request-id prop-id]]
  (get-in db (db/path ::requests request-id prop-id)))

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

(defn request-sent?
  ; @param (keyword) request-id
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [sent-time (r get-request-prop db request-id :sent-time)]
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
  (r get-request-prop db request-id :silent-mode?))

(defn- get-request-on-failure-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) server-response
  ;
  ; @return (metamorphic-event)
  [db [_ request-id server-response]]
  (if-let [on-failure-event (r get-request-prop db request-id :on-failure)]
          (a/metamorphic-event<-params on-failure-event server-response)))

(defn- get-request-on-success-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (*) server-response
  ;
  ; @return (metamorphic-event)
  [db [_ request-id server-response]]
  (if-let [on-success-event (r get-request-prop db request-id :on-success)]
          (a/metamorphic-event<-params on-success-event server-response)))

(defn- get-request-on-sent-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (metamorphic-event)
  [db [_ request-id]]
  (r get-request-prop db request-id :on-sent))

(defn- get-request-on-responsed-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (*) server-response
  ;
  ; @return (metamorphic-event)
  [db [_ request-id server-response]]
  (if-let [on-responsed-event (r get-request-prop db request-id :on-responsed)]
          (a/metamorphic-event<-params on-responsed-event server-response)))

(defn- get-request-on-stalled-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (*) server-response
  ;
  ; @return (metamorphic-event)
  [db [_ request-id server-response]]
  (if-let [on-stalled-event (r get-request-prop db request-id :on-stalled)]
          (a/metamorphic-event<-params on-stalled-event server-response)))

(defn- get-request-idle-timeout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (integer)
  [db [_ request-id]]
  (or (r get-request-prop db request-id :idle-timeout)
      (param DEFAULT-IDLE-TIMEOUT)))

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
  (cond-> request-props
          (vector? source-path)
          (assoc-in [:params :source] (get-in db source-path))))

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
         ; 1.
  (merge {:response-action :store
          :silent-mode?    true
          :timeout DEFAULT-REQUEST-TIMEOUT}
         ; 2.
         (r request-props<-source-data db request-props)
         ; 3.
         {:sent-time (time/timestamp-string)}))



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
  (as-> db % (r db/set-item! % (db/path ::requests request-id) request-props)
             ; DEBUG
             (r db/set-item! % (db/path ::requests request-id :debug)
                               [:sync/get-request-state request-id])
             ; DEBUG
             (r db/update-data-history! % ::requests request-id)))

(defn clear-request!
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/clear-request! db :my-request)
  ;
  ; @return (map)
  [db [_ request-id]]
  (as-> db % (r db/remove-item!  % (db/path ::requests request-id))
             (r a/clear-process! % request-id)))

; @usage
;  [:sync/clear-request! :my-request]
(a/reg-event-db :sync/clear-request! clear-request!)



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
  ;   :modifier (database function)(opt)
  ;    A szerver-válasz értéket eltárolása előtt módosító függvény.
  ;    Only w/ {:target-path ...} or {:target-paths ...}
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
  ;
  ; @usage
  ;  (defn my-modifier [db [_ request-id response] (do-something-with! response))
  ;  [:sync/send-request! {:method      :get
  ;                        :modifier    my-modifier
  ;                        :target-path [:my :response :path]
  ;                        :uri         "/get-my-data"}]
  (fn [{:keys [db]} event-vector]
      (let [request-id    (a/event-vector->second-id   event-vector)
            request-props (a/event-vector->first-props event-vector)
            request-props (r request-props-prototype db request-props)]
           (if (r a/start-process? db request-id)
               {:db         (r store-request-props! db request-id request-props)
                :dispatch-n [[:sync/->request-sent request-id]
                             (let [request-props (merge request-props REQUEST-HANDLERS)]
                                  [:http/send-request! request-id request-props])]}))))



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
           {:dispatch-n
            [[:sync/handle-request-response!      request-id server-response]
             [:core/set-process-status!           request-id :success]
             [:core/set-process-activity!         request-id :idle]
             (r get-request-on-success-event   db request-id server-response)
             (r get-request-on-responsed-event db request-id server-response)]
            :dispatch-later
            [{:ms       (r get-request-idle-timeout     db request-id)
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
      {:dispatch-n
       [[:core/set-process-status!           request-id :failure]
        [:core/set-process-activity!         request-id :idle]
        (r get-request-on-failure-event   db request-id server-response)
        (r get-request-on-responsed-event db request-id server-response)
        (if-not (r silent-mode? db request-id)
                [:sync/show-request-failure-message! request-id status-text])]
       :dispatch-later
       [{:ms       (r get-request-idle-timeout     db request-id)
         :dispatch [:core/set-process-activity!       request-id :stalled]}
        {:ms       (r get-request-idle-timeout     db request-id)
         :dispatch (r get-request-on-stalled-event db request-id server-response)}]}))

(a/reg-event-fx
  :sync/->request-progressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (integer) request-progress
  (fn [_ [_ request-id request-progress]]
      [:core/set-process-progress! request-id request-progress]))
