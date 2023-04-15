
(ns website.sidebar.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @description
; Stores the ID of the currently visible sidebar.
;
; @atom (keyword)
(def VISIBLE-SIDEBAR (ratom nil))
