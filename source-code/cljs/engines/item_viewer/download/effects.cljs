
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-viewer.download.effects
    (:require [engines.item-viewer.body.subs           :as body.subs]
              [engines.item-viewer.core.subs           :as core.subs]
              [engines.item-viewer.download.events     :as download.events]
              [engines.item-viewer.download.queries    :as download.queries]
              [engines.item-viewer.download.validators :as download.validators]
              [re-frame.api                            :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-viewer/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  (fn [{:keys [db]} [_ viewer-id]]
      (let [display-progress? (r body.subs/get-body-prop                          db viewer-id :display-progress?)
            query             (r download.queries/get-request-item-query          db viewer-id)
            validator-f      #(r download.validators/request-item-response-valid? db viewer-id %)]
           {:db       (r download.events/request-item! db viewer-id)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db viewer-id)
                                           {:display-progress? display-progress?
                                            ; XXX#4057 (source-code/cljs/engines/item_handler/download/effects.cljs)
                                            :on-stalled [:item-viewer/receive-item!     viewer-id]
                                            :on-failure [:item-viewer/set-engine-error! viewer-id :failed-to-request-item]
                                            :query query :validator-f validator-f}]})))

(r/reg-event-fx :item-viewer/receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  (fn [{:keys [db]} [_ viewer-id server-response]]
      ; Ha az [:item-viewer/receive-item! ...] esemény megtörténésekor a body komponens már
      ; nincs a React-fába csatolva, akkor az esemény nem végez műveletet.
      (if (r body.subs/body-did-mount? db viewer-id)
          {:db       (r download.events/receive-item! db viewer-id server-response)
           :dispatch [:item-viewer/item-received viewer-id]})))

(r/reg-event-fx :item-viewer/item-received
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  (fn [{:keys [db]} [_ viewer-id]]
      (if-let [auto-title (r core.subs/get-auto-title db viewer-id)]
              [:x.ui/set-window-title! auto-title])))
