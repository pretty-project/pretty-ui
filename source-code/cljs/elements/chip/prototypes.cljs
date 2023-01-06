
(ns elements.chip.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) chip-props
  ; {:icon (keyword)(opt)}
  ;
  ; @return (map)
  ; {:color (keyword or string)
  ;  :fill-color (keyword or string)
  ;  :icon-family (keyword)
  ;  :primary-button-icon (keyword)}
  [{:keys [icon] :as chip-props}]
  (merge {:color               :default
          :fill-color          :primary
          :primary-button-icon :close}
         (if icon {:icon-family :material-icons-filled})
         (param chip-props)))
