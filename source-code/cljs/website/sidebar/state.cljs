
(ns website.sidebar.state
    (:require [reagent.core :rename {atom ratom}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (boolean)
(def VISIBLE-SIDEBAR (ratom false))
