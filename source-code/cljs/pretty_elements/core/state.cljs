
(ns pretty-elements.core.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
; {:my-element (map)}
(defonce ELEMENT-STATE (ratom {}))
