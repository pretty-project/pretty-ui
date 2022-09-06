
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.multiline-field.prototypes
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
  ;   :max-height (integer)
  ;   :min-height (integer)
  ;   :multiline? (boolean)}
  [field-id field-props]
  (merge {:max-height 32
          :min-height 1}
         (param field-props)
         {:multiline? true
          ; XXX#6782
          :autofill-name (a/id)}))