
(ns pretty-engine.element.surface.env
    (:require [pretty-engine.element.state.env :as element.state.env]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-surface-rendered?
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (boolean)
  [element-id _]
  (element.state.env/get-element-state element-id :surface-rendered?))
