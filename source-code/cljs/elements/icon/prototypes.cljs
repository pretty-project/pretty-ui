
(ns elements.icon.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) icon-props
  ;
  ; @return (map)
  ; {:icon-color (keyword)
  ;  :icon-family (keyword)
  ;  :icon-size (keyword)}
  [icon-props]
  (merge {:icon-color  :default
          :icon-family :material-symbols-outlined
          :icon-size   :m}
         (param icon-props)))
