
(ns elements.column.helpers
    (:require [pretty-css.api :as pretty-css]))

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
  [_ {:keys [gap horizontal-align stretch-orientation style vertical-align wrap-items?] :as column-props}]
  (merge (pretty-css/indent-attributes column-props)
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
  [_ column-props]
  (merge (pretty-css/default-attributes column-props)
         (pretty-css/outdent-attributes column-props)))
