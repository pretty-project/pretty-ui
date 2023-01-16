
(ns elements.icon-button.prototypes
    (:require [candy.api        :refer [param]]
              [x.components.api :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :marker-color (keyword)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword)
  ;  :badge-position (keyword)
  ;  :height (keyword)
  ;  :hover-color (keyword)
  ;  :icon-family (keyword)
  ;  :icon-size (keyword)
  ;  :marker-position (keyword)
  ;  :tooltip-content (string)
  ;  :tooltip-position (keyword)
  ;  :width (keyword)}
  [{:keys [badge-content disabled? marker-color tooltip-content] :as button-props}]
  (merge {:icon-family :material-symbols-outlined
          :icon-size   :m
          :height      :3xl
          :width       :3xl}
         (if badge-content   {:badge-color :primary :badge-position :tr})
         (if marker-color    {:marker-position  :tr})
         (if tooltip-content {:tooltip-position :right})
         (param button-props)
         (if disabled?       {:hover-color :none})
         (if tooltip-content {:tooltip-content (x.components/content tooltip-content)})))
