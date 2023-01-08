
(ns elements.button.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :font-size (keyword)(opt)
  ;  :icon (keyword)(opt)
  ;  :marker-color (keyword)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword)
  ;  :badge-position (keyword)
  ;  :font-size (keyword)
  ;  :font-weight (keyword)
  ;  :horizontal-align (keyword)
  ;  :icon-family (keyword)
  ;  :icon-position (keyword)
  ;  :icon-size (keyword)
  ;  :line-height (keyword)}
  [{:keys [badge-content font-size icon marker-color] :as button-props}]
  (merge {:font-size        :s
          :font-weight      :bold
          :horizontal-align :center
          :line-height      :block}
         (if badge-content {:badge-color :primary :badge-position :tr})
         (if marker-color  {:marker-position :tr})
         (if icon          {:icon-family   :material-icons-filled
                            :icon-position :left
                            :icon-size (or font-size :s)})
         (param button-props)))
