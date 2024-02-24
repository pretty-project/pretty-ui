
(ns pretty-elements.vertical-spacer.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn spacer-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) spacer-id
  ; @param (map) spacer-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ spacer-props]
  (-> {:class :pe-vertical-spacer--inner}
      (pretty-attributes/inner-size-attributes  spacer-props)
      (pretty-attributes/inner-space-attributes spacer-props)
      (pretty-attributes/style-attributes       spacer-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn spacer-attributes
  ; @ignore
  ;
  ; @param (keyword) spacer-id
  ; @param (map) spacer-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ spacer-props]
  (-> {:class :pe-vertical-spacer}
      (pretty-attributes/class-attributes          spacer-props)
      (pretty-attributes/inner-position-attributes spacer-props)
      (pretty-attributes/outer-position-attributes spacer-props)
      (pretty-attributes/outer-size-attributes     spacer-props)
      (pretty-attributes/outer-space-attributes    spacer-props)
      (pretty-attributes/state-attributes          spacer-props)
      (pretty-attributes/theme-attributes          spacer-props)))
