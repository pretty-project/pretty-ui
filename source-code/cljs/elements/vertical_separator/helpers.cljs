
(ns elements.vertical-separator.helpers
    (:require [elements.element.helpers :as element.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ; {:style (map)(opt)
  ;  :width (keyword)}
  ;
  ; @return (map)
  ; {:data-block-width (keyword)
  ;  :style (map)}
  [separator-id {:keys [style width] :as separator-props}]
  (merge (element.helpers/element-default-attributes separator-id separator-props)
         {:data-block-width width
          :style            style}))
