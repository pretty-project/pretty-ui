
(ns elements.color-selector.effects
    (:require [elements.color-selector.events     :as color-selector.events]
              [elements.color-selector.prototypes :as color-selector.prototypes]
              [elements.color-selector.views      :as color-selector.views]
              [re-frame.api                       :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.color-selector/render-selector!
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:options (strings in vector)(opt)
  ;  :options-label (metamorphic-content)(opt)
  ;  :options-path (vector)(opt)
  ;  :value-path (vector)(opt)}
  ;
  ; @usage
  ; [:elements.color-selector/render-selector! {...}]
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ selector-id selector-props]]
      (let [selector-props (r color-selector.prototypes/selector-props-prototype db selector-id selector-props)]
           {:fx       [:elements.input/mark-input-as-visited!   selector-id]
            :dispatch [:elements.color-selector/render-options! selector-id selector-props]})))

(r/reg-event-fx :elements.color-selector/render-options!
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  (fn [_ [_ selector-id selector-props]]
      [:x.ui/render-popup! :elements.color-selector/options
                           {:content [color-selector.views/color-selector-options selector-id selector-props]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.color-selector/toggle-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:on-select (metamorphic-event)(opt)}
  ; @param (string) option
  (fn [{:keys [db]} [_ selector-id {:keys [on-select] :as selector-props} option]]
      {:db (r color-selector.events/toggle-color-selector-option! db selector-id selector-props option)
       :dispatch on-select}))
