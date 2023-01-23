
(ns layouts.plain-surface.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-props-prototype
  ; @ignore
  ;
  ; @param (map) surface-props
  ; {:content-orientation (keyword)}
  [surface-props]
  (merge {:content-orientation :vertical}
         (param surface-props)))
