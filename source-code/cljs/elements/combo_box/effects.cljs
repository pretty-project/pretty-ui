
(ns elements.combo-box.effects
    (:require [elements.combo-box.events :as combo-box.events]
              [elements.combo-box.env    :as combo-box.env]
              [elements.plain-field.env  :as plain-field.env]
              [re-frame.api              :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.combo-box/box-did-mount
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:initial-options (vector)(opt)}
  (fn [{:keys [db]} [_ box-id {:keys [initial-options] :as box-props}]]
      (if initial-options {:db (r combo-box.events/box-did-mount db box-id box-props)})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.combo-box/DOWN-pressed
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      {:fx-n [[:elements.plain-field/show-surface!        box-id]
              [:elements.combo-box/highlight-next-option! box-id box-props]]}))

(r/reg-event-fx :elements.combo-box/UP-pressed
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      {:fx-n [[:elements.plain-field/show-surface!        box-id]
              [:elements.combo-box/highlight-prev-option! box-id box-props]]}))

(r/reg-event-fx :elements.combo-box/ESC-pressed
  ; @ignore
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
      ; HACK#1450 (source-code/cljs/elements/combo_box/env.cljs)
      (if (combo-box.env/any-option-rendered? box-id box-props)
          {:fx-n [[:elements.plain-field/hide-surface!             box-id]
                  [:elements.combo-box/discard-option-highlighter! box-id]]}
          [:elements.text-field/ESC-pressed box-id box-props])))

(r/reg-event-fx :elements.combo-box/ENTER-pressed
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:on-type-ended (Re-Frame metamorphic-event)(opt)
  ;  :option-value-f (function)}
  (fn [{:keys [db]} [_ box-id {:keys [on-type-ended option-value-f] :as box-props}]]
      ; XXX#4146 (source-code/cljs/elements/multi_combo_box/effects.cljs)
      ; If the surface of the combo-box is visible ...
      ; ... and any option is highlighted, pressing the ENTER button ...
      ;     ... hides the surface,
      ;     ... discards the highlight on the highlighted option,
      ;     ... stores the highlighted option into the application state,
      ;     ... uses the highlighted option as the field content,
      ;     ... dispatches the :on-type-ended event.
      ; ... and no option is highlighted, pressing the ENTER button ...
      ;     ... hides the surface.
      ;
      ; If the surface of the combo-box isn't visible ...
      ; ... pressing the ENTER button ...
      ;     ... fires the original ENTER event of the text-field.
      (if (plain-field.env/surface-visible? box-id)
          (if-let [highlighted-option (combo-box.env/get-highlighted-option box-id box-props)]
                  {:dispatch [:elements.combo-box/select-option!  box-id box-props highlighted-option]}
                  {:fx       [:elements.plain-field/hide-surface! box-id]})
          [:elements.text-field/ENTER-pressed box-id box-props])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.combo-box/select-option!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:on-type-ended (Re-Frame metamorphic-event)(opt)
  ;  :option-value-f (function)}
  ; @param (*) option
  (fn [{:keys [db]} [_ box-id {:keys [on-type-ended option-value-f] :as box-props} option]]
      ; The :on-type-ended event has to be dispatched even if the user didn't
      ; typed and the option is selected by the pointer or a button event!
      ;
      ; Selecting an option ...
      ; ... hides the surface,
      ; ... discards the highlight on the highlighted option,
      ; ... stores the highlighted option into the application state,
      ; ... uses the highlighted option as field content,
      ; ... dispatches the :on-type-ended event.
      {:db   (r combo-box.events/select-option! db box-id box-props option)
       :fx-n [[:elements.plain-field/hide-surface!             box-id]
              [:elements.combo-box/discard-option-highlighter! box-id]
              [:elements.combo-box/use-selected-option!        box-id box-props option]]
       :dispatch (if on-type-ended (let [option-value (option-value-f option)]
                                        (r/metamorphic-event<-params on-type-ended option-value)))}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.combo-box/field-changed
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      {:fx [:elements.combo-box/discard-option-highlighter! box-id]}))

(r/reg-event-fx :elements.combo-box/field-focused
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      {:fx [:elements.combo-box/reg-keypress-events! box-id box-props]}))

(r/reg-event-fx :elements.combo-box/field-blurred
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      {:fx [:elements.combo-box/remove-keypress-events! box-id box-props]}))
