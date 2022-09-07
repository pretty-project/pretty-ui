
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.thumbnail.prototypes
    (:require [mid-fruits.candy :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) thumbnail-props
  ;
  ; @return (map)
  ;  {:background-size (keyword)
  ;   :height (keyword)
  ;   :width (keyword)}
  [thumbnail-props]
  (merge {:background-size :contain
          :height          :s
          :width           :s}
         (param thumbnail-props)))
