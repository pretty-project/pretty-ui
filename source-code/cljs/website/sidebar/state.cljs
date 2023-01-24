
(ns website.sidebar.state
    (:require [reagent.core :rename {atom ratom}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @description
; Stores the ID of the currently visible sidebar.
;
; @atom (keyword)
(def VISIBLE-SIDEBAR (ratom nil))
