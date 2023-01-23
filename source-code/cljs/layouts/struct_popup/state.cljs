
(ns layouts.struct-popup.state
    (:require [reagent.core :rename {atom ratom}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
(defonce HEADER-SHADOW-VISIBLE? (ratom {}))

  ; @ignore
  ;
; @atom (map)
(defonce FOOTER-SHADOW-VISIBLE? (ratom {}))
