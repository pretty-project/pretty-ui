
(ns pretty-elements.counter.effects
    (:require [pretty-elements.counter.events :as counter.events]
              [re-frame.api            :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-elements.counter/counter-did-mount
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {:initial-value (integer)(opt)}
  (fn [{:keys [db]} [_ counter-id {:keys [initial-value] :as counter-props}]]
      (if initial-value {:db (r counter.events/counter-did-mount db counter-id counter-props)})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-elements.counter/increase-value!
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  (fn [{:keys [db]} [_ counter-id counter-props]]
      {:db (r counter.events/increase-value! db counter-id counter-props)
       :fx [:pretty-elements.input/mark-input-as-visited! counter-id]}))

(r/reg-event-fx :pretty-elements.counter/decrease-value!
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  (fn [{:keys [db]} [_ counter-id counter-props]]
      {:db (r counter.events/decrease-value! db counter-id counter-props)
       :fx [:pretty-elements.input/mark-input-as-visited! counter-id]}))
