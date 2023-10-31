
(ns components.vector-item-controls.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn controls-props-prototype
  ; @ignore
  ;
  ; @param (map) controls-props
  ;
  ; @return (map)
  ; {}
  [controls-props]
  (merge {:tooltip-position :right}
         (-> controls-props)))
