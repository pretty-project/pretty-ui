
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns layouts.surface-a.state
    (:require [plugins.reagent.api :refer [ratom]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (metamorphic-content)
(defonce HEADER-TITLE (ratom nil))

; @atom (boolean)
(defonce HEADER-TITLE-VISIBLE? (ratom false))

; @atom (boolean)
(defonce HEADER-SHADOW-VISIBLE? (ratom false))
