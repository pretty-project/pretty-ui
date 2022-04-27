
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.color-selector.effects
    (:require [x.app-core.api                           :as a :refer [r]]
              [x.app-elements.color-selector.prototypes :as color-selector.prototypes]
              [x.app-elements.color-selector.views      :as color-selector.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements/render-color-selector-options!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  (fn [{:keys [db]} [_ selector-id selector-props]]
      (let [options-props (r color-selector.prototypes/options-props-prototype db selector-id selector-props)]
           [:ui/render-popup! :elements/color-selector-options
                              {:body [color-selector.views/color-selector-options selector-id options-props]
                               :min-width :none}])))
