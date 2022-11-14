
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.date-field.prototypes
    (:require [candy.api         :refer [param]]
              [mid-fruits.random :as random]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ;  {:autofill-name (keyword)
  ;   :type (keyword)}
  [_ field-props]
  (merge {}
         (param field-props)
         {; XXX#6782 (source-code/cljs/elements/text-field/prototypes.cljs)
          :autofill-name (random/generate-keyword)
          :type          :date}))
