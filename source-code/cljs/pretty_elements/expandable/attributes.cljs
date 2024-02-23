
(ns pretty-elements.expandable.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-content-attributes
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ expandable-props]
  (-> {:class :pe-expandable--content}
      (pretty-attributes/font-attributes expandable-props)
      (pretty-attributes/text-attributes expandable-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ expandable-props]
  (-> {:class :pe-expandable--inner}
      (pretty-attributes/background-color-attributes expandable-props)
      (pretty-attributes/border-attributes           expandable-props)
      (pretty-attributes/indent-attributes           expandable-props)
      (pretty-attributes/inner-size-attributes       expandable-props)
      (pretty-attributes/style-attributes            expandable-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-attributes
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ expandable-props]
  (-> {:class :pe-expandable}
      (pretty-attributes/class-attributes          expandable-props)
      (pretty-attributes/inner-position-attributes expandable-props)
      (pretty-attributes/outdent-attributes        expandable-props)
      (pretty-attributes/outer-position-attributes expandable-props)
      (pretty-attributes/outer-size-attributes     expandable-props)
      (pretty-attributes/state-attributes          expandable-props)
      (pretty-attributes/theme-attributes          expandable-props)))
