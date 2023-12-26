
(ns renderers.surface-renderer.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn renderer-props-prototype
  ; @ignore
  ;
  ; @param (map) renderer-props
  ;
  ; @return (map)
  ; {:max-elements-rendered (integer)}
  [renderer-props]
  (merge {:max-elements-rendered 1}
         (-> renderer-props)))
