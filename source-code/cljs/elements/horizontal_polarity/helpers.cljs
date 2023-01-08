
(ns elements.horizontal-polarity.helpers
    (:require [elements.element.helpers :as element.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn polarity-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ; {style (map)(opt)
  ;  :vertical-align (keyword)}
  ;
  ; @return (map)
  ; {:data-vertical-align (keyword)
  ;  :style (map)}
  [polarity-id {:keys [style vertical-align] :as polarity-props}]
  (merge (element.helpers/element-indent-attributes polarity-id polarity-props)
         {:data-vertical-align vertical-align
          :style               style}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn polarity-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ;
  ; @return (map)
  [polarity-id polarity-props]
  (merge (element.helpers/element-default-attributes polarity-id polarity-props)
         (element.helpers/element-outdent-attributes polarity-id polarity-props)))
