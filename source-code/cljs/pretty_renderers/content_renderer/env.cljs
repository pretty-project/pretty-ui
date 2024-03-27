
(ns pretty-renderers.content-renderer.env
    (:require [transition-controller.api :as transition-controller]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn renderer-mounted?
  ; @description
  ; Returns TRUE if the 'content-renderer' component is mounted.
  ;
  ; @param (keyword) id
  ;
  ; @usage
  ; (renderer-mounted? :my-content-renderer)
  ; =>
  ; true
  ;
  ; @return (boolean)
  [id]
  ; @note (#0018)
  ; The ':mounted?' property of the 'content-renderer' component can be provided as a static property or imported as a dynamic property!
  ; In both cases, the 'controller-mounted?' function is a reliable source (of the mounted state).
  (transition-controller/controller-mounted? id))
 
