
(ns pretty-renderers.renderer.state
    (:require [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
; {:my-renderer {...}}
(defonce RENDERERS (reagent/atom {}))
