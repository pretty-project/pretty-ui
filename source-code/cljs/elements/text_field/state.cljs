
(ns elements.text-field.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(defonce FIELD-STATES (ratom {}))

; @atom (keyword)
(defonce VISIBLE-SURFACE (ratom nil))
