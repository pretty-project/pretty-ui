
(ns elements.counter.effects
    (:require [elements.counter.events :as counter.events]
              [re-frame.api            :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.counter/counter-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {:initial-value (integer)(opt)}
  (fn [{:keys [db]} [_ counter-id {:keys [initial-value] :as counter-props}]]
      (if initial-value {:db (r counter.events/counter-did-mount db counter-id counter-props)})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.counter/increase-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  (fn [{:keys [db]} [_ counter-id counter-props]]
      {:db (r counter.events/increase-value! db counter-id counter-props)
       :fx [:elements.input/mark-input-as-visited! counter-id]}))

(r/reg-event-fx :elements.counter/decrease-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  (fn [{:keys [db]} [_ counter-id counter-props]]
      {:db (r counter.events/decrease-value! db counter-id counter-props)
       :fx [:elements.input/mark-input-as-visited! counter-id]}))
