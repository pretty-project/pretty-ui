
(ns pretty-engine.input.focus.side-effects
    (:require [pretty-engine.element.focus.side-effects]
              [pretty-engine.input.value.env :as input.value.env]
              [pretty-engine.input.keypress.side-effects :as input.keypress.side-effects]
              [pretty-forms.api :as pretty-forms]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-engine.element.focus.side-effects/*)
(def focus-input!             pretty-engine.element.focus.side-effects/focus-element!)
(def blur-input!              pretty-engine.element.focus.side-effects/blur-element!)
(def mark-input-as-focused!   pretty-engine.element.focus.side-effects/mark-element-as-focused!)
(def unmark-input-as-focused! pretty-engine.element.focus.side-effects/unmark-element-as-focused!)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-focused
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [on-focus-f] :as input-props}]
  (mark-input-as-focused!                                 input-id input-props)
  (input.keypress.side-effects/reg-input-keypress-events! input-id input-props)
  (let [input-displayed-value (input.value.env/get-input-displayed-value input-id input-props)]
       (if on-focus-f (on-focus-f input-displayed-value))))

(defn input-left
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [on-blur-f] :as input-props}]
  (unmark-input-as-focused!                                 input-id input-props)
  (pretty-forms/input-left                                  input-id input-props)
  (input.keypress.side-effects/dereg-input-keypress-events! input-id input-props)
  (let [input-displayed-value (input.value.env/get-input-displayed-value input-id input-props)]
       (if on-blur-f (on-blur-f input-displayed-value))))
