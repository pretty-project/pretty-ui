
(ns elements.vertical-polarity.helpers
    (:require [elements.element.helpers :as element.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn polarity-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ; {:horizontal-align (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-horizontal-align (keyword)
  ;  :style (map)}
  [polarity-id {:keys [horizontal-align style] :as polarity-props}]
  (merge (element.helpers/element-indent-attributes polarity-id polarity-props)
         {:data-horizontal-align horizontal-align
          :style                 style}))

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
