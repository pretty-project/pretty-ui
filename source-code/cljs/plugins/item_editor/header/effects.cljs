
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.header.effects
    (:require [plugins.item-editor.core.subs     :as core.subs]
              [plugins.item-editor.header.events :as header.events]
              [plugins.item-editor.routes.subs   :as routes.subs]
              [plugins.item-editor.transfer.subs :as transfer.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/change-view!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (keyword) view-id
  (fn [{:keys [db]} [_ editor-id view-id]]
      ; A) Ha az item-editor plugin rendelkezik az útvonal elkészítéséhez szükséges tulajdonságokkal, ...
      ;    ...
      ;
      ; B) Ha az item-editor plugin NEM rendelkezik az útvonal elkészítéséhez szükséges tulajdonságokkal, ...
      ;    ...
      (if-let [base-route (r transfer.subs/get-transfer-item db editor-id :base-route)]
              ; A)
              (let [current-item-id (r core.subs/get-current-item-id db editor-id)
                    view-route      (r routes.subs/get-view-route    db editor-id current-item-id view-id)]
                   [:router/go-to! view-route])
              ; B)
              {:db (r header.events/change-view! db editor-id view-id)})))
