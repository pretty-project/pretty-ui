
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.icon-button.prototypes
    (:require [candy.api :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ; {:background-color (keyword or string)(opt)
  ;  :hover-color (keyword or string)(opt)}
  ;
  ; @return (map)
  ; {:background-color (keyword or string)
  ;  :border-radius (keyword)
  ;  :color (keyword or string)
  ;  :height (keyword)
  ;  :icon-family (keyword)
  ;  :width (keyword)}
  [{:keys [background-color hover-color] :as button-props}]
  (merge {:color            :default
          :icon-family      :material-icons-filled
          :height           :xxl
          :width            :xxl}
         (if background-color {:border-radius :s})
         (if hover-color      {:border-radius :s})
         (param button-props)))
