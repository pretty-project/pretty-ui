
(ns elements.dropdown-menu.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @atom (metamorphic-content)
(defonce VISIBLE-CONTENT (ratom nil))
