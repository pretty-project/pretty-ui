
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.download.effects
    (:require [engines.item-handler.body.subs           :as body.subs]
              [engines.item-handler.core.subs           :as core.subs]
              [engines.item-handler.download.events     :as download.events]
              [engines.item-handler.download.queries    :as download.queries]
              [engines.item-handler.download.validators :as download.validators]
              [re-frame.api                             :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  (fn [{:keys [db]} [_ handler-id]]
      ; XXX#4057
      ; Az on-success helyett on-stalled időzítéssel a UI változásai
      ; egyszerre történnek meg a lekérés okozta {:handler-synchronizing? true}
      ; állapot megszűnésével.
      (let [display-progress? (r body.subs/get-body-prop                          db handler-id :display-progress?)
            query             (r download.queries/get-request-item-query          db handler-id)
            validator-f      #(r download.validators/request-item-response-valid? db handler-id %)]
           [:pathom/send-query! (r core.subs/get-request-id db handler-id)
                                {:display-progress? display-progress?
                                 :on-stalled [:item-handler/receive-item!     handler-id]
                                 :on-failure [:item-handler/set-engine-error! handler-id :failed-to-request-item]
                                 :query query :validator-f validator-f}])))

(r/reg-event-fx :item-handler/receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ handler-id server-response]]
      ; XXX#3007
      ; Ha az [:item-handler/receive-item! ...] esemény megtörténésekor a 'body'
      ; komponens már nincs a React-fába csatolva, akkor az esemény nem végez műveletet.
      (if (r body.subs/body-did-mount? db handler-id)
          {:db       (r download.events/receive-item! db handler-id server-response)
           :dispatch [:item-handler/item-received handler-id]})))

(r/reg-event-fx :item-handler/item-received
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  (fn [{:keys [db]} [_ handler-id]]
      (if-let [auto-title (r core.subs/get-auto-title db handler-id)]
              [:x.ui/set-tab-title! auto-title])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/request-suggestions!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  (fn [{:keys [db]} [_ handler-id]]
      (let [query (r download.queries/get-request-suggestions-query db handler-id)]
           [:pathom/send-query! (r core.subs/get-request-id db handler-id)
                                {:display-progress? false
                                 :on-success [:item-handler/receive-suggestions! handler-id]
                                 :query query}])))

(r/reg-event-fx :item-handler/receive-suggestions!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ handler-id server-response]]
      ; XXX#3007
      (if (r body.subs/body-did-mount? db handler-id)
          {:db (r download.events/receive-suggestions! db handler-id server-response)})))
