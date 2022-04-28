
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
      ; A) Ha az item-editor plugin útvonal-vezérelt, ...
      ;    ... akkor elkészíti az elem szerkesztéséhez az útvonalat és átirányít arra.
      ;
      ; B) Ha az item-editor plugin NEM útvonal-vezérelt és a body komponens a React-fába
      ;    van csatolva, ...
      ;    ... akkor beállítja az item-id paraméter értékét az aktuálisan szerkesztett
      ;        elem azonosítójaként.
      ;    ... meghívja az [:item-editor/load-editor! ...] eseményt.
      (if-let [route-handled? (r routes.subs/route-handled? db editor-id)]
              ; A)
              (let [item-route (r routes.subs/get-edit-route db editor-id item-id)]
                   {:dispatch [:router/go-to! item-route]})
              ; B)
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
              (if (r core.subs/new-item? db editor-id)
                  (let [base-route (r transfer.subs/get-transfer-item db editor-id :base-route)]
                       [:router/go-to! base-route])
                  (let [current-item-id (r core.subs/get-current-item-id db editor-id)
                        item-route      (r routes.subs/get-item-route    db editor-id current-item-id)]
                       [:router/go-to! item-route])))))
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
