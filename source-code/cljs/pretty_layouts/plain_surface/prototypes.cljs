
(ns pretty-layouts.plain-surface.prototypes
    (:require [pretty-css.api :as pretty-css]))

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
