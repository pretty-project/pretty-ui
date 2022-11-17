
; WARNING
; Az x.ui.renderer.prototypes névtér újraírása szükséges, a Re-Frame adatbázis
; események számának csökkentése érdekében!



;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.renderer.prototypes
    (:require [candy.api            :refer [param]]
              [x.ui.renderer.config :as renderer.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) element-props
  ;
  ; @return (map)
  ;  {:hide-animated? (boolean)
  ;   :reveal-animated? (boolean)}
  [element-props]
  (merge {:hide-animated?   true
          :reveal-animated? true}
         (param element-props)))

(defn renderer-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) renderer-props
  ;
  ; @return (map)
  ;  {:invisible-elements (vector)
  ;   :max-elements-rendered (integer)
  ;   :queue-behavior (keyword)
  ;   :required? (boolean)
  ;   :rerender-same? (boolean)}
  [renderer-props]
  (merge {:invisible-elements    []
          :max-elements-rendered renderer.config/DEFAULT-MAX-ELEMENTS-RENDERED
          :queue-behavior        :wait
          :required?             false
          :rerender-same?        false}
         (param renderer-props)))
