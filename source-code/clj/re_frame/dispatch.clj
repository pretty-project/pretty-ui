
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns re-frame.dispatch
    (:require [mid.re-frame.dispatch :as dispatch]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.re-frame.dispatch
(def dispatch       dispatch/dispatch)
(def dispatch-fx    dispatch/dispatch-fx)
(def dispatch-sync  dispatch/dispatch-sync)
(def dispatch-n     dispatch/dispatch-n)
(def dispatch-later dispatch/dispatch-later)
(def dispatch-if    dispatch/dispatch-if)
(def dispatch-cond  dispatch/dispatch-cond)
