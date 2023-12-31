
(ns pretty-elements.text-field.effects
    (:require [pretty-elements.plain-field.env    :as plain-field.env]
              [pretty-elements.plain-field.events :as plain-field.events]
              [re-frame.api                       :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-elements.text-field/field-did-mount
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [_ [_ field-id field-props]]
      (let [get-value-f #(plain-field.env/get-field-content field-id)]
           {:fx       [:pretty-elements.form/reg-form-input!        field-id field-props get-value-f]
            :dispatch [:pretty-elements.plain-field/field-did-mount field-id field-props]})))

(r/reg-event-fx :pretty-elements.text-field/field-will-unmount
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [_ [_ field-id field-props]]
      {:fx       [:pretty-elements.form/remove-form-input!        field-id field-props]
       :dispatch [:pretty-elements.plain-field/field-will-unmount field-id field-props]}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-elements.text-field/ENTER-pressed
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-enter (function or Re-Frame metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-enter]}]]
      (let [field-content (plain-field.env/get-field-content field-id)]
           [:pretty-build-kit/dispatch-event-handler! on-enter field-content])))

(r/reg-event-fx :pretty-elements.text-field/ESC-pressed
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:emptiable? (boolean)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [emptiable?] :as field-props}]]
      (if emptiable? [:pretty-elements.text-field/empty-field! field-id field-props])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-elements.text-field/empty-field!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-empty (function or Re-Frame metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-empty] :as field-props}]]
      (if (plain-field.env/field-filled? field-id)
          {:dispatch [:pretty-build-kit/dispatch-event-handler! on-empty ""]
           :db       (r plain-field.events/empty-field! db field-id field-props)
           :fx       [:pretty-elements.plain-field/empty-field! field-id]})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-elements.text-field/type-ended
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {}
  (fn [_ [_ field-id {:keys [on-type-ended validate-when-change?] :as field-props}]]
      (let [field-content (plain-field.env/get-field-content field-id)]
           {:dispatch-n [[:pretty-build-kit/dispatch-event-handler! on-type-ended field-content]]
            :fx-n       [(if validate-when-change? [:pretty-elements.form/validate-input! field-id field-props])]})))

(r/reg-event-fx :pretty-elements.text-field/field-blurred
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:validate-when-leave? (boolean)(opt)}
  (fn [_ [_ field-id {:keys [on-blur validate-when-leave?] :as field-props}]]
      (let [field-content (plain-field.env/get-field-content field-id)]
           {:dispatch-n [[:pretty-build-kit/dispatch-event-handler! on-blur field-content]
                         [:pretty-elements.plain-field/field-blurred          field-id field-props]]
            :fx-n       [[:pretty-elements.text-field/remove-keypress-events! field-id field-props]
                         (if validate-when-leave? [:pretty-elements.form/validate-input! field-id field-props])]})))

(r/reg-event-fx :pretty-elements.text-field/field-focused
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [_ [_ field-id {:keys [on-focus] :as field-props}]]
      (let [field-content (plain-field.env/get-field-content field-id)]
           {:dispatch-n [[:pretty-build-kit/dispatch-event-handler! on-focus field-content]
                         [:pretty-elements.plain-field/field-focused       field-id field-props]]
            :fx-n       [[:pretty-elements.text-field/reg-keypress-events! field-id field-props]]})))
