
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.effects
    (:require [mid-fruits.logical               :refer [swap]]
              [plugins.item-lister.core.events  :as core.events]
              [plugins.item-lister.core.helpers :as core.helpers]
              [plugins.item-lister.core.subs    :as core.subs]
              [re-frame.api                     :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx
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

(r/reg-event-fx
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

(r/reg-event-fx
  :item-lister/order-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (namespaced keyword) order-by
  (fn [{:keys [db]} [_ lister-id order-by]]
      {:db       (r core.events/order-items! db lister-id order-by)
       :dispatch [:item-lister/request-items! lister-id]}))

(r/reg-event-fx
  :item-lister/swap-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  (fn [{:keys [db]} [_ lister-id]]
      ; ...
      (let [current-order-by (r core.subs/get-meta-item db lister-id :order-by)
            current-order-by-direction (name      current-order-by)
            current-order-by-key       (namespace current-order-by)
            swap-order-by-direction    (swap current-order-by-direction "descending" "ascending")
            swap-order-by              (keyword current-order-by-key swap-order-by-direction)]
           [:item-lister/order-items! lister-id swap-order-by])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx
  :item-lister/choose-order-by!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) order-by-props
  ;  {:order-by-options (namespaced keywords in vector)}
  (fn [{:keys [db]} [_ lister-id {:keys [order-by-options]}]]
      [:elements.select/render-select! :item-lister/order-by-select
                                       {:option-label-f  core.helpers/order-by-label-f
                                        :initial-options order-by-options
                                        :on-select      [:item-lister/order-items! lister-id]
                                        :options-label   :order-by
                                        :value-path      [:plugins :plugin-handler/meta-items lister-id :order-by]}]))
