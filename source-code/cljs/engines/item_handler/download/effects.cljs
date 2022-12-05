
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
      (let [display-progress? (r body.subs/get-body-prop                          db editor-id :display-progress?)
            query             (r download.queries/get-request-item-query          db handler-id)
            validator-f      #(r download.validators/request-item-response-valid? db handler-id %)]
           {:db       (r download.events/request-item! db handler-id)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db handler-id)
                                           {:display-progress? display-progress?
                                            ; XXX#4057
                                            ; Az on-success helyett on-stalled időzítéssel a UI változásai
                                            ; egyszerre történnek meg a lekérés okozta {:handler-disabled? true}
                                            ; állapot megszűnésével.
                                            :on-stalled [:item-handler/receive-item!     handler-id]
                                            :on-failure [:item-handler/set-engine-error! handler-id :failed-to-request-item]
                                            :query query :validator-f validator-f}]})))

(r/reg-event-fx :item-handler/receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ handler-id server-response]]
      ; Ha az [:item-handler/receive-item! ...] esemény megtörténésekor a body komponens már
      ; nincs a React-fába csatolva, akkor az esemény nem végez műveletet.
      (if (r body.subs/body-did-mount? db handler-id)
          {:db       (r download.events/receive-item! db handler-id server-response)
           :dispatch [:item-handler/item-received handler-id]})))

(r/reg-event-fx :item-handler/item-received
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  (fn [{:keys [db]} [_ handler-id]]
      (if-let [auto-title (r core.subs/get-auto-title db handler-id)]
              [:x.ui/set-window-title! auto-title])))
