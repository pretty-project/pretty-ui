
(ns elements.switch.effects
    (:require [elements.checkbox.events :as checkbox.events]
              [re-frame.api             :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.switch/switch-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:initial-options (vector)(opt)
  ;  :initial-value (boolean)(opt)}
  (fn [{:keys [db]} [_ switch-id {:keys [initial-options initial-value] :as switch-props}]]
      (if (or initial-options initial-value)
          {:db (r checkbox.events/checkbox-did-mount db switch-id switch-props)})))

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
