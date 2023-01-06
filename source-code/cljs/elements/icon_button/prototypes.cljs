
(ns elements.icon-button.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:height (keyword)
  ;  :icon-family (keyword)
  ;  :width (keyword)}
  [button-props]
  (merge {:icon-family :material-icons-filled
          :height      :xxl
          :width       :xxl}
         (param button-props)))
