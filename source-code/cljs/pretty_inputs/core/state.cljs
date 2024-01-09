
(ns pretty-inputs.core.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @description
; Stores the ID of the input which displays a popup.
; Only one input can display a popup at a time.
;
; @atom (keyword)
; :my-input
(def RENDERED-POPUP (ratom nil))

; @ignore
;
; Stores the ID of the input which is focused.
; Only one input can be focused at a time.
;
; @atom (keyword)
; :my-input
(def FOCUSED-INPUT (ratom nil))

; @ignore
;
; Stores the IDs of the inputs which are already changed at least once.
;
; @atom (map)
; {:my-input (boolean)}
(def CHANGED-INPUTS (ratom {}))

; @ignore
;
; @description
; - Stores a shadow value for each input to avoid misread input values in case
;   the provided 'get-value-f' function returns the updated input value with a latency when it gets updated.
; - It also provides a default input value source in case the 'get-value-f' function is not provided.
;
; @atom (map)
; {:my-input (*)}
(def INPUT-INTERNAL-VALUES (ratom {}))
