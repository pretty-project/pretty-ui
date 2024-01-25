
(ns pretty-engine.element.state.env
    (:require [fruits.vector.api        :as vector]
              [pretty-engine.core.state :as core.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-state
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
  (get-in @core.state/STATE (vector/cons-item keys element-id)))
