
(ns elements.combo-box.effects
    (:require [elements.combo-box.events   :as combo-box.events]
              [elements.combo-box.helpers  :as combo-box.helpers]
              [elements.text-field.helpers :as text-field.helpers]
              [re-frame.api                :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.combo-box/reg-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      ; XXX#4156
      ; Overwrites the default ESC and ENTER keypress events of the field by using
      ; the :elements.text-field/ESC and :elements.text-field/ENTER keypress event IDs.
      ; The overwritten keypress events' functionality is implemented in the combo-box
      ; field keypress events.
      ;
      ; The UP and DOWN keypress events has similar names (for keeping consistency).
      (let [on-down-props  {:key-code 40 :on-keydown [:elements.combo-box/DOWN-pressed  box-id box-props] :required? true :prevent-default? true}
            on-up-props    {:key-code 38 :on-keydown [:elements.combo-box/UP-pressed    box-id box-props] :required? true :prevent-default? true}
            on-esc-props   {:key-code 27 :on-keydown [:elements.combo-box/ESC-pressed   box-id box-props] :required? true}
            on-enter-props {:key-code 13 :on-keydown [:elements.combo-box/ENTER-pressed box-id box-props] :required? true}]
           {:dispatch-n [[:x.environment/reg-keypress-event! :elements.text-field/DOWN   on-down-props]
                         [:x.environment/reg-keypress-event! :elements.text-field/UP       on-up-props]
                         [:x.environment/reg-keypress-event! :elements.text-field/ESC     on-esc-props]
                         [:x.environment/reg-keypress-event! :elements.text-field/ENTER on-enter-props]]})))

(r/reg-event-fx :elements.combo-box/remove-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; XXX#4156
  {:dispatch-n [[:x.environment/remove-keypress-event! :elements.text-field/DOWN]
                [:x.environment/remove-keypress-event! :elements.text-field/UP]
                [:x.environment/remove-keypress-event! :elements.text-field/ESC]
                [:x.environment/remove-keypress-event! :elements.text-field/ENTER]]})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.combo-box/DOWN-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      {:fx-n [[:elements.text-field/show-surface!         box-id]
              [:elements.combo-box/highlight-next-option! box-id box-props]]}))

(r/reg-event-fx :elements.combo-box/UP-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      {:fx-n [[:elements.text-field/show-surface!         box-id]
              [:elements.combo-box/highlight-prev-option! box-id box-props]]}))

(r/reg-event-fx :elements.combo-box/ESC-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      ; If the surface of the combo-box ...
      ; ... displays options, pressing the ESC button:
      ;     - hides the surface.
      ;     - discards the highlight on the highlighted option.
      ; ... doesn't display any options, pressing the ESC button:
      ;     - fires the original ESC event of the text-field.
      ;
      ; HACK#1450 (source-code/cljs/elements/combo_box/helpers.cljs)
      (if (combo-box.helpers/any-option-rendered? box-id box-props)
          {:fx-n [[:elements.text-field/hide-surface!              box-id]
                  [:elements.combo-box/discard-option-highlighter! box-id]]}
          [:elements.text-field/ESC-pressed box-id box-props])))

(r/reg-event-fx :elements.combo-box/ENTER-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:on-type-ended (metamorphic-event)(opt)
  ;  :option-value-f (function)}
  (fn [{:keys [db]} [_ box-id {:keys [on-type-ended option-value-f] :as box-props}]]
      ; XXX#4146 (source-code/cljs/elements/multi_combo_box/effects.cljs)
      ; If the surface of the combo-box is visible ...
      ; ... and an option is highlighted, pressing the ENTER button:
      ;     - hides the surface.
      ;     - discards the highlight on the highlighted option.
      ;     - stores the highlighted option into the application state.
      ;     - uses the highlighted option as the field content.
      ;     - dispatches the :on-type-ended event.
      ; ... and no option is highlight, pressing the ENTER button:
      ;     - hides the surface.
      ;
      ; If the surface of the combo-box isn't visible ...
      ; ... pressing the ENTER button:
      ;     - fires the original ENTER event of the text-field.
      (if (text-field.helpers/surface-visible? box-id)
          (if-let [highlighted-option (combo-box.helpers/get-highlighted-option box-id box-props)]
                  {:dispatch [:elements.combo-box/select-option! box-id box-props highlighted-option]}
                  {:fx       [:elements.text-field/hide-surface! box-id]})
          [:elements.text-field/ENTER-pressed box-id box-props])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.combo-box/select-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:on-type-ended (metamorphic-event)(opt)
  ;  :option-value-f (function)}
  ; @param (*) option
  (fn [{:keys [db]} [_ box-id {:keys [on-type-ended option-value-f] :as box-props} option]]
      ; The :on-type-ended event has to be dispatched even if the user didn't
      ; typed and the option is selected by the pointer or a button event!
      ;
      ; Selecting an option:
      ; - hides the surface.
      ; - discards the highlight on the highlighted option.
      ; - stores the highlighted option into the application state.
      ; - uses the highlighted option as the field content.
      ; - dispatches the :on-type-ended event.
      {:db   (r combo-box.events/select-option! db box-id box-props option)
       :fx-n [[:elements.text-field/hide-surface!              box-id]
              [:elements.combo-box/discard-option-highlighter! box-id]
              [:elements.combo-box/use-selected-option!        box-id box-props option]]
       :dispatch (if on-type-ended (let [option-value (option-value-f option)]
                                        (r/metamorphic-event<-params on-type-ended option-value)))}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.combo-box/field-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      {:fx [:elements.combo-box/discard-option-highlighter! box-id]}))

(r/reg-event-fx :elements.combo-box/field-focused
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      [:elements.combo-box/reg-keypress-events! box-id box-props]))

(r/reg-event-fx :elements.combo-box/field-blurred
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      [:elements.combo-box/remove-keypress-events! box-id box-props]))
