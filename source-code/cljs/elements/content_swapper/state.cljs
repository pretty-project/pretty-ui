
(ns elements.content-swapper.state
    (:require [reagent.core :rename {atom ratom}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
; {:my-swapper {:active-dex 42} ...}
(defonce SWAPPERS (ratom {}))
