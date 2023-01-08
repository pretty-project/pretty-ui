
(ns elements.checkbox.effects
    (:require [elements.checkbox.events :as checkbox.events]
              [re-frame.api             :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.checkbox/toggle-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; @param (*) option
  (fn [{:keys [db]} [_ checkbox-id checkbox-props option]]
      {:db (r checkbox.events/toggle-option! db checkbox-id checkbox-props option)
       :fx [:elements.input/mark-input-as-visited! checkbox-id]}))
