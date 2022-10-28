
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.chip.prototypes
    (:require [mid-fruits.candy :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) chip-props
  ;  {:icon (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:background-color (keyword or string)
  ;   :color (keyword or string)
  ;   :icon-family (keyword)
  ;   :primary-button-icon (keyword)}
  [{:keys [icon] :as chip-props}]
  (merge {:background-color    :primary
          :color               :default
          :primary-button-icon :close}
         (if icon {:icon-family :material-icons-filled})
         (param chip-props)))
