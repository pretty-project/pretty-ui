
(ns pretty-renderers.renderer.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
; {:my-renderer {...}}
(defonce RENDERERS (ratom {}))
