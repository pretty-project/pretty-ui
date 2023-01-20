
(ns elements.dropdown-menu.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (metamorphic-content)
(defonce VISIBLE-CONTENT (ratom nil))
