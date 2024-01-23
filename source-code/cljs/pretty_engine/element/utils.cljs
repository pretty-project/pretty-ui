
(ns pretty-engine.element.utils
    (:require [fruits.keyword.api :as keyword]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-id->subitem-id
  ; @ignore
  ;
  ; @param (keyword) element-id
  ;
  ; @usage
  ; (element-id->subitem-id :my-element :my-subitem)
  ; =>
  ; :my-element--my-subitem
  ;
  ; @return (keyword)
  [element-id subitem-id]
  (keyword/append element-id subitem-id "--"))
