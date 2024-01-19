
(ns pretty-elements.core.env
    (:require [fruits.vector.api          :as vector]
              [pretty-elements.core.state :as core.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-state
  ; @ignore
  ;
  ; @param (keyword) element-id
  ; @param (list of keyword) keys
  ;
  ; @usage
  ; (get-element-state :my-element)
  ; =>
  ; {:my-key "My value"}
  ;
  ; @usage
  ; (get-element-state :my-element :my-key)
  ; =>
  ; "My value"
  ;
  ; @return (*)
  [element-id & keys]
  (get-in @core.state/ELEMENT-STATE (vector/cons-item keys element-id)))
