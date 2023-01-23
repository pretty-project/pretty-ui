
(ns elements.radio-button.effects
    (:require [elements.radio-button.events :as radio-button.events]
              [re-frame.api                 :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.radio-button/button-did-mount
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:initial-options (vector)(opt)
  ;  :initial-value (*)(opt)}
  (fn [{:keys [db]} [_ button-id {:keys [initial-options initial-value] :as button-props}]]
      (if (or initial-options initial-value)
          {:db (r radio-button.events/button-did-mount db button-id button-props)})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.radio-button/select-option!
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:on-select (metamorphic-event)(opt)}
  ; @param (*) option
  (fn [{:keys [db]} [_ button-id {:keys [on-select] :as button-props} option]]
      {:db (r radio-button.events/select-option! db button-id button-props option)
       :fx [:elements.input/mark-input-as-visited! button-id]
       :dispatch on-select}))

(r/reg-event-fx :elements.radio-button/clear-value!
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  (fn [{:keys [db]} [_ button-id button-props]]
      {:db (r radio-button.events/clear-value! db button-id button-props)
       :fx [:elements.input/mark-input-as-visited! button-id]}))
