
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.download.effects
    (:require [plugins.item-viewer.body.subs           :as body.subs]
              [plugins.item-viewer.core.subs           :as core.subs]
              [plugins.item-viewer.download.events     :as download.events]
              [plugins.item-viewer.download.queries    :as download.queries]
              [plugins.item-viewer.download.validators :as download.validators]
              [re-frame.api                            :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-viewer/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  (fn [{:keys [db]} [_ viewer-id]]
      (let [query        (r download.queries/get-request-item-query          db viewer-id)
            validator-f #(r download.validators/request-item-response-valid? db viewer-id %)]
           {:db       (r download.events/request-item! db viewer-id)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db viewer-id)
                                           {:display-progress? true
                                            ; XXX#4057
                                            :on-stalled [:item-viewer/receive-item!   viewer-id]
                                            :on-failure [:item-viewer/set-error-mode! viewer-id]
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
              [:ui/set-window-title! auto-title])))
