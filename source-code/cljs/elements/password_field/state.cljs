
(ns elements.password-field.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(defonce PASSWORD-VISIBILITY (ratom {}))
