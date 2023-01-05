
(ns elements.button.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ; {:background-color (keyword or string)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;  :icon (keyword)(opt)}
  ;
  ; @return (map)
  ; {:border-radius (keyword)
  ;  :color (keyword or string)
  ;  :font-size (keyword)
  ;  :font-weight (keyword)
  ;  :horizontal-align (keyword)
  ;  :icon-family (keyword)
  ;  :icon-position (keyword)
  ;  :line-height (keyword)}
  [{:keys [background-color hover-color icon] :as button-props}]
  (merge {:color            :default
          :font-size        :s
          :font-weight      :bold
          :horizontal-align :center
          :line-height      :normal}
         ;(if background-color {:border-radius :s})
         ;(if hover-color      {:border-radius :s})
         (if icon             {:icon-family   :material-icons-filled
                               :icon-position :left})
         (param button-props)))
