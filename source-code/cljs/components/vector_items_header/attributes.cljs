
(ns components.vector-items-header.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.layout.api :as pretty-css.layout]
              [pretty-css.basic.api :as pretty-css.basic]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-body-attributes
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:horizontal-align (keyword)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-row-horizontal-align (keyword)
  ;  :data-orientation (keyword)}
  [_ {:keys [horizontal-align] :as header-props}]
  (-> {:class                     :c-vector-items-header--body
       :data-orientation          :horizontal
       :data-row-horizontal-align horizontal-align}
      (pretty-css.layout/indent-attributes header-props)
      (pretty-css.basic/style-attributes  header-props)))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-attributes
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {}
  [_ header-props]
  (-> {:class :c-vector-items-header}
      (pretty-css.basic/class-attributes   header-props)
      (pretty-css.layout/outdent-attributes header-props)
      (pretty-css.basic/state-attributes   header-props)))
