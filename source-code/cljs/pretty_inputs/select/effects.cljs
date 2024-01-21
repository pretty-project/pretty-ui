
(ns pretty-inputs.select.effects
    (:require [pretty-build-kit.api]
              [pretty-inputs.core.env              :as core.env]
              [pretty-inputs.input.env             :as input.env]
              [pretty-inputs.select.config         :as select.config]
              [pretty-inputs.select.events         :as select.events]
              [pretty-inputs.text-field.prototypes :as text-field.prototypes]
              [re-frame.api                        :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.select/select-button-did-mount
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:initial-options (vector)(opt)
  ;  :initial-value (*)(opt)}
  (fn [{:keys [db]} [_ select-id {:keys [initial-options initial-value] :as select-props}]]
      (if (or initial-options initial-value)
          {:db (r select.events/select-will-mount db select-id select-props)})))

(r/reg-event-fx :pretty-inputs.select/select-options-did-mount
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [_ [_ select-id select-props]]
      {:fx [:pretty-inputs.select/reg-keypress-events! select-id select-props]}))

(r/reg-event-fx :pretty-inputs.select/select-options-will-unmount
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [_ [_ select-id select-props]]
      {:fx [:pretty-inputs.select/dereg-keypress-events! select-id select-props]}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.select/ESC-pressed
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [_ [_ select-id select-props]]
      {:fx [:pretty-inputs.select/close-options! select-id select-props]}))

(r/reg-event-fx :pretty-inputs.select/ENTER-pressed
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [_ [_ select-id select-props]]
      (if-let [option-field-focused? (input.env/input-focused? :pretty-inputs.select/option-field)]
              [:pretty-inputs.select/add-option! select-id select-props])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.select/select-option!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:on-select (function or Re-Frame metamorphic-event)(opt)
  ;  :option-value-f (function)}
  ; @param (*) option
  (fn [{:keys [db]} [_ select-id {:keys [on-select option-value-f] :as select-props} option]]
      (let [option-value (option-value-f option)]
           {:db             (r select.events/select-option! db select-id select-props option)
            :dispatch-later [               {:ms select.config/CLOSE-POPUP-DELAY     :fx       [:pretty-inputs.input/close-popup!  select-id select-props]}
                             (if on-select  {:ms select.config/ON-SELECT-DELAY       :dispatch [:pretty-build-kit/dispatch-event-handler! on-select option-value]})]})))

(r/reg-event-fx :pretty-inputs.select/clear-value!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [{:keys [db]} [_ select-id select-props]]
      {:db (r select.events/clear-value! db select-id select-props)
       :fx [:pretty-inputs.input/mark-input-as-visited! select-id]}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.select/add-option!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:add-option-f (function)}
  (fn [{:keys [db]} [_ select-id {:keys [add-option-f] :as select-props}]]
      (if-let [option-field-filled? (core.env/input-not-empty? :pretty-inputs.select/option-field {})]
              (let [field-id      :pretty-inputs.select/option-field
                    field-props   (text-field.prototypes/field-props-prototype field-id {})
                    field-content (core.env/get-input-displayed-value          field-id field-props)
                    option        (add-option-f field-content)]
                   {:db       (r select.events/add-option! db select-id select-props option)
                    :dispatch [:pretty-inputs.text-field/empty-field! field-id field-props]}))))
