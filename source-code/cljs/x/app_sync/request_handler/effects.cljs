
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



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
  ;  A lekéréssel párhuzamosan elindul egy :core/process folyamat is amelynek
  ;  azonosíja megegyezik a request-id azonosítóval, ennek köszönhetően a lekérés
  ;  státuszát, aktivitását és folyamatállapotát lehetséges figyelni a :core/process-handler
  ;  kezelő használatával (a request-id azonosítóra hivatkozva).
  ; @param (map) request-props
  ;  {:body (*)(opt)
  ;   :display-progress? (boolean)(opt)
  ;    Default: false
  ;   :idle-timeout (ms)(opt)
  ;    Default: request-handler.config/DEFAULT-IDLE-TIMEOUT
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
  ;   :response-f (function)(opt)
  ;   :timeout (ms)(opt)
  ;    Default: request-handler.config/DEFAULT-REQUEST-TIMEOUT
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
      ; A request-props térkép az események láncolata paraméterként adja tovább, így az egyes
      ; lekérésekből egy időben több példányt is tud kezelni.
      ; Pl. Az egyes lekérések [:sync/request-successed ...] eseménye és [:sync/request-stalled ...]
      ;      eseménye között újra elküldhetők eltérő beállításokkal, ami miatt szükséges a beállításokat
      ;      tartalmazó request-props térképet paraméterként átadni az eseményeknek és függvényeknek!
      (let [request-props (r request-handler.prototypes/request-props-prototype db request-props)]
           (if (r a/start-process? db request-id)
               {:db       (r request-handler.events/send-request! db request-id request-props)
                :fx       [:ajax/send-request! request-id request-props]
                :dispatch [:sync/request-sent  request-id request-props]}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :sync/request-sent
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:on-sent (metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ _ {:keys [on-sent]}]]
      ; Dispatch request on-sent event
      {:dispatch on-sent}))

(a/reg-event-fx
  :sync/request-successed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:idle-timeout (ms)}
  ; @param (string) server-response-body
  ;  "{...}"
  (fn [{:keys [db]} [_ request-id {:keys [idle-timeout] :as request-props} server-response-body]]
      (if (r response-handler.subs/request-response-invalid? db request-id request-props server-response-body)
          ; If request-response is invalid ...
          (let [invalid-server-response (r response-handler.subs/get-invalid-server-response db request-id request-props server-response-body)]
               {:fx       [:core/print-warning!   request-handler.config/INVALID-REQUEST-RESPONSE-ERROR request-id]
                :dispatch [:sync/request-failured request-id request-props invalid-server-response]})
          ; If request-response is valid ...
          (let [server-response (reader/string->mixed server-response-body)
                request-props   (assoc request-props :request-successed? true)]
               {:db              (r request-handler.events/request-successed            db request-id request-props server-response)
                :dispatch-n     [(r request-handler.subs/get-request-on-success-event   db request-id request-props server-response)
                                 (r request-handler.subs/get-request-on-responsed-event db request-id request-props server-response)]
                :dispatch-later [{:ms idle-timeout :dispatch [:sync/request-stalled request-id request-props server-response]}]}))))

(a/reg-event-fx
  :sync/request-failured
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:idle-timeout (ms)}
  ; @param (map) server-response
  ;  {:status (integer)
  ;   :status-text (string)
  ;   :failure (keyword)
  ;    :error, :parse, :aborted, :timeout}
  ;   :response (string)
  ;    server-response-body}
  (fn [{:keys [db]} [_ request-id {:keys [idle-timeout] :as request-props} {:keys [status-text] :as server-response}]]
      (let [request-props (assoc request-props :request-failured? true)]
           {:db              (r request-handler.events/request-failured             db request-id request-props server-response)
            :dispatch-n     [(r request-handler.subs/get-request-on-failure-event   db request-id request-props server-response)
                             (r request-handler.subs/get-request-on-responsed-event db request-id request-props server-response)]
            :dispatch-later [{:ms idle-timeout :dispatch [:sync/request-stalled request-id request-props server-response]}]})))

(a/reg-event-fx
  :sync/request-stalled
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ; @param (map) server-response
  (fn [{:keys [db]} [_ request-id request-props server-response]]
      {:db       (r request-handler.events/request-stalled            db request-id request-props)
       :dispatch (r request-handler.subs/get-request-on-stalled-event db request-id request-props server-response)}))
