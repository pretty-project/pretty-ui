
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.core.effects
    (:require [engines.item-editor.body.subs     :as body.subs]
              [engines.item-editor.core.subs     :as core.subs]
              [engines.item-editor.core.events   :as core.events]
              [engines.item-editor.routes.subs   :as routes.subs]
              [engines.item-editor.transfer.subs :as transfer.subs]
              [re-frame.api                      :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-editor/edit-item!
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-editor/edit-item! :my-editor "my-item"]
  (fn [{:keys [db]} [_ editor-id item-id]]
      ; XXX#5575 (source-code/cljs/engines/item_handler/core/effects.cljs)
      (if-let [route-handled? (r routes.subs/route-handled? db editor-id)]
              (let [edit-route (r routes.subs/get-edit-route db editor-id item-id)]
                   {:dispatch [:x.router/go-to! edit-route]})
              (if (r body.subs/body-did-mount? db editor-id)
                  {:db       (r core.events/set-item-id! db editor-id item-id)
                   :dispatch [:item-editor/load-editor! editor-id]}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-editor/cancel-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      ; A) ...
      ;
      ; B) ...
      (if-let [route-handled? (r routes.subs/route-handled? db editor-id)]
              ; A)
              [:item-editor/go-up! editor-id])))
              ; B)
              ; ...



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-editor/load-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      {:db       (r core.events/load-editor! db editor-id)
       :dispatch [:item-editor/request-item! editor-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-editor/go-up!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      ; Nem minden esetben érhető el a current-item-id azonosító!
      ; Pl.: Új elem szerkesztése közben az [:item-editor/cancel-item! ...]
      ;      esemény hatására megtörténő [:item-editor/go-up! ...] esemény
      ;      számára nem elérhető az elem azonosítója, mivel az csak az új elem
      ;      mentésekor jön létre!
      (if-let [current-item-id (r core.subs/get-current-item-id db editor-id)]
              (let [item-route (r routes.subs/get-item-route db editor-id current-item-id)]
                   [:x.router/go-to! item-route])
              (let [base-route (r transfer.subs/get-transfer-item db editor-id :base-route)]
                   [:x.router/go-to! base-route]))))
