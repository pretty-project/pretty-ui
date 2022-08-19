

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-gestures.select-handler.prototypes
    (:require [mid-fruits.candy :refer [param]]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- handler-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) handler-props
  ;  {:enabled? (boolean)(opt)
  ;   :max-selected-count (integer)(opt)
  ;   :selected-items (vector)(opt)}
  ;
  ; @return (map)
  ;  {:enabled? (boolean)
  ;   :max-selected-count (integer)
  ;   :selected-items (vector)}
  [handler-props]
  (merge {:enabled?           true
          :max-selected-count 256
          :selected-items     []}
         (param handler-props)))
