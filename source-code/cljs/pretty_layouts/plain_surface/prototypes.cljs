
(ns pretty-layouts.plain-surface.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-props-prototype
  ; @ignore
  ;
  ; @param (map) surface-props
  ; {:content-orientation (keyword)}
  [surface-props]
  (merge {:content-orientation :vertical}
         (-> surface-props)))
