
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.routes.effects
    (:require [plugins.item-editor.routes.events :as routes.events]
              [plugins.item-editor.transfer.subs :as transfer.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  :item-editor/handle-route!
  (fn [{:keys [db]} [_ editor-id]]
      (let [on-route (r transfer.subs/get-transfer-item db editor-id :on-route)]
           {:db (r routes.events/handle-route! db editor-id)
            :dispatch on-route})))
