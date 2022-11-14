
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.surface.prototypes
    (:require [candy.api :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) surface-props
  ;
  ; @return (map)
  ;  {:hide-animated? (boolean)
  ;   :reveal-animated? (boolean)
  ;   :update-animated? (boolean)}
  [surface-props]
  (merge {}
         (param surface-props)
         {:hide-animated?   false
          :reveal-animated? false
          :update-animated? false}))
