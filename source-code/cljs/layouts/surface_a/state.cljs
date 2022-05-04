
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns layouts.surface-a.state
    (:require [reagent.api :refer [ratom]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (metamorphic-content)
(defonce HEADER-TITLE (ratom nil))

; @atom (boolean)
(defonce HEADER-TITLE-VISIBLE? (ratom false))

; @atom (boolean)
(defonce HEADER-SHADOW-VISIBLE? (ratom false))
