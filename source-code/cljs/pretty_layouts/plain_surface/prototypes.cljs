
(ns pretty-layouts.plain-surface.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-props-prototype
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (map)
  ; {:content-orientation (keyword)}
  [_ surface-props]
  (merge {:content-orientation :vertical}
         (-> surface-props)))
; size-unit :screen
