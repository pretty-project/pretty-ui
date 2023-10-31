
(ns layouts.plain-surface.prototypes)

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
