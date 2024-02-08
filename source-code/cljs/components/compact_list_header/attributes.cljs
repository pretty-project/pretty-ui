
(ns components.compact-list-header.attributes
    (:require [pretty-attributes.api        :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-body-attributes
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ header-props]
  (-> {:class :c-compact-list-header--body}
      (pretty-attributes/background-color-attributes header-props)
      (pretty-attributes/border-attributes           header-props)
      (pretty-attributes/indent-attributes           header-props)
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
      (pretty-attributes/class-attributes   header-props)
      (pretty-attributes/outdent-attributes header-props)
      (pretty-attributes/state-attributes   header-props)
      (pretty-attributes/theme-attributes   header-props)))
