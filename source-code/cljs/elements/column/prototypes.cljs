
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.column.prototypes
    (:require [candy.api :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) column-props
  ;
  ; @return (map)
  ;  {:horizontal-align (keyword)
  ;   :stretch-orientation (keyword)
  ;   :vertical-align (keyword)}
  [column-props]
  (merge {:horizontal-align    :center
          :stretch-orientation :vertical
          :vertical-align      :top}
         (param column-props)))
