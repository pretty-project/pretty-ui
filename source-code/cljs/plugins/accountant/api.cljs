
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.accountant.api
    (:require [plugins.accountant.side-effects :as side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.accountant.side-effects
(def configure-navigation!   side-effects/configure-navigation!)
(def unconfigure-navigation! side-effects/unconfigure-navigation!)
(def navigate!               side-effects/navigate!)
(def dispatch-current!       side-effects/dispatch-current!)
