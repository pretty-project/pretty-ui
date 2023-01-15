
(ns templates.item-lister.core.effects
    (:require [re-frame.api                       :as r]
              [templates.item-lister.core.helpers :as core.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/choose-order-by!
  ; @param (keyword) lister-id
  ; @param (map) order-by-props
  ; {:options (keywords or namespaced keywords in vector)}
  ;
  ; @usage
  ; [:item-lister/choose-order-by! :my-lister {...}]
  (fn [_ [_ lister-id {:keys [options]}]]
      [:elements.select/render-select! :item-lister/order-by-select
                                       {:option-label-f  core.helpers/order-by-label-f
                                        :initial-options options
                                        :on-select       [:item-lister/order-items! lister-id]
                                        :options-label   :order-by
                                        :value-path      [:engines :engine-handler/meta-items lister-id :order-by]}]))
