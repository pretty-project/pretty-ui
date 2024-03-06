
(ns components.compact-list-header.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-inner-attributes
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ header-props]
  (-> {:class :c-compact-list-header--inner}
      (pretty-attributes/background-color-attributes header-props)
      (pretty-attributes/border-attributes           header-props)
      (pretty-attributes/inner-space-attributes      header-props)
      (pretty-attributes/style-attributes            header-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-attributes
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ header-props]
  (-> {:class :c-compact-list-header}
      (pretty-attributes/class-attributes       header-props)
      (pretty-attributes/outer-space-attributes header-props)
      (pretty-attributes/state-attributes       header-props)
      (pretty-attributes/theme-attributes       header-props)))
