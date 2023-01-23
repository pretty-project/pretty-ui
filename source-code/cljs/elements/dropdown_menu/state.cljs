
(ns elements.dropdown-menu.state
    (:require [reagent.core :rename {atom ratom}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (integer)
(defonce ACTIVE-DEX (ratom nil))
