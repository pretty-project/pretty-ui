
(ns pretty-layouts.popup.state
    (:require [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
(defonce HEADER-SHADOW-VISIBLE? (reagent/atom {}))

; @ignore
;
; @atom (map)
(defonce FOOTER-SHADOW-VISIBLE? (reagent/atom {}))
