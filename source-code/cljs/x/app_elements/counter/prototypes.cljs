
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.counter.prototypes
    (:require [mid-fruits.candy             :refer [param]]
              [x.app-elements.input.helpers :as input.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn counter-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (map)
  ;  {:border-color (keyword or string)
  ;   :font-size (keyword)
  ;   :initial-value (integer)
  ;   :value-path (vector)}
  [counter-id counter-props]
  (merge {:border-color  :primary
          :font-size     :s
          :initial-value 0
          :value-path    (input.helpers/default-value-path counter-id)}
         (param counter-props)))
