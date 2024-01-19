
(ns pretty-inputs.text-field.effects
    (:require [pretty-build-kit.api]
              [pretty-forms.api]
              [pretty-inputs.core.env           :as core.env]
              [pretty-inputs.plain-field.events :as plain-field.events]
              [re-frame.api                     :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.text-field/ENTER-pressed
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-enter (function or Re-Frame metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-enter] :as field-props}]]
      (let [field-content (core.env/get-input-displayed-value field-id field-props)]
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
      (if (core.env/input-not-empty? field-id field-props)
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
      (let [field-content (core.env/get-input-displayed-value field-id field-props)]
           {:dispatch [:pretty-build-kit/dispatch-event-handler! on-type-ended field-content]
            :fx       [:pretty-forms/input-changed field-id field-props]})))
