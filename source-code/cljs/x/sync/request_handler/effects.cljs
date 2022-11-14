
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.sync.request-handler.effects
    (:require [ajax.api]
              [re-frame.api                      :as r :refer [r]]
              [reader.api                        :as reader]
              [x.core.api                        :as x.core]
              [x.sync.request-handler.config     :as request-handler.config]
              [x.sync.request-handler.events     :as request-handler.events]
              [x.sync.request-handler.prototypes :as request-handler.prototypes]
              [x.sync.request-handler.subs       :as request-handler.subs]
              [x.sync.response-handler.subs      :as response-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.sync/abort-request!
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  [:x.sync/abort-request! :my-request]
  (fn [{:keys [db]} [_ request-id]]
      {:db (r request-handler.events/request-aborted db request-id)
       :fx [:ajax/abort-request! request-id]}))

(r/reg-event-fx :x.sync/send-request!
  ; @param (keyword)(opt) request-id
  ;  A lekéréssel párhuzamosan elindul egy :x.core/process folyamat is amelynek
  ;  azonosíja megegyezik a request-id azonosítóval, ennek köszönhetően a lekérés
  ;  státuszát, aktivitását és folyamatállapotát lehetséges figyelni a :x.core/process-handler
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
  ;    Az esemény utolsó paraméterként megkapja a szerver-válasz értékét.
  ;   :on-responsed (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a szerver-válasz értékét.
  ;   :on-sent (metamorphic-event)(opt)
  ;   :on-stalled (metamorphic-event)(opt)
  ;    A szerver-válasz visszaérkezése utáni idle-timeout időtartam lejárta után megtörténő esemény.
  ;    Az esemény utolsó paraméterként megkapja a szerver-válasz értékét.
  ;   :on-success (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a szerver-válasz értékét.
  ;   :params (map)(opt)
  ;    W/ {:method :post}
  ;   :refresh-interval (ms)(opt)
  ;   :response-f (function)(opt)
  ;   :timeout (ms)(opt)
  ;    Default: request-handler.config/DEFAULT-REQUEST-TIMEOUT
  ;   :uri (string)
  ;    "/sample-uri"
  ;   :validator-f (function)(opt)}
  ;
  ; @usage
  ;  [:x.sync/send-request! {...}]
  ;
  ; @usage
  ;  [:x.sync/send-request! :my-request {...}]
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ request-id request-props]]
      ; A request-props térkép az események láncolata paraméterként adja tovább, így az egyes
      ; lekérésekből egy időben több példányt is tud kezelni.
      ; Pl.: Az egyes lekérések [:x.sync/request-successed ...] eseménye és [:x.sync/request-stalled ...]
      ;      eseménye között újra elküldhetők eltérő beállításokkal, ami miatt szükséges a beállításokat
      ;      tartalmazó request-props térképet paraméterként átadni az eseményeknek és függvényeknek!
      (let [request-props (r request-handler.prototypes/request-props-prototype db request-props)]
           (if (r x.core/start-process? db request-id)
               {:db       (r request-handler.events/send-request! db request-id request-props)
                :fx       [:ajax/send-request!  request-id request-props]
                :dispatch [:x.sync/request-sent request-id request-props]}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.sync/request-sent
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:on-sent (metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ _ {:keys [on-sent]}]]
      ; Dispatch request on-sent event
      {:dispatch on-sent}))

(r/reg-event-fx :x.sync/request-successed
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
               {:fx       [:x.core/print-warning! request-handler.config/INVALID-REQUEST-RESPONSE-ERROR request-id]
                :dispatch [:x.sync/request-failured request-id request-props invalid-server-response]})
          ; If request-response is valid ...
          (let [server-response (reader/string->mixed server-response-body)
                request-props   (assoc request-props :request-successed? true)]
               {:db              (r request-handler.events/request-successed            db request-id request-props server-response)
                :dispatch-n     [(r request-handler.subs/get-request-on-success-event   db request-id request-props server-response)
                                 (r request-handler.subs/get-request-on-responsed-event db request-id request-props server-response)]
                :dispatch-later [{:ms idle-timeout :dispatch [:x.sync/request-stalled request-id request-props server-response]}]}))))

(r/reg-event-fx :x.sync/request-failured
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
  (fn [{:keys [db]} [_ request-id {:keys [idle-timeout] :as request-props} server-response]]
      (let [request-props   (assoc  request-props   :request-failured? true)
            server-response (update server-response :response reader/string->mixed)]
           {:db              (r request-handler.events/request-failured             db request-id request-props server-response)
            :dispatch-n     [(r request-handler.subs/get-request-on-failure-event   db request-id request-props server-response)
                             (r request-handler.subs/get-request-on-responsed-event db request-id request-props server-response)]
            :dispatch-later [{:ms idle-timeout :dispatch [:x.sync/request-stalled request-id request-props server-response]}]})))

(r/reg-event-fx :x.sync/request-stalled
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:refresh-interval (ms)(opt)}
  ; @param (map) server-response
  (fn [{:keys [db]} [_ request-id {:keys [refresh-interval] :as request-props} server-response]]
      {:db       (r request-handler.events/request-stalled            db request-id request-props)
       :dispatch (r request-handler.subs/get-request-on-stalled-event db request-id request-props server-response)
       :dispatch-later [(if refresh-interval {:ms refresh-interval :dispatch [:x.sync/send-request! request-id request-props]})]}))
