
(ns pretty-inputs.input.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (keyword)
(def RENDERED-POPUP (ratom nil))

; @ignore
;
; @atom (map)
(def FOCUSED-INPUTS (ratom {}))

; @ignore
;
; @atom (map)
(def VISITED-INPUTS (ratom {}))
