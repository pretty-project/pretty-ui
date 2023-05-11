
(ns elements.text-field.side-effects
    (:require [dom.api                   :as dom]
              [elements.text-field.state :as text-field.state]
              [hiccup.api                :as hiccup]
              [keypress-handler.api      :as keypress-handler]
              [re-frame.api              :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn validate-field!
  ; @ignore
  ;
  ; @param (keyword) field-id
  [field-id]
  (let [input-id (hiccup/value field-id "input")]
       (if-let [input (dom/get-element-by-id input-id)]
               (.dir js/console input)))
               ;(.onchange input)
  (swap! text-field.state/VALIDATE-FIELD-CONTENT? assoc field-id true))

(defn init-validator!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:validator (map)(opt)
  ;   {:prevalidate? (boolean)(opt)}}
  [field-id {:keys [validator]}]
  ; It's important to reset the previous state when a field (re)mounts.
  (swap! text-field.state/VALIDATE-FIELD-CONTENT? assoc field-id nil)
  (swap! text-field.state/FIELD-CONTENT-INVALID?  assoc field-id nil)
  (if (:prevalidate? validator)
      (validate-field! field-id)))

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

; @usage
; [:elements.text-field/validate-field! :my-field]
(r/reg-fx :elements.text-field/validate-field! validate-field!)

; @ignore
(r/reg-fx :elements.text-field/init-validator! init-validator!)

; @ignore
(r/reg-fx :elements.text-field/reg-keypress-events! reg-keypress-events!)

; @ignore
(r/reg-fx :elements.text-field/remove-keypress-events! remove-keypress-events!)
