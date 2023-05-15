
(ns elements.multi-combo-box.side-effects
    (:require [keypress-handler.api :as keypress-handler]
              [re-frame.api         :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  ; XXX#4156 (source-code/cljs/elements/combo_box/effects.cljs)
  (let [on-down-props  {:key-code 40  :on-keydown #(r/dispatch [:elements.combo-box/DOWN-pressed        box-id box-props]) :required? true :prevent-default? true}
        on-up-props    {:key-code 38  :on-keydown #(r/dispatch [:elements.combo-box/UP-pressed          box-id box-props]) :required? true :prevent-default? true}
        on-esc-props   {:key-code 27  :on-keydown #(r/dispatch [:elements.combo-box/ESC-pressed         box-id box-props]) :required? true}
        on-enter-props {:key-code 13  :on-keydown #(r/dispatch [:elements.multi-combo-box/ENTER-pressed box-id box-props]) :required? true}
        on-comma-props {:key-code 188 :on-keydown #(r/dispatch [:elements.multi-combo-box/COMMA-pressed box-id box-props]) :required? true :prevent-default? true}]
       (keypress-handler/reg-keypress-event! :elements.text-field/DOWN   on-down-props)
       (keypress-handler/reg-keypress-event! :elements.text-field/UP       on-up-props)
       (keypress-handler/reg-keypress-event! :elements.text-field/ESC     on-esc-props)
       (keypress-handler/reg-keypress-event! :elements.text-field/ENTER on-enter-props)
       (keypress-handler/reg-keypress-event! :elements.text-field/COMMA on-comma-props)))

(defn remove-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [_ _]
  ; XXX#4156 (source-code/cljs/elements/combo_box/effects.cljs)
  (keypress-handler/remove-keypress-event! :elements.text-field/DOWN)
  (keypress-handler/remove-keypress-event! :elements.text-field/UP)
  (keypress-handler/remove-keypress-event! :elements.text-field/ESC)
  (keypress-handler/remove-keypress-event! :elements.text-field/ENTER)
  (keypress-handler/remove-keypress-event! :elements.text-field/COMMA))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @param (keyword) box-id
; @param (map) box-props
(r/reg-fx :elements.multi-combo-box/reg-keypress-events! reg-keypress-events!)

; @ignore
;
; @param (keyword) box-id
; @param (map) box-props
(r/reg-fx :elements.multi-combo-box/remove-keypress-events! remove-keypress-events!)
