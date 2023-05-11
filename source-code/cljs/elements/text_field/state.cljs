
(ns elements.text-field.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
(def VALIDATE-FIELD-CONTENT? (ratom {}))

; @ignore
;
; @atom (map)
(def FIELD-CONTENT-INVALID? (ratom {}))
