
(ns pretty-engine.element.timeout.env
    (:require [countdown-timer.api :as countdown-timer]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-timeout-left
  ; @param (keyword) element-id
  ; @param (keyword) element-props
  ;
  ; @return (ms)
  [element-id _]
  (countdown-timer/time-left element-id))
