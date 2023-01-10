
(ns elements.plain-field.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(defonce FIELD-CONTENTS (ratom {}))

; @atom (map)
(defonce FIELD-OUTPUTS (ratom {}))

; @atom (map)
(defonce FIELD-STATES (ratom {}))

; @atom (keyword)
(defonce VISIBLE-SURFACE (ratom nil))
