
(ns pretty-elements.text-field.side-effects
    (:require [keypress-handler.api :as keypress-handler]
              [re-frame.api         :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:emptiable? (boolean)(opt)
  ;  :on-enter (Re-Frame metamorphic-event)(opt)}
  [field-id {:keys [emptiable? on-enter] :as field-props}]
  (let [on-esc-props   {:key-code 27 :on-keydown #(r/dispatch [:pretty-elements.text-field/ESC-pressed   field-id field-props]) :required? true}
        on-enter-props {:key-code 13 :on-keydown #(r/dispatch [:pretty-elements.text-field/ENTER-pressed field-id field-props]) :required? true}]
       (if emptiable? (keypress-handler/reg-keypress-event! :pretty-elements.text-field/ESC     on-esc-props))
       (if on-enter   (keypress-handler/reg-keypress-event! :pretty-elements.text-field/ENTER on-enter-props))))

(defn remove-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:emptiable? (boolean)(opt)
  ;  :on-enter (Re-Frame metamorphic-event)(opt)}
  [_ {:keys [emptiable? on-enter]}]
  (if emptiable? (keypress-handler/remove-keypress-event! :pretty-elements.text-field/ESC))
  (if on-enter   (keypress-handler/remove-keypress-event! :pretty-elements.text-field/ENTER)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @param (keyword) field-id
; @param (map) field-props
(r/reg-f :pretty-elements.text-field/reg-keypress-events! reg-keypress-events!)

; @ignore
;
; @param (keyword) field-id
; @param (map) field-props
(r/reg-f :pretty-elements.text-field/remove-keypress-events! remove-keypress-events!)
