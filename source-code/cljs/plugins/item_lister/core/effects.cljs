
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.effects
    (:require [plugins.item-lister.core.events  :as core.events]
              [plugins.item-lister.core.helpers :as core.helpers]
              [x.app-core.api                   :as a :refer [r]]))



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
  ; @param (map) search-props
  ;  {:search-keys (keywords in vector)}
  ; @param (string) search-term
  (fn [{:keys [db]} [_ lister-id search-props search-term]]
      {:db       (r core.events/search-items! db lister-id search-props search-term)
       :dispatch [:item-lister/request-items! lister-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/order-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (namespaced keyword) order-by
  (fn [{:keys [db]} [_ lister-id order-by]]
      {:db       (r core.events/order-items! db lister-id order-by)
       :dispatch [:item-lister/request-items! lister-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/choose-order-by!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) order-by-props
  ;  {:order-by-options (namespaced keywords in vector)}
  (fn [{:keys [db]} [_ lister-id {:keys [order-by-options]}]]
      [:elements.select/render-select! :item-lister/order-by-select
                                       {:get-label-f     core.helpers/order-by-label-f
                                        :initial-options order-by-options
                                        :on-popup-closed [:item-lister/order-items! lister-id]
                                        :options-label   :order-by
                                        :value-path      [:plugins :plugin-handler/meta-items lister-id :order-by]}]))
