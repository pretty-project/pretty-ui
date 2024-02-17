
(ns pretty-elements.expandable.side-effects
    (:require [dynamic-props.api :as dynamic-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expand-content!
  ; @description
  ; Expands the content of the expandable element.
  ;
  ; @param (keyword) expandable-id
  ;
  ; @usage
  ; [expandable :my-expandable {...}]
  ; (expand-content! :my-expandable)
  [expandable-id]
  (dynamic-props/update-props! expandable-id assoc :expanded? true))

(defn collapse-content!
  ; @description
  ; Collapses the content of the expandable element.
  ;
  ; @param (keyword) expandable-id
  ;
  ; @usage
  ; [expandable :my-expandable {...}]
  ; (collapse-content! :my-expandable)
  [expandable-id]
  (dynamic-props/update-props! expandable-id assoc :expanded? false))
