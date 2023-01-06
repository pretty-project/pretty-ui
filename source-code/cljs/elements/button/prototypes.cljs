
(ns elements.button.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ; {:font-size (keyword)(opt)
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
  ;  :icon-size (keyword)
  ;  :line-height (keyword)}
  [{:keys [font-size icon] :as button-props}]
  (merge {:color            :default
          :font-size        :s
          :font-weight      :bold
          :horizontal-align :center
          :line-height      :normal}
         (if icon           {:icon-family   :material-icons-filled
                             :icon-position :left
                             :icon-size (or font-size :s)})
         (param button-props)))
