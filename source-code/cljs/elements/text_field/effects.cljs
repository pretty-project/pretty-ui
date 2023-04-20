
(ns elements.text-field.effects
    (:require [elements.plain-field.env    :as plain-field.env]
              [elements.plain-field.events :as plain-field.events]
              [re-frame.api                :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.text-field/field-did-mount
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [_ [_ field-id field-props]]
      [:elements.plain-field/field-did-mount field-id field-props]))

(r/reg-event-fx :elements.text-field/field-will-unmount
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [_ [_ field-id field-props]]
      [:elements.plain-field/field-will-unmount field-id field-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.text-field/ENTER-pressed
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-enter (Re-Frame metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-enter]}]]
      {:dispatch on-enter}))

(r/reg-event-fx :elements.text-field/ESC-pressed
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:emptiable? (boolean)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [emptiable?] :as field-props}]]
      (if emptiable? [:elements.text-field/empty-field! field-id field-props])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.text-field/empty-field!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-empty (Re-Frame metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-empty] :as field-props}]]
      (if (plain-field.env/field-filled? field-id)
          {:dispatch (if on-empty (r/metamorphic-event<-params on-empty ""))
           :db       (r plain-field.events/empty-field! db field-id field-props)
           :fx       [:elements.plain-field/empty-field! field-id]})))
