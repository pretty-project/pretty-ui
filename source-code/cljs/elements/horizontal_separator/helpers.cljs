
(ns elements.horizontal-separator.helpers
    (:require [elements.element.helpers :as element.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ; {:height (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-block-height (keyword)
  ;  :style (map)}
  [separator-id {:keys [height style] :as separator-props}]
  (merge (element.helpers/element-default-attributes separator-id separator-props)
         {:data-block-height height
          :style             style}))
