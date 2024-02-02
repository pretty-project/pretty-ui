
(ns pretty-elements.surface.side-effects
    (:require [pretty-elements.engine.api :as pretty-elements.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn show-surface!
  ; @description
  ; Turns on the visibility of a specific 'surface' element.
  ;
  ; @param (keyword) surface-id
  ;
  ; @usage
  ; (show-surface! :my-surface)
  [surface-id]
  (println "show" surface-id)
  (pretty-elements.engine/show-element-surface! surface-id {}))

(defn hide-surface!
  ; @description
  ; Turns off the visibility of a specific 'surface' element.
  ;
  ; @param (keyword) surface-id
  ;
  ; @usage
  ; (hide-surface! :my-surface)
  [surface-id]
  (pretty-elements.engine/hide-element-surface! surface-id {}))
