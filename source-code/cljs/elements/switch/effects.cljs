
(ns elements.switch.effects
    (:require [elements.checkbox.events :as checkbox.events]
              [re-frame.api             :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.switch/toggle-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; @param (*) option
  (fn [{:keys [db]} [_ switch-id switch-props option]]
      {:db (r checkbox.events/toggle-option! db switch-id switch-props option)
       :fx [:elements.input/mark-input-as-visited! switch-id]}))
