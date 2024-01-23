
(ns pretty-engine.input.options.side-effects
    (:require [fruits.mixed.api :as mixed]
              [fruits.vector.api :as vector]
              [pretty-engine.input.options.env :as input.options.env]
              [pretty-engine.input.value.env :as input.value.env]
              [pretty-engine.input.value.side-effects :as input.value.side-effects]
              [pretty-engine.input.popup.config :as input.popup.config]
              [pretty-engine.input.popup.side-effects :as input.popup.side-effects]
              [time.api :as time]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn pick-input-option!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ; @param (*) option
  [input-id {:keys [on-selected-f on-unselected-f option-value-f] :as input-props} option]
  (letfn [(f0 [] (input.popup.side-effects/close-input-popup! input-id input-props))]
         ; If the input displays its options on a popup element, and only one option can be selected at a time,
         ; it always closes the popup after the user selected an option.
         (time/set-timeout! f0 input.popup.config/CLOSE-INPUT-POPUP-AFTER))
  (let [option-value (option-value-f option)]
       (cond (input.options.env/input-option-picked? input-id input-props option)
             (let [input-updated-value (-> nil)]
                  (input.value.side-effects/input-value-changed input-id input-props input-updated-value)
                  (if on-unselected-f (on-unselected-f input-updated-value)))
             (input.options.env/max-input-selection-not-reached? input-id input-props)
             (let [input-updated-value (-> option-value)]
                  (input.value.side-effects/input-value-changed input-id input-props input-updated-value)
                  (if on-selected-f (on-selected-f input-updated-value))))))

(defn toggle-input-option!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ; @param (*) option
  [input-id {:keys [on-selected-f on-unselected-f option-value-f] :as input-props} option]
  (let [option-value          (option-value-f option)
        input-displayed-value (input.value.env/get-input-displayed-value input-id input-props)]
       (cond (input.options.env/input-option-toggled? input-id input-props option)
             (let [input-updated-value (-> input-displayed-value mixed/to-vector (vector/remove-item option-value))]
                  (input.value.side-effects/input-value-changed input-id input-props input-updated-value)
                  (if on-unselected-f (on-unselected-f input-updated-value)))
             (input.options.env/max-input-selection-not-reached? input-id input-props)
             (let [input-updated-value (-> input-displayed-value mixed/to-vector (vector/conj-item option-value))]
                  (input.value.side-effects/input-value-changed input-id input-props input-updated-value)
                  (if on-selected-f (on-selected-f input-updated-value))))))

(defn select-input-option!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; @param (*) option
  [input-id input-props option]
  (if (input.options.env/multiple-input-option-selectable? input-id input-props)
      (toggle-input-option!                                input-id input-props option)
      (pick-input-option!                                  input-id input-props option)))
