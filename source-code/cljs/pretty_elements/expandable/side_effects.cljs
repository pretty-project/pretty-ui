
(ns pretty-elements.expandable.side-effects
    (:require [pretty-elements.surface.side-effects :as surface.side-effects]
              [pretty-elements.engine.api :as pretty-elements.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expand-content!
  ; @description
  ; Turns on the content visibility of the 'expandable' element.
  ;
  ; @param (keyword) expandable-id
  ;
  ; @usage
  ; (expand-content! :my-expandable)
  [expandable-id]
  (let [surface-id (pretty-elements.engine/element-id->subitem-id expandable-id :surface)]
       (surface.side-effects/mount-surface! surface-id)))

(defn collapse-content!
  ; @description
  ; Turns off the content visibility of the 'expandable' element.
  ;
  ; @param (keyword) expandable-id
  ;
  ; @usage
  ; (collapse-content! :my-expandable)
  [expandable-id]
  (let [surface-id (pretty-elements.engine/element-id->subitem-id expandable-id :surface)]
       (surface.side-effects/unmount-surface! surface-id)))
