
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.temporary-component.api
    (:require [tools.temporary-component.side-effects :as side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; tools.temporary-component.side-effects
(def append-component! side-effects/append-component!)
(def remove-component! side-effects/remove-component!)
