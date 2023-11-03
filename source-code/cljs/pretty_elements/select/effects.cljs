
(ns pretty-elements.select.effects
    (:require [pretty-elements.input.env             :as input.env]
              [pretty-elements.select.config         :as select.config]
              [pretty-elements.select.events         :as select.events]
              [pretty-elements.plain-field.env       :as plain-field.env]
              [pretty-elements.text-field.prototypes :as text-field.prototypes]
              [re-frame.api                   :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-elements.select/select-button-did-mount
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:initial-options (vector)(opt)
  ;  :initial-value (*)(opt)}
  (fn [{:keys [db]} [_ select-id {:keys [initial-options initial-value] :as select-props}]]
      (if (or initial-options initial-value)
          {:db (r select.events/select-will-mount db select-id select-props)})))

(r/reg-event-fx :pretty-elements.select/select-options-did-mount
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [_ [_ select-id select-props]]
      {:fx [:pretty-elements.select/reg-keypress-events! select-id select-props]}))

(r/reg-event-fx :pretty-elements.select/select-options-will-unmount
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [_ [_ select-id select-props]]
      {:fx [:pretty-elements.select/remove-keypress-events! select-id select-props]}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-elements.select/ESC-pressed
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [_ [_ select-id select-props]]
      {:fx [:pretty-elements.select/close-options! select-id select-props]}))

(r/reg-event-fx :pretty-elements.select/ENTER-pressed
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [_ [_ select-id select-props]]
      (if-let [option-field-focused? (input.env/input-focused? :pretty-elements.select/option-field)]
              [:pretty-elements.select/add-option! select-id select-props])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-elements.select/select-option!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:autoclear? (boolean)(opt)
  ;  :on-select (Re-Frame metamorphic-event)(opt)
  ;  :option-value-f (function)}
  ; @param (*) option
  (fn [{:keys [db]} [_ select-id {:keys [autoclear? on-select option-value-f] :as select-props} option]]
      (let [option-value (option-value-f option)]
           {:db             (r select.events/select-option! db select-id select-props option)
            :dispatch-later [               {:ms select.config/CLOSE-POPUP-DELAY     :fx       [:pretty-elements.input/close-popup!  select-id select-props]}
                             (if autoclear? {:ms select.config/AUTOCLEAR-VALUE-DELAY :dispatch [:pretty-elements.select/clear-value! select-id select-props]})
                             (if on-select  {:ms select.config/ON-SELECT-DELAY       :dispatch (r/metamorphic-event<-params on-select option-value)})]})))

(r/reg-event-fx :pretty-elements.select/clear-value!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [{:keys [db]} [_ select-id select-props]]
      {:db (r select.events/clear-value! db select-id select-props)
       :fx [:pretty-elements.input/mark-input-as-visited! select-id]}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-elements.select/add-option!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:add-option-f (function)}
  (fn [{:keys [db]} [_ select-id {:keys [add-option-f] :as select-props}]]
      (if-let [option-field-filled? (plain-field.env/field-filled? :pretty-elements.select/option-field)]
              (let [field-id      :pretty-elements.select/option-field
                    field-props   (text-field.prototypes/field-props-prototype field-id {})
                    field-content (plain-field.env/get-field-content           field-id)
                    option        (add-option-f field-content)]
                   {:db       (r select.events/add-option! db select-id select-props option)
                    :dispatch [:pretty-elements.text-field/empty-field! field-id field-props]}))))
