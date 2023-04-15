
(ns elements.content-swapper.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
; {:my-swapper {:active-dex 42} ...}
(defonce SWAPPERS (ratom {}))
