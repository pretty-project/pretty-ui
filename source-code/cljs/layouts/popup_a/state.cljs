
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns layouts.popup-a.state
    (:require [reagent.api :refer [ratom]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (boolean)
(defonce HEADER-SHADOW-VISIBLE? (ratom false))

; @atom (boolean)
(defonce FOOTER-SHADOW-VISIBLE? (ratom false))
