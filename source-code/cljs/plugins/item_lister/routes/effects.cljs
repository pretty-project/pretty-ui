
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.routes.effects
    (:require [plugins.item-lister.transfer.subs :as transfer.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  (fn [{:keys [db]} [_ lister-id]]
      (let [on-route    (r transfer.subs/get-transfer-item db lister-id :on-route)
            route-title (r transfer.subs/get-transfer-item db lister-id :route-title)]
           {;:db (r routes.events/handle-route! db lister-id)
            :dispatch-n [on-route (if route-title [:ui/set-window-title! route-title])]})))
