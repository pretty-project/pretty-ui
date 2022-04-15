
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.routes.effects
    (:require [plugins.view-selector.routes.events :as routes.events]
              [plugins.view-selector.transfer.subs :as transfer.subs]
              [x.app-core.api                      :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  (fn [{:keys [db]} [_ selector-id]]
      (let [on-route    (r transfer.subs/get-transfer-item db selector-id :on-route)
            route-title (r transfer.subs/get-transfer-item db selector-id :route-title)]
           {:db (r routes.events/handle-route! db selector-id)
            :dispatch-n [on-route (if route-title [:ui/set-window-title! route-title])]})))
