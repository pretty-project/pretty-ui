
(ns elements.input.state
    (:require [reagent.api :refer [ratom]]))

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
