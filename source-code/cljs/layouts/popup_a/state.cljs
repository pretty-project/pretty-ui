
(ns layouts.popup-a.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(defonce HEADER-SHADOW-VISIBLE? (ratom {}))

; @atom (map)
(defonce FOOTER-SHADOW-VISIBLE? (ratom {}))
