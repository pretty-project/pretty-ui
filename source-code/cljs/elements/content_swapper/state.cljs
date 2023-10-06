
(ns elements.content-swapper.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
; {:my-swapper {:active-page (keyword)
;               :animation-direction (keyword)
;                :bwd, :fwd
;               :page-pool (maps in vector)
;                Contains the newly mounted page and the currently unmounting page.
;                [{:id (keyword)
;                  :page (metamorphic-content)}]}}
(defonce SWAPPERS (ratom {}))
 
