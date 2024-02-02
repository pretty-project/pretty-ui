
(ns pretty-elements.surface.env
    (:require [pretty-elements.engine.api :as pretty-elements.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-visible?
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ; {:visible? (boolean)(opt)}
  [surface-id {:keys [visible?]}]
  (pretty-elements.engine/element-surface-visible? surface-id {:surface-visible? visible?}))
