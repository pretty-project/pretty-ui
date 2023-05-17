
(ns elements.form.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
; {:my-input {:form-id (keyword)
;             :validators (maps in vector)
;             :validate-when-change? (boolean)(opt)
;             :validate-when-leave? (boolean)(opt)
;             :value-path (Re-Frame path vector)}}
(def FORM-INPUTS (ratom {}))

; @ignore
;
; @atom (map)
; {:my-input (metamorphic-content)(opt)}
(def FORM-ERRORS (ratom {}))
