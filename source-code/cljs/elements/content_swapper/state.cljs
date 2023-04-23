
(ns elements.content-swapper.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
; {:my-swapper {:animation-direction (keyword)
;                :bwd, :fwd
;               :current-page (keyword)
;                :a, :b
;               :page-a (metamorphic-content)
;               :page-b (metamorphic-content)}}
(defonce SWAPPERS (ratom {}))
