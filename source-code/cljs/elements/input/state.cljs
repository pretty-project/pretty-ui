
(ns elements.input.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(def FOCUSED-INPUTS (ratom {}))

; @atom (map)
(def VISITED-INPUTS (ratom {}))
