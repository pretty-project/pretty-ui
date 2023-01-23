
(ns elements.input.state
    (:require [reagent.core :rename {atom ratom}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
(def FOCUSED-INPUTS (ratom {}))

; @ignore
;
; @atom (map)
(def VISITED-INPUTS (ratom {}))
