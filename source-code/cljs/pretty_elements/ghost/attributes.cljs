
(ns pretty-elements.ghost.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ ghost-props]
  (-> {:class :pe-ghost--inner}
      (pretty-attributes/animation-attributes        ghost-props)
      (pretty-attributes/background-color-attributes ghost-props)
      (pretty-attributes/border-attributes           ghost-props)
      (pretty-attributes/indent-attributes           ghost-props)
      (pretty-attributes/inner-size-attributes       ghost-props)
      (pretty-attributes/style-attributes            ghost-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-attributes
  ; @ignore
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ ghost-props]
  (-> {:class :pe-ghost}
      (pretty-attributes/class-attributes          ghost-props)
      (pretty-attributes/inner-position-attributes ghost-props)
      (pretty-attributes/outdent-attributes        ghost-props)
      (pretty-attributes/outer-position-attributes ghost-props)
      (pretty-attributes/outer-size-attributes     ghost-props)
      (pretty-attributes/state-attributes          ghost-props)
      (pretty-attributes/theme-attributes          ghost-props)))
