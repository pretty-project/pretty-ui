
(ns elements.icon.helpers
    (:require [pretty-css.api :as pretty-css]))

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
      (pretty-css/apply-color :color :data-color color)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  (merge (pretty-css/indent-attributes  icon-props)
         (icon-style-attributes icon-id icon-props)
         {:data-icon-family icon-family
          :data-icon-size   size
          :data-selectable  false}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;
  ; @return (map)
  [_ icon-props]
  (merge (pretty-css/default-attributes icon-props)
         (pretty-css/outdent-attributes icon-props)))
