
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.radio-button.prototypes
    (:require [mid-fruits.candy             :refer [param return]]
              [x.app-elements.input.helpers :as input.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ;  {:border-color (keyword or string)
  ;   :font-size (keyword)
  ;   :option-label-f (function)
  ;   :option-value-f (function)
  ;   :options-orientation (keyword)
  ;   :options-path (vector)
  ;   :value-path (vector)}
  [button-id button-props]
  (merge {:border-color        :primary
          :font-size           :s
          :options-orientation :vertical
          :options-path        (input.helpers/default-options-path button-id)
          :value-path          (input.helpers/default-value-path   button-id)
          :option-label-f      return
          :option-value-f      return}
         (param button-props)))
