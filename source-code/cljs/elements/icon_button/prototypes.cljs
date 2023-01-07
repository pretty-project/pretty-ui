
(ns elements.icon-button.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ; {:badge-content (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword)
  ;  :badge-position (keyword)
  ;  :height (keyword)
  ;  :icon-family (keyword)
  ;  :width (keyword)}
  [{:keys [badge-content marker-color] :as button-props}]
  (merge {:icon-family :material-icons-filled
          :height      :xxl
          :width       :xxl}
         (if badge-content {:badge-color :primary :badge-position :tr})
         (if marker-color  {:marker-position :tr})
         (param button-props)))
