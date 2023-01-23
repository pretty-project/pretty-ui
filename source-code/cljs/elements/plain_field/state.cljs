
(ns elements.plain-field.state
    (:require [reagent.core :rename {atom ratom}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
(defonce FIELD-CONTENTS (ratom {}))

; @ignore
;
; @atom (map)
(defonce FIELD-OUTPUTS (ratom {}))

; @ignore
;
; @atom (map)
(defonce FIELD-STATES (ratom {}))

; @ignore
;
; @atom (keyword)
(defonce VISIBLE-SURFACE (ratom nil))
