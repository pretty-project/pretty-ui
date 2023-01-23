
(ns elements.select.state
    (:require [reagent.core :rename {atom ratom}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (boolean)
(def POPUP-VISIBLE? (ratom false))
