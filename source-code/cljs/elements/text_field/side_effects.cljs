
(ns elements.text-field.side-effects
    (:require [dom.api                   :as dom]
              [elements.plain-field.env  :as plain-field.env]
              [elements.text-field.state :as text-field.state]
              [elements.text-field.utils :as text-field.utils]
              [hiccup.api                :as hiccup]
              [keypress-handler.api      :as keypress-handler]
              [re-frame.api              :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn validate-field!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) validation-props
  ; {:on-invalid (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content and the invalid message as its last parameter.
  ;  :on-valid (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter.
  ;  :validators (maps in vector)
  ;   [{:f (function)
  ;     :invalid-message (metamorphic-content)}]}
  [field-id validation-props]
  ; When the field gets validated by a side-effect or the init-validator! function,
  ; the field validation gets turned on.
  (swap! text-field.state/VALIDATE-FIELD-CONTENT? assoc field-id true)
  ; XXX#0612
  ; The field content can be validated automatically when the user ends typing
  ; or by a side-effect (e.g. the user clicks on a submit button).
  (let [field-content (plain-field.env/get-field-content field-id)]
       (text-field.utils/field-content-validator-f field-id validation-props field-content)))

(defn init-validator!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:prevalidate? (boolean)(opt)}
  [field-id {:keys [prevalidate?] :as field-props}]
  ; It's important to reset the previous state when a field (re)mounts.
  (swap! text-field.state/VALIDATE-FIELD-CONTENT?       dissoc field-id)
  (swap! text-field.state/FIELD-CONTENT-INVALID?        dissoc field-id)
  (swap! text-field.state/FIELD-CONTENT-INVALID-MESSAGE dissoc field-id)
  (if prevalidate? (validate-field! field-id field-props)))

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
  (let [on-esc-props   {:key-code 27 :on-keydown #(r/dispatch [:elements.text-field/ESC-pressed   field-id field-props]) :required? true}
        on-enter-props {:key-code 13 :on-keydown #(r/dispatch [:elements.text-field/ENTER-pressed field-id field-props]) :required? true}]
       (if emptiable? (keypress-handler/reg-keypress-event! :elements.text-field/ESC     on-esc-props))
       (if on-enter   (keypress-handler/reg-keypress-event! :elements.text-field/ENTER on-enter-props))))

(defn remove-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:emptiable? (boolean)(opt)
  ;  :on-enter (Re-Frame metamorphic-event)(opt)}
  [_ {:keys [emptiable? on-enter]}]
  (if emptiable? (keypress-handler/remove-keypress-event! :elements.text-field/ESC))
  (if on-enter   (keypress-handler/remove-keypress-event! :elements.text-field/ENTER)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) field-id
; @param (map) validator
; {:on-invalid (Re-Frame metamorphic-event)(opt)
;  :on-valid (Re-Frame metamorphic-event)(opt)
;  :validator-f (function)}
;
; @usage
; [:elements.text-field/validate-field! :my-field {...}]
(r/reg-fx :elements.text-field/validate-field! validate-field!)

; @ignore
;
; @param (keyword) field-id
; @param (map) field-props
(r/reg-fx :elements.text-field/init-validator! init-validator!)

; @ignore
;
; @param (keyword) field-id
; @param (map) field-props
(r/reg-fx :elements.text-field/reg-keypress-events! reg-keypress-events!)

; @ignore
;
; @param (keyword) field-id
; @param (map) field-props
(r/reg-fx :elements.text-field/remove-keypress-events! remove-keypress-events!)
