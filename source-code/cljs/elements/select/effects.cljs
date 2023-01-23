
(ns elements.select.effects
    (:require [elements.input.events          :as input.events]
              [elements.input.helpers         :as input.helpers]
              [elements.select.config         :as select.config]
              [elements.select.events         :as select.events]
              [elements.select.prototypes     :as select.prototypes]
              [elements.select.views          :as select.views]
              [elements.plain-field.helpers   :as plain-field.helpers]
              [elements.text-field.prototypes :as text-field.prototypes]
              [noop.api                       :refer [return]]
              [re-frame.api                   :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.select/select-button-did-mount
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:initial-options (vector)(opt)
  ;  :initial-value (*)(opt)}
  (fn [{:keys [db]} [_ select-id {:keys [initial-options initial-value] :as select-props}]]
      (if (or initial-options initial-value)
          {:db (r select.events/select-will-mount db select-id select-props)})))

(r/reg-event-fx :elements.select/select-options-did-mount
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [_ [_ select-id select-props]]
      [:elements.select/reg-keypress-events! select-id select-props]))

(r/reg-event-fx :elements.select/select-options-will-unmount
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [_ [_ select-id select-props]]
      [:elements.select/remove-keypress-events! select-id select-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.select/ESC-pressed
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [:x.ui/remove-popup! :elements.select/options])

(r/reg-event-fx :elements.select/ENTER-pressed
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [_ [_ select-id select-props]]
      (if-let [option-field-focused? (input.helpers/input-focused? :elements.select/option-field)]
              [:elements.select/add-option! select-id select-props])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.select/reg-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [{:keys [db]} [_ select-id select-props]]
      (let [on-escape-props {:key-code 27 :required? true :on-keyup [:elements.select/ESC-pressed   select-id select-props]}
            on-enter-props  {:key-code 13 :required? true :on-keyup [:elements.select/ENTER-pressed select-id select-props]}]
           {:dispatch-n [[:x.environment/reg-keypress-event! ::on-ESC-pressed   on-escape-props]
                         [:x.environment/reg-keypress-event! ::on-ENTER-pressed on-enter-props]]})))

(r/reg-event-fx :elements.select/remove-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [{:keys [db]} [_ select-id _]]
      {:dispatch-n [[:x.environment/remove-keypress-event! ::on-ESC-pressed]
                    [:x.environment/remove-keypress-event! ::on-ENTER-pressed]]}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.select/render-options!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [_ [_ select-id select-props]]
      [:x.ui/render-popup! :elements.select/options
                           {:content [select.views/select-options select-id select-props]}]))

(r/reg-event-fx :elements.select/render-select!
  ; @param (keyword)(opt) select-id
  ; @param (map) select-props
  ;
  ; @usage
  ; [:elements.select/render-select! {...}]
  ;
  ; @usage
  ; [:elements.select/render-select! :my-select {...}]
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ select-id select-props]]
      ; The [:elements.select/render-select! ...] event can be used independently
      ; of the elements.api/select component.
      ; Therefore the 'select-props-prototype' and the 'select-will-mount'
      ; functions has to be applied here too.
      (let [select-props (select.prototypes/select-props-prototype select-id select-props)]
           {:db       (r select.events/select-will-mount db select-id select-props)
            :fx       [:elements.input/mark-input-as-visited! select-id]
            :dispatch [:elements.select/render-options!       select-id select-props]})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.select/select-option!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:autoclear? (boolean)(opt)
  ;  :on-select (metamorphic-event)(opt)
  ;  :option-value-f (function)}
  ; @param (*) option
  (fn [{:keys [db]} [_ select-id {:keys [autoclear? option-value-f on-select] :as select-props} option]]
      (let [option-value (option-value-f option)]
           {:db             (r select.events/select-option! db select-id select-props option)
            :dispatch-later [               {:ms select.config/CLOSE-POPUP-DELAY     :dispatch [:x.ui/remove-popup! :elements.select/options]}
                             (if autoclear? {:ms select.config/AUTOCLEAR-VALUE-DELAY :dispatch [:elements.select/clear-value! select-id select-props]})
                             (if on-select  {:ms select.config/ON-SELECT-DELAY       :dispatch (r/metamorphic-event<-params on-select option-value)})]})))

(r/reg-event-fx :elements.select/clear-value!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [{:keys [db]} [_ select-id select-props]]
      {:db (r select.events/clear-value! db select-id select-props)
       :fx [:elements.input/mark-input-as-visited! select-id]}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.select/add-option!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:add-option-f (function)}
  (fn [{:keys [db]} [_ select-id {:keys [add-option-f] :as select-props}]]
      (if-let [option-field-filled? (plain-field.helpers/field-filled? :elements.select/option-field)]
              (let [field-id      :elements.select/option-field
                    field-props   (text-field.prototypes/field-props-prototype field-id {})
                    field-content (plain-field.helpers/get-field-content       field-id)
                    option        (add-option-f field-content)]
                   {:db       (r select.events/add-option! db select-id select-props option)
                    :dispatch [:elements.text-field/empty-field! field-id field-props]}))))
