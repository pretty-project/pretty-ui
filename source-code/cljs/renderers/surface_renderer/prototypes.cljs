
(ns renderers.surface-renderer.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn renderer-props-prototype
  ; @ignore
  ;
  ; @param (map) renderer-props
  ;
  ; @return (map)
  ; {:autoreset-scroll-y (boolean)
  ;  :max-elements-rendered (integer)
  ;  :queue-behavior (keyword)
  ;  :rerender-same? (boolean)}
  [renderer-props]
  (merge {:autoreset-scroll-y?   true
          :max-elements-rendered 1
          :queue-behavior        :push
          :rerender-same?        false}
         (param renderer-props)))
