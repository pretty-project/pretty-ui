
(ns pretty-elements.multi-combo-box.utils
    (:require [fruits.keyword.api :as keyword]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-id->group-id
  ; @ignore
  ;
  ; @param (keyword) box-id
  ;
  ; @usage
  ; (box-id->group-id :my-multi-combo-box)
  ; =>
  ; :my-multi-combo-box--chip-group
  ;
  ; @return (keyword)
  [box-id]
  (keyword/append box-id :chip-group "--"))

(defn box-id->field-id
  ; @ignore
  ;
  ; @param (keyword) box-id
  ;
  ; @usage
  ; (box-id->field-id :my-multi-combo-box)
  ; =>
  ; :my-multi-combo-box--text-field
  ;
  ; @return (keyword)
  [box-id]
  (keyword/append box-id :text-field "--"))
