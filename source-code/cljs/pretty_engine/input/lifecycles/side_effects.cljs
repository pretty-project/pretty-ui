
(ns pretty-engine.input.lifecycles.side-effects
    (:require [pretty-engine.input.focus.side-effects :as input.focus.side-effects]
              [pretty-engine.input.state.side-effects :as input.state.side-effects]
              [pretty-engine.input.value.env          :as input.value.env]
              [pretty-engine.input.value.side-effects :as input.value.side-effects]
              [pretty-forms.api                       :as pretty-forms]
              [time.api                               :as time]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-did-mount
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [autofocus? on-mount-f] :as input-props}]
  ; The autofocus has to be delayed; otherwise, if the input is a field, then the caret
  ; might shown up at the beginning of the content (instead of at its end).
  (letfn [(f0 [] (input.focus.side-effects/focus-input! input-id))]
         (if autofocus? (time/set-timeout! f0 50)))
  (let [provided-get-value-f #(input.value.env/get-input-displayed-value input-id input-props)]
       (pretty-forms/reg-form-input!                        input-id input-props provided-get-value-f)
       (input.value.side-effects/init-input-internal-value! input-id input-props)
       (input.value.side-effects/use-input-initial-value!   input-id input-props))
  (let [input-displayed-value (input.value.env/get-input-displayed-value input-id input-props)]
       (if on-mount-f (on-mount-f input-displayed-value))))

(defn input-will-unmount
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [on-unmount-f] :as input-props}]
  (let [input-displayed-value (input.value.env/get-input-displayed-value input-id input-props)]
       (if on-unmount-f (on-unmount-f input-displayed-value)))
  (pretty-forms/dereg-form-input!              input-id)
  (input.state.side-effects/clear-input-state! input-id))
