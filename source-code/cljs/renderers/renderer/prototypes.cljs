
; WARNING
; Az x.ui.renderer.prototypes névtér újraírása szükséges, a Re-Frame adatbázis
; események számának csökkentése érdekében!

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; x5 Clojure/ClojureScript web application engine
; https://monotech.hu/x5
;
; Copyright Adam Szűcs and other contributors - All rights reserved

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.renderer.prototypes
    (:require [x.ui.renderer.config :as renderer.config]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) element-props
  ;
  ; @return (map)
  ; {:hide-animated? (boolean)
  ;  :reveal-animated? (boolean)}
  [element-props]
  (merge {:hide-animated?   true
          :reveal-animated? true}
         (-> element-props)))

(defn renderer-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) renderer-props
  ;
  ; @return (map)
  ; {:invisible-elements (vector)
  ;  :max-elements-rendered (integer)
  ;  :queue-behavior (keyword)
  ;  :rerender-same? (boolean)}
  [renderer-props]
  (merge {:invisible-elements    []
          :max-elements-rendered renderer.config/DEFAULT-MAX-ELEMENTS-RENDERED
          :queue-behavior        :wait
          :rerender-same?        false}
         (-> renderer-props)))
