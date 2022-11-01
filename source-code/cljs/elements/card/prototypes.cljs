
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.card.prototypes
    (:require [mid-fruits.candy :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) card-props
  ;
  ; @return (map)
  ;  {:border-radius (keyword)
  ;   :horizontal-align (keyword)}
  [card-props]
  (merge {:border-radius    :s
          :horizontal-align :center}
         (param card-props)))
