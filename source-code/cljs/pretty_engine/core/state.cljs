
(ns pretty-engine.core.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
; {:my-element (map)}
(defonce STATE (ratom {}))
