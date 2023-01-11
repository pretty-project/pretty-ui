
(ns elements.icon-button.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :marker-color (keyword)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword)
  ;  :badge-position (keyword)
  ;  :height (keyword)
  ;  :hover-color (keyword)
  ;  :icon-family (keyword)
  ;  :marker-position (keyword)
  ;  :width (keyword)}
  [{:keys [badge-content disabled? marker-color] :as button-props}]
  (merge {:icon-family :material-icons-filled
          :height      :3xl
          :width       :3xl}
         (if badge-content {:badge-color :primary :badge-position :tr})
         (if marker-color  {:marker-position :tr})
         (param button-props)
         (if disabled? {:hover-color :none})))
