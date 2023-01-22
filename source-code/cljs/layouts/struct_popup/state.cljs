
(ns layouts.struct-popup.state
    (:require [reagent.api :refer [ratom]]))

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
