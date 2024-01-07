
(ns pretty-inputs.text-field.effects
    (:require [pretty-forms.api]
              [pretty-inputs.plain-field.env    :as plain-field.env]
              [pretty-inputs.plain-field.events :as plain-field.events]
              [re-frame.api                       :as r :refer [r]]
              [pretty-build-kit.api]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.text-field/field-did-mount
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [_ [_ field-id field-props]]
      (let [get-value-f #(plain-field.env/get-field-content field-id)]
           {:fx       [:pretty-forms/reg-form-input!                field-id field-props get-value-f]
            :dispatch [:pretty-inputs.plain-field/field-did-mount field-id field-props]})))

(r/reg-event-fx :pretty-inputs.text-field/field-will-unmount
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [_ [_ field-id field-props]]
      {:fx       [:pretty-forms/dereg-form-input!                 field-id field-props]
       :dispatch [:pretty-inputs.plain-field/field-will-unmount field-id field-props]}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.text-field/ENTER-pressed
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-enter (function or Re-Frame metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-enter]}]]
      (let [field-content (plain-field.env/get-field-content field-id)]
           [:pretty-build-kit/dispatch-event-handler! on-enter field-content])))

(r/reg-event-fx :pretty-inputs.text-field/ESC-pressed
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:emptiable? (boolean)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [emptiable?] :as field-props}]]
      (if emptiable? [:pretty-inputs.text-field/empty-field! field-id field-props])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.text-field/empty-field!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-empty (function or Re-Frame metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-empty] :as field-props}]]
      (if (plain-field.env/field-filled? field-id)
          {:dispatch [:pretty-build-kit/dispatch-event-handler! on-empty ""]
           :db       (r plain-field.events/empty-field! db field-id field-props)
           :fx       [:pretty-inputs.plain-field/empty-field! field-id]})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.text-field/type-ended
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {}
  (fn [_ [_ field-id {:keys [on-type-ended] :as field-props}]]
      (let [field-content (plain-field.env/get-field-content field-id)]
           {:dispatch [:pretty-build-kit/dispatch-event-handler! on-type-ended field-content]
            :fx       [:pretty-forms/input-changed field-id field-props]})))

(r/reg-event-fx :pretty-inputs.text-field/field-blurred
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:validate-when-leave? (boolean)(opt)}
  (fn [_ [_ field-id {:keys [on-blur] :as field-props}]]
      (let [field-content (plain-field.env/get-field-content field-id)]
           {:dispatch-n [[:pretty-build-kit/dispatch-event-handler! on-blur field-content]
                         [:pretty-inputs.plain-field/field-blurred         field-id field-props]]
            :fx-n       [[:pretty-inputs.text-field/dereg-keypress-events! field-id field-props]
                         [:pretty-forms/input-left                           field-id field-props]]})))

(r/reg-event-fx :pretty-inputs.text-field/field-focused
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [_ [_ field-id {:keys [on-focus] :as field-props}]]
      (let [field-content (plain-field.env/get-field-content field-id)]
           {:dispatch-n [[:pretty-build-kit/dispatch-event-handler! on-focus field-content]
                         [:pretty-inputs.plain-field/field-focused       field-id field-props]]
            :fx-n       [[:pretty-inputs.text-field/reg-keypress-events! field-id field-props]]})))
