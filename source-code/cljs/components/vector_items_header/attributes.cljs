
(ns components.vector-items-header.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-inner-attributes
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
  (-> {:class                     :c-vector-items-header--inner
       :data-orientation          :horizontal
       :data-row-horizontal-align horizontal-align}
      (pretty-attributes/inner-space-attributes header-props)
      (pretty-attributes/style-attributes  header-props)))


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
      (pretty-attributes/class-attributes  header-props)
      (pretty-attributes/outer-space-attributes header-props)
      (pretty-attributes/state-attributes  header-props)))
