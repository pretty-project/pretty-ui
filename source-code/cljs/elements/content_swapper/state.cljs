
(ns elements.content-swapper.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
; {:my-swapper {:animation-direction (keyword)
;                :bwd, :fwd
;               :page-cursor (integer)
;               :page-history (metamorphic-contents in vector)}}
(defonce SWAPPERS (ratom {}))
