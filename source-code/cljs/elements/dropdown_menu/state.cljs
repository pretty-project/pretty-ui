
(ns elements.dropdown-menu.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (integer)
(defonce ACTIVE-DEX (ratom nil))
