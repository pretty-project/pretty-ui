
(ns pretty-elements.blank.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ blank-props]
  (-> {:class :pe-blank--inner}
      (pretty-attributes/indent-attributes     blank-props)
      (pretty-attributes/inner-size-attributes blank-props)
      (pretty-attributes/style-attributes      blank-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank-attributes
  ; @ignore
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ blank-props]
  (-> {:class :pe-blank}
      (pretty-attributes/class-attributes          blank-props)
      (pretty-attributes/inner-position-attributes blank-props)
      (pretty-attributes/outdent-attributes        blank-props)
      (pretty-attributes/outer-position-attributes blank-props)
      (pretty-attributes/outer-size-attributes     blank-props)
      (pretty-attributes/state-attributes          blank-props)
      (pretty-attributes/theme-attributes          blank-props)))
