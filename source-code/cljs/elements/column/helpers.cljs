
(ns elements.column.helpers
    (:require [elements.element.helpers :as element.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [column-id {:keys [gap horizontal-align stretch-orientation style vertical-align wrap-items?] :as column-props}]
  (merge (element.helpers/element-indent-attributes column-id column-props)
         {:data-row-gap                 gap
          :data-horizontal-column-align horizontal-align
          :data-stretch-orientation     stretch-orientation
          :data-vertical-column-align   vertical-align
          :data-wrap-items              wrap-items?
          :style                        style}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ;
  ; @return (map)
  [column-id column-props]
  (merge (element.helpers/element-default-attributes column-id column-props)
         (element.helpers/element-outdent-attributes column-id column-props)))
