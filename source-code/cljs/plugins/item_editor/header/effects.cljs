
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.header.effects
    (:require [plugins.item-editor.core.events   :as core.events]
              [plugins.item-editor.core.subs     :as core.subs]
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
      ;    ... akkor elkészíti a nézethez tartozó útvonalat és átirányít arra.
      ;
      ; B) Ha az item-editor plugin NEM rendelkezik az útvonal elkészítéséhez szükséges tulajdonságokkal, ...
      ;    ... akkor útvonal használata nélkül eltárolja a nézet azonosítóját.
      (if-let [base-route (r transfer.subs/get-transfer-item db editor-id :base-route)]
              ; A)
              (let [current-item-id (r core.subs/get-current-item-id db editor-id)
                    view-route      (r routes.subs/get-view-route    db editor-id current-item-id view-id)]
                   [:router/go-to! view-route])
              ; B)
              {:db (r core.events/set-view-id! db editor-id view-id)})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/header-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) header-props
  (fn [{:keys [db]} [_ editor-id header-props]]
      {:db (r header.events/header-did-mount db editor-id header-props)}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/header-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      {:db (r header.events/header-will-unmount db editor-id)}))
