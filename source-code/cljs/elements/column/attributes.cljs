
(ns elements.column.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-body-attributes
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [gap horizontal-align style vertical-align wrap-items?] :as column-props}]
  (-> {:class                        :e-column--body
       :data-row-gap                 gap
       :data-horizontal-column-align horizontal-align
       :data-vertical-column-align   vertical-align
       :data-wrap-items              wrap-items?
       :style                        style}
      (pretty-css/border-attributes column-props)
      (pretty-css/color-attributes  column-props)
      (pretty-css/indent-attributes column-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-attributes
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ;
  ; @return (map)
  ; {}
  [_ column-props]
  (-> {:class :e-column}
      (pretty-css/default-attributes          column-props)
      (pretty-css/outdent-attributes          column-props)
      (pretty-css/element-max-size-attributes column-props)
      (pretty-css/element-min-size-attributes column-props)
      (pretty-css/element-size-attributes     column-props)))
