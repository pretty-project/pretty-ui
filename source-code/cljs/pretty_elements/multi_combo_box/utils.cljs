
(ns pretty-elements.multi-combo-box.utils
    (:require [keyword.api :as keyword]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-id->group-id
  ; @ignore
  ;
  ; @param (keyword) box-id
  ;
  ; @example
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
  ; @example
  ; (box-id->field-id :my-multi-combo-box)
  ; =>
  ; :my-multi-combo-box--text-field
  ;
  ; @return (keyword)
  [box-id]
  (keyword/append box-id :text-field "--"))
