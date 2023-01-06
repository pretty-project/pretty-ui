
(ns elements.icon.helpers
    (:require [elements.element.helpers :as element.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ; {:color (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-color (keyword)
  ;  :style (map)}
  [_ {:keys [color style]}]
  (-> {:style style}
      (element.helpers/apply-color :color :data-color color)))

(defn icon-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ; {:icon-family (keyword)
  ;  :size (keyword)}
  ;
  ; @return (map)
  ; {:data-icon-family (keyword)
  ;  :data-icon-size (keyword)
  ;  :data-selectable (boolean)}
  [icon-id {:keys [icon-family size] :as icon-props}]
  (merge (element.helpers/element-indent-attributes icon-id icon-props)
         (icon-style-attributes                     icon-id icon-props)
         {:data-icon-family icon-family
          :data-icon-size   size
          :data-selectable  false}))

(defn icon-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;
  ; @return (map)
  [icon-id icon-props]
  (merge (element.helpers/element-default-attributes icon-id icon-props)
         (element.helpers/element-outdent-attributes icon-id icon-props)))
