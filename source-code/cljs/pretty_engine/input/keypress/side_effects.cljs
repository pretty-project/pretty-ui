
(ns pretty-engine.input.keypress.side-effects
    (:require [pretty-engine.input.value.env :as input.value.env]
              [pretty-engine.input.value.side-effects :as input.value.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-ENTER-pressed
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [on-enter-f] :as input-props}]
  (let [input-displayed-value (input.value.env/get-input-displayed-value input-id input-props)]
       (if on-enter-f (on-enter-f input-displayed-value))))

(defn input-ESC-pressed
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [emptiable? on-escape-f] :as input-props}]
  (let [input-displayed-value (input.value.env/get-input-displayed-value input-id input-props)]
       (if emptiable?  (input.value.side-effects/empty-input! input-id input-props))
       (if on-escape-f (on-escape-f input-displayed-value))))
