
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.effects
    (:require [plugins.item-lister.core.events   :as core.events]
              [plugins.item-lister.transfer.subs :as transfer.subs]
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
           {;:db (r core.events/handle-route! db lister-id)
            :dispatch-n [on-route (if route-title [:ui/set-title! route-title])]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/use-filter!
  ; @param (keyword) lister-id
  ; @param (keyword) item-namespace
  ; @param (map) filter-pattern
  ;
  ; @usage
  ;  [:item-lister/use-filter! :my-lister {...}]
  (fn [{:keys [db]} [_ lister-id filter-pattern]]
      {:db (r core.events/use-filter! db lister-id filter-pattern)
       :dispatch [:tools/reload-infinite-loader! lister-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/search-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ lister-id]]
      {:db (r core.events/reset-downloads! db lister-id)
       :dispatch [:tools/reload-infinite-loader! lister-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/order-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ lister-id]]
      {:db (r core.events/reset-downloads! db lister-id)
       :dispatch [:tools/reload-infinite-loader! lister-id]}))
