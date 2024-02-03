
(ns pretty-elements.expandable.side-effects
    (:require [pretty-elements.surface.side-effects :as surface.side-effects]
              [pretty-elements.expandable.env :as expandable.env]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expand-content!
  ; @description
  ; Turns on the visibility of the content of a specific 'expandable' element.
  ;
  ; @param (keyword) expandable-id
  ;
  ; @usage
  ; (expand-content! :my-expandable)
  [expandable-id]
  (surface.side-effects/show-surface! expandable-id))

(defn collapse-content!
  ; @description
  ; Turns off the visibility of the content of a specific 'expandable' element.
  ;
  ; @param (keyword) expandable-id
  ;
  ; @usage
  ; (collapse-content! :my-expandable)
  [expandable-id]
  (surface.side-effects/hide-surface! expandable-id))
