
(ns pretty-forms.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
; Stored props of registered inputs.
;
; @atom (map)
; {:my-input (map)
;   {:form-id (keyword)
;    :get-value-f (function)
;     Must return the actual value of the input.
;    :validators (maps in vector)
;     [{:f (function)
;        Takes the actual value of the input as parameter.
;       :invalid-message (metamorphic-content)(opt)}]
;    :validate-when-change? (boolean)(opt)
;    :validate-when-leave? (boolean)(opt)
;    :value-path (Re-Frame path vector)}}
;
; @usage
; (deref FORM-INPUTS)
; =>
; {:my-input {:form-id     :my-form
;             :get-value-f #(deref MY-ATOM)
;             :validators  [{:f #(-> % empty? not) :invalid-message "Please fill out this field!"}]}
(def FORM-INPUTS (ratom {}))

; @description
; Stored error messages of failed input validations.
;
; @atom (map)
; {:my-input (metamorphic-content)(opt)}
;
; @usage
; (deref FORM-ERRORS)
; =>
; {:my-input "Please fill out this field!"}
(def FORM-ERRORS (ratom {}))
