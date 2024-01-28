
(ns components.compact-list-header.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.layout.api     :as pretty-css.layout]))

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
      (pretty-css.appearance/background-attributes header-props)
      (pretty-css.appearance/border-attributes     header-props)
      (pretty-css.layout/indent-attributes header-props)
      (pretty-css.basic/style-attributes  header-props)))

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
      (pretty-css.basic/class-attributes   header-props)
      (pretty-css.layout/outdent-attributes header-props)
      (pretty-css.basic/state-attributes   header-props)))
