
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.text-editor.state
    (:require [reagent.api :refer [ratom]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(defonce EDITOR-INPUT (ratom {}))

; @atom (map)
(defonce EDITOR-OUTPUT (ratom {}))

; @atom (map)
(defonce EDITOR-TRIGGER (ratom {}))
