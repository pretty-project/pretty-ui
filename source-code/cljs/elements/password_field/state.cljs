
(ns elements.password-field.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (map)
(defonce PASSWORD-VISIBILITY (ratom {}))
