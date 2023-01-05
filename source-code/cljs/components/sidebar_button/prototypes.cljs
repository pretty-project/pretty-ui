
(ns components.sidebar-button.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:font-size (keyword)
  ;  :hover-color (keyword)
  ;  :font-weight (keyword)
  ;  :icon-family (keyword)}
  [{:keys [] :as button-props}]
  (merge {:font-size   :xs
          :font-weight :bold
          :hover-color :invert
          :icon-family :material-icons-filled}
         (param button-props)))
