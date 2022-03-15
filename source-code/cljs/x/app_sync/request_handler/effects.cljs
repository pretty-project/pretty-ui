
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.request-handler.effects
    (:require [ajax.api]
              [mid-fruits.reader                     :as reader]
              [x.app-core.api                        :as a :refer [r]]
              [x.app-sync.request-handler.config     :as request-handler.config]
              [x.app-sync.request-handler.events     :as request-handler.events]
              [x.app-sync.request-handler.prototypes :as request-handler.prototypes]
              [x.app-sync.request-handler.subs       :as request-handler.subs]
              [x.app-sync.response-handler.subs      :as response-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :sync/abort-request!
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  [:sync/abort-request! :my-request]
  (fn [{:keys [db]} [_ request-id]]
      {:db (r request-handler.events/request-aborted db request-id)
       :fx [:ajax/abort-request! request-id]}))

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
  ;   :source-path (vector)(opt)
  ;    Milyen Re-Frame adatbázis útvonalon tárolt adatot küldjön el paraméterként
  ;    Only w/ {:method :post}
  ;   :target-path (vector)(opt)
  ;    Milyen Re-Frame adatbázis útvonalra mentse el a szerver válaszát
  ;    Only w/ {:response-action :store}
  ;   :uri (string)
  ;    "/sample-uri"
  ;   :validator-f (function)(opt)}
  ;
  ; @usage
  ;  [:sync/send-request! {...}]
  ;
  ; @usage
  ;  [:sync/send-request! :my-request {...}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ request-id request-props]]
      (let [request-props (r request-handler.prototypes/request-props-prototype db request-props)]
           (if (r a/start-process? db request-id)
               {:db (r request-handler.events/send-request! db request-id request-props)
                :fx       [:ajax/send-request! request-id request-props]
                :dispatch [:sync/request-sent  request-id]}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :sync/request-sent
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  (fn [{:keys [db]} [_ request-id]]
      ; Dispatch request on-sent event
      (r request-handler.subs/get-request-on-sent-event db request-id)))

(a/reg-event-fx
  :sync/request-successed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (string) server-response-body
  ;  "{...}"
  (fn [{:keys [db]} [_ request-id server-response-body]]
      (if (r response-handler.subs/request-response-invalid? db request-id server-response-body)
          ; If request-response is invalid ...
          (let [invalid-server-response (r response-handler.subs/get-invalid-server-response db request-id server-response-body)]
               {:fx       [:core/print-warning! request-handler.config/INVALID-REQUEST-RESPONSE-ERROR request-id]
                :dispatch [:sync/request-failured request-id invalid-server-response]})
          ; If request-response is valid ...
          (let [server-response (reader/string->mixed server-response-body)
                request-props   (assoc (get-in db [:sync :request-handler/data-items request-id]) :request-successed? true)]
               {:db (r request-handler.events/request-successed db request-id server-response)
                :dispatch-n     [(r request-handler.subs/get-request-on-success-event   db request-id server-response)
                                 (r request-handler.subs/get-request-on-responsed-event db request-id server-response)]
                :dispatch-if    [(r response-handler.subs/save-request-response?        db request-id)
                                 [:sync/save-request-response! request-id server-response-body]]
                :dispatch-later [{:ms (r request-handler.subs/get-request-idle-timeout db request-id)
                                  :dispatch [:sync/request-stalled request-id request-props server-response]}]}))))

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
      (let [request-props (assoc (get-in db [:sync :request-handler/data-items request-id]) :request-failured? true)]
           {:db (r request-handler.events/request-failured db request-id server-response)
            :dispatch-n     [(r request-handler.subs/get-request-on-failure-event   db request-id server-response)
                             (r request-handler.subs/get-request-on-responsed-event db request-id server-response)]
            :dispatch-later [{:ms (r request-handler.subs/get-request-idle-timeout db request-id)
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
      {:db       (r request-handler.events/request-stalled            db request-id request-props)
       :dispatch (r request-handler.subs/get-request-on-stalled-event db request-id request-props server-response)}))
