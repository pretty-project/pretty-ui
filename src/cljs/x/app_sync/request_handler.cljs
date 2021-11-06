
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v2.3.2
; Compatibility: x3.9.9



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
;  Mennyi idő után probálkozzon a hibásan teljesített request-et újraküldeni
(def DEFAULT-RETRY-TIMEOUT 5000)

; @constant (ms)
;  Mennyi ideig várjon a szerver válaszára
(def DEFAULT-REQUEST-TIMEOUT 15000)

; @constant (ms)
(def DEFAULT-IDLE-TIMEOUT 500)

; @constant (integer)
(def MAX-TRY-COUNT 3)

; @constant (metamorphic-content)
(def DEFAULT-FAILURE-MESSAGE :synchronization-error)

; @constant (map)
(def REQUEST-HANDLERS
     {:error-handler-event    ::->request-failure
      :handler-event          ::->request-success
      :progress-handler-event ::->request-progressed})



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  (let [try-count (r get-request-prop db request-id :try-count)]
       (> try-count 0)))

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

(defn- max-try-count-reached?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [try-count (r get-request-prop db request-id :try-count)]
       (= MAX-TRY-COUNT try-count)))

(defn- retry-request?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (and (not (r request-successed?     db request-id))
       (not (r max-try-count-reached? db request-id))))

(defn- auto-retry-request?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [auto-retry? (r get-request-prop db request-id :auto-retry?)]
       (and (boolean auto-retry?)
            (not (r max-try-count-reached? db request-id)))))

(defn- get-request-retry-timeout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (integer)
  [db [_ request-id]]
  (r get-request-prop db request-id :retry-timeout))

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

(a/reg-sub :x.app-sync/get-request-state get-request-state)



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
  ;  {:auto-retry? (boolean)
  ;   :response-action (keyword)
  ;   :retry-timeout (integer)
  ;   :sent-time (object)
  ;   :silent-mode? (boolean)
  ;   :timeout (integer)}
  [db [_ {:keys [auto-retry? response-action] :as request-props}]]
         ; 1.
  (merge {:auto-retry?     false
          :response-action :store
          :silent-mode?    true
          :timeout DEFAULT-REQUEST-TIMEOUT}
         ; 2.
         (if (= auto-retry? true)
             {:retry-timeout DEFAULT-RETRY-TIMEOUT})
         ; 3.
         (r request-props<-source-data db request-props)
         ; 4.
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
  (-> db (assoc-in (db/path ::requests request-id) request-props)
         ; DEBUG
         (assoc-in (db/path ::requests request-id :debug)
                   [:x.app-sync/get-request-state request-id])))

(defn clear-request!
  ; @param (keyword) request-id
  ;
  ; @return (map)
  [db [event-id request-id]]
  (-> db (db/remove-item!  [event-id (db/path ::requests request-id)])
         (a/clear-process! [event-id request-id])))

(a/reg-event-db :x.app-sync/clear-request! clear-request!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-sync/show-request-failure-message!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (keyword or string) status-text
  (fn [{:keys [db]} [_ request-id status-text]]
      [:x.app-ui/blow-bubble! request-id
       {:content (or status-text DEFAULT-FAILURE-MESSAGE)
        :color   :warning}]))

(a/reg-event-fx
  :x.app-sync/send-request!
  ; @param (keyword)(opt) request-id
  ;  A request a saját request-id azonosítójával párhuzamosan elindít egy process
  ;  folyamatot, ennek köszönhetően a request státuszát, aktivitását
  ;  és folyamatállapotát lehetséges ellenőrizni a request azonosítójára
  ;  {:process-id ...} tulajdonságként hivatkozva.
  ; @param (map) request-props
  ;  {:auto-retry? (boolean)(opt)
  ;    Default: false
  ;   :body (*)(opt)
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
  ;   :retry-timeout (integer)(opt)
  ;    Milyen időközönként próbálja meg újra elküldeni a hibásan teljesített
  ;    request-et.
  ;    Only w/ {:auto-retry? true}
  ;    Default: DEFAULT-RETRY-TIMEOUT
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
  ;  [:x.app-sync/send-request! {...}]
  ;
  ; @usage
  ;  [:x.app-sync/send-request! :my-request {...}]
  ;
  ; @usage
  ;  (defn download-our-data [request] (http/map-wrap {:my-data-item "..." :your-data-item "..."}))
  ;  [:x.app-sync/send-request! {:method :get
  ;                              :target-paths {:my-data-item   [:db :my   :data :item :path]
  ;                                             :your-data-item [:db :your :data :item :path]
  ;                                             :my-item {:my-nested-item [:db :my :nested :item]}}
  ;                              :uri "/get-our-data"}]
  ;
  ; @usage
  ;  (defn my-modifier [db [_ request-id response] (do-something-with! response))
  ;  [:x.app-sync/send-request! {:method      :get
  ;                              :modifier    my-modifier
  ;                              :target-path [:my :response :path]
  ;                              :uri         "/get-my-data"}]
  (fn [{:keys [db]} event-vector]
      (let [request-id    (a/event-vector->second-id   event-vector)
            request-props (a/event-vector->first-props event-vector)
            request-props (r request-props-prototype db request-props)]
           (if (r a/start-process? db request-id)
               {:db         (r store-request-props!   db request-id request-props)
                :dispatch-n [[:x.app-sync/->request-sent request-id]
                             (let [request-props (merge request-props REQUEST-HANDLERS)]
                                  [:x.app-utils.http/send-request! request-id request-props])]}))))

(a/reg-event-fx
  :x.app-sync/retry-request!
  ; @param (keyword) request-id
  (fn [{:keys [db]} [_ request-id]]
      {:dispatch-if
       [(r retry-request? db request-id)
        [:x.app-sync/send-request! request-id (r get-request-props db request-id)]]}))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-sync/->request-sent
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  (fn [{:keys [db]} [event-id request-id]]
                  ; Increase try-count
      {:db (-> db (db/apply! [event-id (db/path ::requests request-id :try-count) inc])
                  ; Set request status
                  (a/set-process-status!   [event-id request-id :progress])
                  ; Set request activity
                  (a/set-process-activity! [event-id request-id :active])
                  ; Set request progress
                  ; Szükséges a process-progress értékét nullázni!
                  ; A szerver-válasz megérkezése után a process-progress értéke 100%-on marad.
                  (a/set-process-progress! [event-id request-id 0]))
      ; Dispatch request on-sent event
       :dispatch (r get-request-on-sent-event db request-id)}))

(a/reg-event-fx
  ::->request-success
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (string) server-response-body
  ;  "{...}"
  (fn [{:keys [db]} [_ request-id server-response-body]]
      (let [server-response (reader/string->mixed server-response-body)]
           {:dispatch-n
            [[:x.app-sync/handle-request-response! request-id server-response]
             [:x.app-core/set-process-status!      request-id :success]
             [:x.app-core/set-process-activity!    request-id :idle]
             (r get-request-on-success-event   db  request-id server-response)
             (r get-request-on-responsed-event db  request-id server-response)]
            :dispatch-later
            [{:ms       (r get-request-idle-timeout     db request-id)
              :dispatch [:x.app-core/set-process-activity! request-id :stalled]}
             {:ms       (r get-request-idle-timeout     db request-id)
              :dispatch (r get-request-on-stalled-event db request-id server-response)}]})))

(a/reg-event-fx
  ::->request-failure
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
       [[:x.app-core/set-process-status!     request-id :failure]
        [:x.app-core/set-process-activity!   request-id :idle]
        (r get-request-on-failure-event   db request-id server-response)
        (r get-request-on-responsed-event db request-id server-response)
        (if-not (r silent-mode? db request-id)
                [:x.app-sync/show-request-failure-message! request-id status-text])]
       :dispatch-later
       (if (r auto-retry-request? db request-id)
           ; Auto retry
           [{:ms       (r get-request-retry-timeout    db request-id)
             :dispatch [:x.app-core/set-process-activity! request-id :stalled]}
            {:ms       (r get-request-retry-timeout    db request-id)
             :dispatch [:x.app-sync/retry-request!        request-id]}
            {:ms       (r get-request-idle-timeout     db request-id)
             :dispatch (r get-request-on-stalled-event db request-id server-response)}]
           ; No auto retry
           [{:ms       (r get-request-idle-timeout     db request-id)
             :dispatch [:x.app-core/set-process-activity! request-id :stalled]}
            {:ms       (r get-request-idle-timeout     db request-id)
             :dispatch (r get-request-on-stalled-event db request-id server-response)}])}))

(a/reg-event-fx
  ::->request-progressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (integer) request-progress
  (fn [_ [_ request-id request-progress]]
      [:x.app-core/set-process-progress! request-id request-progress]))
