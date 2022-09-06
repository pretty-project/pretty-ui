
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.date-field.prototypes
    (:require [mid-fruits.candy :refer [param]]
              [x.app-core.api   :as a]))



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
          ; XXX#6782
         {:autofill-name (a/id)
          :type          :date}))