
(ns elements.radio-button.effects
    (:require [elements.radio-button.events :as radio-button.events]
              [re-frame.api                 :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.radio-button/select-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; @param (*) option
  (fn [{:keys [db]} [_ button-id button-props option]]
      {:db (r radio-button.events/select-option! db button-id button-props option)
       :fx [:elements.input/mark-input-as-visited! button-id]}))

(r/reg-event-fx :elements.radio-button/clear-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  (fn [{:keys [db]} [_ button-id button-props]]
      {:db (r radio-button.events/clear-value! db button-id button-props)
       :fx [:elements.input/mark-input-as-visited! button-id]}))
