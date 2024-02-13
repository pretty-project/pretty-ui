
(ns pretty-website.sidebar.state
    (:require [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @description
; Stores the ID of the currently visible sidebar.
;
; @atom (keyword)
(def VISIBLE-SIDEBAR (reagent/atom nil))
