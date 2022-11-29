
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



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
