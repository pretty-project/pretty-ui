
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.download.effects
    (:require [plugins.item-editor.body.subs           :as body.subs]
              [plugins.item-editor.core.subs           :as core.subs]
              [plugins.item-editor.download.events     :as download.events]
              [plugins.item-editor.download.queries    :as download.queries]
              [plugins.item-editor.download.validators :as download.validators]
              [x.app-core.api                          :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      (let [query        (r download.queries/get-request-item-query          db editor-id)
            validator-f #(r download.validators/request-item-response-valid? db editor-id %)]
           {:db       (r download.events/request-item! db editor-id)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db editor-id)
                                           {:display-progress? true
                                            ; XXX#4057
                                            ; Az on-stalled időzítéssel a UI változásai egyszerre történnek
                                            ; meg a lekérés okozta {:editor-disabled? true} állapot megszűnésével
                                            :on-stalled [:item-editor/receive-item!   editor-id]
                                            :on-failure [:item-editor/set-error-mode! editor-id]
                                            :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-editor/receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id server-response]]
      ; Ha az [:item-editor/receive-item! ...] esemény megtörténésekor a body komponens már
      ; nincs a React-fába csatolva, akkor az esemény nem végez műveletet.
      (if (r body.subs/body-did-mount? db editor-id)
          {:db       (r download.events/receive-item! db editor-id server-response)
           :dispatch [:item-editor/item-received editor-id]})))

(a/reg-event-fx
  :item-editor/item-received
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      (if-let [auto-title (r core.subs/get-auto-title db editor-id)]
              [:ui/set-window-title! auto-title])))
