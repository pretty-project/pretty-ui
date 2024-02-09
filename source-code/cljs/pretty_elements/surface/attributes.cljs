
(ns pretty-elements.surface.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-body-attributes
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [surface-id surface-props]
  (-> {:class :pe-surface--body}
      (pretty-attributes/animation-attributes        surface-props)
      (pretty-attributes/background-color-attributes surface-props)
      (pretty-attributes/border-attributes           surface-props)
      (pretty-attributes/indent-attributes           surface-props)
      (pretty-attributes/size-attributes             surface-props)
      (pretty-attributes/style-attributes            surface-props)))

(defn surface-attributes
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ surface-props]
  (-> {:class :pe-surface}
      (pretty-attributes/class-attributes        surface-props)
      (pretty-attributes/outdent-attributes      surface-props)
      (pretty-attributes/position-attributes     surface-props)
      (pretty-attributes/state-attributes        surface-props)
      (pretty-attributes/theme-attributes        surface-props)
      (pretty-attributes/wrapper-size-attributes surface-props)))
