
(ns pretty-elements.surface.env
    (:require [transition-controller.api :as transition-controller]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-mounted?
  ; @description
  ; Returns TRUE if the 'surface' element is mounted.
  ;
  ; @param (keyword) surface-id
  ;
  ; @usage
  ; (surface-mounted? :my-surface)
  ; =>
  ; true
  ;
  ; @return (boolean)
  [surface-id]
  ; @note (#0018)
  ; The ':mounted?' property of the 'surface' element can be provided as a static property or imported as a dynamic property!
  ; In both cases, the 'controller-mounted?' function is a reliable source (of the mounted state).
  (transition-controller/controller-mounted? surface-id))
