
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.effects
    (:require [plugins.item-lister.core.events :as core.events]
              [x.app-core.api                  :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/filter-items!
  ; @param (keyword) lister-id
  ; @param (map) filter-pattern
  ;
  ; @usage
  ;  [:item-lister/filter-items! :my-lister {...}]
  (fn [{:keys [db]} [_ lister-id filter-pattern]]
      {:db       (r core.events/filter-items! db lister-id filter-pattern)
       :dispatch [:item-lister/request-items! lister-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/search-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  (fn [{:keys [db]} [_ lister-id]]
      {:db       (r core.events/reset-downloads! db lister-id)
       :dispatch [:item-lister/request-items! lister-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/order-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  (fn [{:keys [db]} [_ lister-id]]
      {:db       (r core.events/reset-downloads! db lister-id)
       :dispatch [:item-lister/request-items! lister-id]}))
