
(ns pretty-elements.expandable.env
    (:require [pretty-elements.surface.env :as surface.env]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-visible?
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {:surface (map)(opt)
  ;   {:visible? (boolean)(opt)}}
  [expandable-id {:keys [surface]}]
  (surface.env/surface-visible? expandable-id surface))
