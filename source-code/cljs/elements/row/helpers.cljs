
(ns elements.row.helpers
    (:require [elements.element.helpers :as element.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; {:gap (keyword)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;  :stretch-orientation (keyword)(opt)
  ;  :style (map)(opt)
  ;  :vertical-align (keyword)(opt)
  ;  :wrap-items? (boolean)(opt)}
  ;
  ; @return (map)
  ; {:data-column-gap (keyword)
  ;  :data-horizontal-row-align (keyword)
  ;  :data-stretch-orientation (keyword)
  ;  :data-vertical-row-align (keyword)
  ;  :data-wrap-items (boolean)
  ;  :style (map)}
  [row-id {:keys [gap horizontal-align stretch-orientation style vertical-align wrap-items?] :as row-props}]
  (merge (element.helpers/element-indent-attributes row-id row-props)
         {:data-column-gap           gap
          :data-horizontal-row-align horizontal-align
          :data-stretch-orientation  stretch-orientation
          :data-vertical-row-align   vertical-align
          :data-wrap-items           wrap-items?
          :style                     style}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ;
  ; @return (map)
  [row-id row-props]
  (merge (element.helpers/element-default-attributes row-id row-props)
         (element.helpers/element-outdent-attributes row-id row-props)))
