
(ns components.compact-list-header.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
      (pretty-build-kit/border-attributes header-props)
      (pretty-build-kit/color-attributes  header-props)
      (pretty-build-kit/indent-attributes header-props)
      (pretty-build-kit/style-attributes  header-props)))


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
      (pretty-build-kit/class-attributes   header-props)
      (pretty-build-kit/outdent-attributes header-props)
      (pretty-build-kit/state-attributes   header-props)))
