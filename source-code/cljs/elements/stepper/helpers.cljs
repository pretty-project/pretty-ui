
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.stepper.helpers
    (:require [elements.element.helpers :as element.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stepper-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stepper-id
  ; @param (map) stepper-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ _]
  {})

(defn stepper-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stepper-id
  ; @param (map) stepper-props
  ;
  ; @return (map)
  [stepper-id stepper-props]
  (merge (element.helpers/element-default-attributes stepper-id stepper-props)
         (element.helpers/element-indent-attributes  stepper-id stepper-props)))
