
(ns pretty-inputs.multi-combo-box.side-effects
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
  ; XXX#4156 (source-code/cljs/pretty_inputs/combo_box/effects.cljs)
  (let [on-down-props  {:key-code 40  :on-keydown-f #(r/dispatch [:pretty-inputs.combo-box/DOWN-pressed        box-id box-props]) :in-type-mode? true :prevent-default? true}
        on-up-props    {:key-code 38  :on-keydown-f #(r/dispatch [:pretty-inputs.combo-box/UP-pressed          box-id box-props]) :in-type-mode? true :prevent-default? true}
        on-esc-props   {:key-code 27  :on-keydown-f #(r/dispatch [:pretty-inputs.combo-box/ESC-pressed         box-id box-props]) :in-type-mode? true}
        on-enter-props {:key-code 13  :on-keydown-f #(r/dispatch [:pretty-inputs.multi-combo-box/ENTER-pressed box-id box-props]) :in-type-mode? true}
        on-comma-props {:key-code 188 :on-keydown-f #(r/dispatch [:pretty-inputs.multi-combo-box/COMMA-pressed box-id box-props]) :in-type-mode? true :prevent-default? true}]
       (keypress-handler/reg-keypress-event! :pretty-inputs.text-field/DOWN   on-down-props)
       (keypress-handler/reg-keypress-event! :pretty-inputs.text-field/UP       on-up-props)
       (keypress-handler/reg-keypress-event! :pretty-inputs.text-field/ESC     on-esc-props)
       (keypress-handler/reg-keypress-event! :pretty-inputs.text-field/ENTER on-enter-props)
       (keypress-handler/reg-keypress-event! :pretty-inputs.text-field/COMMA on-comma-props)))

(defn dereg-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [_ _]
  ; XXX#4156 (source-code/cljs/pretty_inputs/combo_box/effects.cljs)
  (keypress-handler/dereg-keypress-event! :pretty-inputs.text-field/DOWN)
  (keypress-handler/dereg-keypress-event! :pretty-inputs.text-field/UP)
  (keypress-handler/dereg-keypress-event! :pretty-inputs.text-field/ESC)
  (keypress-handler/dereg-keypress-event! :pretty-inputs.text-field/ENTER)
  (keypress-handler/dereg-keypress-event! :pretty-inputs.text-field/COMMA))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @param (keyword) box-id
; @param (map) box-props
(r/reg-fx :pretty-inputs.multi-combo-box/reg-keypress-events! reg-keypress-events!)

; @ignore
;
; @param (keyword) box-id
; @param (map) box-props
(r/reg-fx :pretty-inputs.multi-combo-box/dereg-keypress-events! dereg-keypress-events!)
