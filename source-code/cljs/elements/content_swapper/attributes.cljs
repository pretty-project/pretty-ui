
(ns elements.content-swapper.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn swapper-page-attributes
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ;
  ; @return (map)
  ; {}
  [_ swapper-props]
  (-> {:class :e-content-swapper--page}
      (pretty-css/element-max-size-attributes swapper-props)
      (pretty-css/element-min-size-attributes swapper-props)))

(defn swapper-body-attributes
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ;
  ; @return (map)
  ; {}
  [_ {:keys [gap style] :as swapper-props}]
  (-> {:class :e-content-swapper--body
       :data-column-gap gap
       :style           style}
      (pretty-css/indent-attributes swapper-props)))
 
(defn swapper-attributes
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ;
  ; @return (map)
  ; {}
  [_ swapper-props]
  (-> {:class :e-content-swapper}
      (pretty-css/default-attributes swapper-props)
      (pretty-css/outdent-attributes swapper-props)))
