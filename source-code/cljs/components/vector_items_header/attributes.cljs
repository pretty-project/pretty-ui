
(ns components.vector-items-header.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
      (pretty-build-kit/indent-attributes header-props)
      (pretty-build-kit/style-attributes  header-props)))


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
      (pretty-build-kit/class-attributes   header-props)
      (pretty-build-kit/outdent-attributes header-props)
      (pretty-build-kit/state-attributes   header-props)))
