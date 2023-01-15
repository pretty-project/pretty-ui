
(ns elements.thumbnail.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) thumbnail-props
  ; {:border-color (keyword or string)(opt)}
  ;
  ; @return (map)
  ; {:background-size (keyword)
  ;  :border-color (keyword)
  ;  :border-position (keyword)
  ;  :border-width (keyword)
  ;  :height (keyword)
  ;  :icon (keyword)
  ;  :icon-family (keyword)
  ;  :width (keyword)}
  [{:keys [border-color] :as thumbnail-props}]
  (merge {:background-size :contain
          :height          :s
          :width           :s
          :icon            :image
          :icon-family     :material-symbols-outlined}
         (if border-color {:border-color    :default
                           :border-position :all
                           :border-width    :xxs})
         (param thumbnail-props)))
