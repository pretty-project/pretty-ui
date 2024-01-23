
(ns pretty-engine.element.focus.env
    (:require [pretty-engine.element.state.env :as element.state.env]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-focused?
  ; @param (map) element-props
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [element-id _]
  (element.state.env/get-element-state element-id :focused?))
