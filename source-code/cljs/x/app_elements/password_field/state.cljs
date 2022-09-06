
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.password-field.state
    (:require [reagent.api :refer [ratom]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(defonce PASSWORD-VISIBILITY (ratom {}))