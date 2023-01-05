
(ns components.side-menu-button.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:font-size (keyword)
  ;  :hover-color (keyword)
  ;  :icon-family (keyword)}
  [{:keys [] :as button-props}]
  (merge {:font-size   :xs
          :hover-color :highlight
          :icon-family :material-icons-filled}
         (param button-props)))
