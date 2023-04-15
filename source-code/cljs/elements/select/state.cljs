
(ns elements.select.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (boolean)
(def POPUP-VISIBLE? (ratom false))
