
(ns pretty-elements.content-swapper.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
; {:my-swapper {:active-content (keyword)
;               :animation-direction (keyword)
;                :bwd, :fwd
;               :content-pool (maps in vector)
;                Contains the newly mounted content and the currently unmounting content.
;                [{:id (keyword)
;                  :content (metamorphic-content)}]}}
(defonce SWAPPERS (ratom {}))
