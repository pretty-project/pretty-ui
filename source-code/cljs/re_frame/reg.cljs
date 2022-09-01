
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns re-frame.reg
    (:require [mid.re-frame.reg :as reg]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.re-frame.reg
(def reg-cofx        reg/reg-cofx)
(def reg-sub         reg/reg-sub)
(def reg-event-db    reg/reg-event-db)
(def reg-event-fx    reg/reg-event-fx)
(def apply-fx-params reg/apply-fx-params)
(def reg-fx          reg/reg-fx)
