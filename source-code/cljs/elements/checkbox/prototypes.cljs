
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.checkbox.prototypes
    (:require [elements.input.helpers :as input.helpers]
              [mid-fruits.candy       :refer [param return]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  ;  {:border-color (keyword or string)
  ;   :font-size (keyword)
  ;   :option-label-f (function)
  ;   :option-value-f (function)
  ;   :options-orientation (keyword)
  ;   :options-path (vector)
  ;   :value-path (vector)}
  [checkbox-id checkbox-props]
  (merge {:border-color        :primary
          :font-size           :s
          :option-label-f      return
          :option-value-f      return
          :options-orientation :vertical
          :options-path        (input.helpers/default-options-path checkbox-id)
          :value-path          (input.helpers/default-value-path   checkbox-id)}
         (param checkbox-props)))