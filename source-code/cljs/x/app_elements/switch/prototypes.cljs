
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.switch.prototypes
    (:require [mid-fruits.candy             :refer [param return]]
              [x.app-elements.input.helpers :as input.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;
  ; @return (map)
  ;  {:border-color (keyword or string)
  ;   :font-size (keyword)
  ;   :option-label-f (function)
  ;   :option-value-f (function)
  ;   :options-orientation (keyword)
  ;   :options-path (vector)
  ;   :value-path (vector)}
  [switch-id switch-props]
  (merge {:border-color        :primary
          :font-size           :s
          :option-label-f      return
          :option-value-f      return
          :options-orientation :vertical
          :options-path        (input.helpers/default-options-path switch-id)
          :value-path          (input.helpers/default-value-path   switch-id)}
         (param switch-props)))
