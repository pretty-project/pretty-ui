
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.core.effects
    (:require [engines.item-lister.core.events  :as core.events]
              [engines.item-lister.core.helpers :as core.helpers]
              [engines.item-lister.core.subs    :as core.subs]
              [logic.api                        :refer [swap]]
              [re-frame.api                     :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/filter-items!
  ; @param (keyword) lister-id
  ; @param (map) filter-pattern
  ;
  ; @usage
  ; [:item-lister/filter-items! :my-lister {...}]
  (fn [{:keys [db]} [_ lister-id filter-pattern]]
      {:db       (r core.events/filter-items! db lister-id filter-pattern)
       :dispatch [:item-lister/request-items! lister-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/search-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) search-props
  ; {:search-keys (keywords in vector)}
  ; @param (string) search-term
  (fn [{:keys [db]} [_ lister-id search-props search-term]]
      {:db       (r core.events/search-items! db lister-id search-props search-term)
       :dispatch [:item-lister/request-items! lister-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/order-items!
  ; @param (keyword) lister-id
  ; @param (keyword or namespaced keyword) order-by
  ;
  ; @usage
  ; [:item-lister/order-items! :my-lister :name]
  ;
  ; @usage
  ; [:item-lister/order-items! :my-lister :name/descending]
  (fn [{:keys [db]} [_ lister-id order-by]]
      (if-let [order-by-key (namespace order-by)]
              ; (A)
              {:db       (r core.events/order-items! db lister-id order-by)
               :dispatch [:item-lister/request-items! lister-id]}
              ; (B)
              (let [current-order-by (r core.subs/get-meta-item db lister-id :order-by)]
                   (if (= (namespace current-order-by)
                          (name              order-by))
                       ; (B1)
                       [:item-lister/swap-items! lister-id]
                       ; (B2)
                       (let [order-by (keyword (name order-by) "descending")]
                            {:db       (r core.events/order-items! db lister-id order-by)
                             :dispatch [:item-lister/request-items! lister-id]}))))))

(r/reg-event-fx :item-lister/swap-items!
  ; @param (keyword) lister-id
  ;
  ; @usage
  ; [:item-lister/swap-items! :my-lister]
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

(r/reg-event-fx :item-lister/choose-order-by!
  ; @param (keyword) lister-id
  ; @param (map) order-by-props
  ; {:order-by-options (keywords or namespaced keywords in vector)}
  ;
  ; @usage
  ; [:item-lister/choose-order-by! :my-lister {...}]
  (fn [{:keys [db]} [_ lister-id {:keys [order-by-options]}]]
      [:elements.select/render-select! :item-lister/order-by-select
                                       {:option-label-f  core.helpers/order-by-label-f
                                        :initial-options order-by-options
                                        :on-select       [:item-lister/order-items! lister-id]
                                        :options-label   :order-by
                                        :value-path      [:engines :engine-handler/meta-items lister-id :order-by]}]))
