
(ns components.vector-items-header.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-body-attributes
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:horizontal-align (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-row-horizontal-align (keyword)
  ;  :data-orientation (keyword)
  ;  :style (map)}
  [_ {:keys [horizontal-align style] :as header-props}]
  (-> {:class                     :c-vector-items-header--body
       :data-orientation          :horizontal
       :data-row-horizontal-align horizontal-align
       :style                     style}
      (pretty-css/indent-attributes header-props)))

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
      (pretty-css/class-attributes   header-props)
      (pretty-css/state-attributes   header-props)
      (pretty-css/outdent-attributes header-props)))
