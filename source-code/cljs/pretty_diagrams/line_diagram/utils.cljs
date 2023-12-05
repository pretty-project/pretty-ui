
(ns pretty-diagrams.line-diagram.utils
    (:require [math.api :as math]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-props->total-value
  ; @ignore
  ;
  ; @param (map) diagram-props
  ; {:sections (maps in vector)}
  ;
  ; @return (integer)
  [{:keys [sections]}]
  (letfn [(f0 [total-value {:keys [value]}] (+ total-value value))]
         (reduce f0 0 sections)))

(defn section-props->value-ratio
  ; @ignore
  ;
  ; @param (map) diagram-props
  ; {:total-value (integer)}
  ; @param (map) section-props
  ; {:value (integer)}
  ;
  ; @return (integer)
  [{:keys [total-value]} {:keys [value]}]
  (math/percent total-value value))
