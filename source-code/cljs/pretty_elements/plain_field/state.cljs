
(ns pretty-elements.plain-field.state
    (:require [reagent.api :refer [ratom]]))

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
