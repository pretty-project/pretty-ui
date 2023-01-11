
(ns elements.text-field.effects
    (:require [elements.plain-field.helpers :as plain-field.helpers]
              [elements.plain-field.events  :as plain-field.events]
              [re-frame.api                 :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.text-field/field-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [_ [_ field-id field-props]]
      [:elements.plain-field/field-did-mount field-id field-props]))

(r/reg-event-fx :elements.text-field/field-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [_ [_ field-id field-props]]
      [:elements.plain-field/field-will-unmount field-id field-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.text-field/reg-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {}
  (fn [{:keys [db]} [_ field-id {:keys [emptiable? on-enter] :as field-props}]]
      (let [on-enter-props {:key-code 13 :on-keydown [:elements.text-field/ENTER-pressed field-id field-props] :required? true}
            on-esc-props   {:key-code 27 :on-keydown [:elements.text-field/ESC-pressed   field-id field-props] :required? true}]
           {:dispatch-n [(if on-enter   [:x.environment/reg-keypress-event! :elements.text-field/ENTER on-enter-props])
                         (if emptiable? [:x.environment/reg-keypress-event! :elements.text-field/ESC     on-esc-props])]})))

(r/reg-event-fx :elements.text-field/remove-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:emptiable? (boolean)(opt)
  ;  :on-enter (metamorphic-event)(opt)}
  (fn [_ [_ field-id {:keys [emptiable? on-enter]}]]
      {:dispatch-n [(if on-enter   [:x.environment/remove-keypress-event! :elements.text-field/ENTER])
                    (if emptiable? [:x.environment/remove-keypress-event! :elements.text-field/ESC])]}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.text-field/ENTER-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-enter (metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-enter]}]]
      {:dispatch on-enter}))

(r/reg-event-fx :elements.text-field/ESC-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:emptiable? (boolean)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [emptiable?] :as field-props}]]
      (if emptiable? [:elements.text-field/empty-field! field-id field-props])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.text-field/empty-field!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-empty (metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-empty] :as field-props}]]
      (if (plain-field.helpers/field-filled? field-id)
          {:dispatch (if on-empty (r/metamorphic-event<-params on-empty ""))
           :db       (r plain-field.events/empty-field! db field-id field-props)
           :fx       [:elements.plain-field/empty-field! field-id]})))
