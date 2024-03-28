
(ns pretty-elements.expandable.side-effects
    (:require [component-props.api :as component-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expand-content!
  ; @description
  ; Expands the content of the expandable element.
  ;
  ; @param (keyword) id
  ;
  ; @usage
  ; [expandable :my-expandable {...}]
  ; (expand-content! :my-expandable)
  [id]
  (component-props/update-props! id assoc :mounted? true))

(defn collapse-content!
  ; @description
  ; Collapses the content of the expandable element.
  ;
  ; @param (keyword) id
  ;
  ; @usage
  ; [expandable :my-expandable {...}]
  ; (collapse-content! :my-expandable)
  [id]
  (component-props/update-props! id assoc :mounted? false))
