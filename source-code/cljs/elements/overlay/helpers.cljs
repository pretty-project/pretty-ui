
(ns elements.overlay.helpers
    (:require [elements.element.helpers :as element.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn overlay-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) overlay-id
  ; @param (map) overlay-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [overlay-id {:keys [style] :as overlay-props}]
  (merge (element.helpers/element-indent-attributes overlay-id overlay-props)
         {:style style}))

(defn overlay-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) overlay-id
  ; @param (map) overlay-props
  ;
  ; @return (map)
  [overlay-id overlay-props]
  (merge (element.helpers/element-default-attributes overlay-id overlay-props)
         (element.helpers/element-outdent-attributes overlay-id overlay-props)))
