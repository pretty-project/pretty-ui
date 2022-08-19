

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.effects
    (:require [plugins.item-editor.body.subs     :as body.subs]
              [plugins.item-editor.core.subs     :as core.subs]
              [plugins.item-editor.core.events   :as core.events]
              [plugins.item-editor.routes.subs   :as routes.subs]
              [plugins.item-editor.transfer.subs :as transfer.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/edit-item!
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-editor/edit-item! :my-editor "my-item"]
  (fn [{:keys [db]} [_ editor-id item-id]]
      ; XXX#5575
      (if-let [route-handled? (r routes.subs/route-handled? db editor-id)]
              (let [edit-route (r routes.subs/get-edit-route db editor-id item-id)]
                   {:dispatch [:router/go-to! edit-route]})
              (if (r body.subs/body-did-mount? db editor-id)
                  {:db       (r core.events/set-item-id! db editor-id item-id)
                   :dispatch [:item-editor/load-editor! editor-id]}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/cancel-item!
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

(a/reg-event-fx
  :item-editor/load-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      {:db       (r core.events/load-editor! db editor-id)
       :dispatch [:item-editor/request-item! editor-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/go-up!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      (if (r core.subs/new-item? db editor-id)
          (let [base-route (r transfer.subs/get-transfer-item db editor-id :base-route)]
               [:router/go-to! base-route])
          (let [current-item-id (r core.subs/get-current-item-id db editor-id)
                item-route      (r routes.subs/get-item-route    db editor-id current-item-id)]
               [:router/go-to! item-route]))))
