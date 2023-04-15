
(ns elements.row.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-body-attributes
  ; @ignore
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; {:gap (keyword)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;  :style (map)(opt)
  ;  :vertical-align (keyword)(opt)
  ;  :wrap-items? (boolean)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-column-gap (keyword)
  ;  :data-horizontal-row-align (keyword)
  ;  :data-vertical-row-align (keyword)
  ;  :data-wrap-items (boolean)
  ;  :style (map)}
  [_ {:keys [gap horizontal-align style vertical-align wrap-items?] :as row-props}]
  (-> {:class                     :e-row--body
       :data-column-gap           gap
       :data-horizontal-row-align horizontal-align
       :data-vertical-row-align   vertical-align
       :data-wrap-items           wrap-items?
       :style                     style}
      (pretty-css/border-attributes row-props)
      (pretty-css/color-attributes  row-props)
      (pretty-css/indent-attributes row-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-attributes
  ; @ignore
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ;
  ; @return (map)
  ; {}
  [_ row-props]
  (-> {:class :e-row}
      (pretty-css/default-attributes          row-props)
      (pretty-css/outdent-attributes          row-props)
      (pretty-css/element-max-size-attributes row-props)
      (pretty-css/element-min-size-attributes row-props)
      (pretty-css/element-size-attributes     row-props)))
