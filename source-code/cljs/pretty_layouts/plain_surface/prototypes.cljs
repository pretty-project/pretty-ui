
(ns pretty-layouts.plain-surface.prototypes
    (:require [pretty-rules.api :as pretty-rules]
              [pretty-properties.api :as pretty-properties]))

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
; ;(pretty-rules/auto-disable-highlight-color)) <- stay commented
; ;(pretty-rules/auto-disable-hover-color)) <- stay commented
;(pretty-rules/auto-align-scrollable-flex) ???
