
(ns components.compact-list-header.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-body-attributes
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ header-props]
  (-> {:class :c-compact-list-header--body}
      (pretty-css/border-attributes header-props)
      (pretty-css/color-attributes  header-props)
      (pretty-css/indent-attributes header-props)
      (pretty-css/style-attributes  header-props)))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-attributes
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {}
  [_ header-props]
  (-> {:class :c-compact-list-header}
      (pretty-css/class-attributes   header-props)
      (pretty-css/outdent-attributes header-props)
      (pretty-css/state-attributes   header-props)))
