

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.print-handler.config
    (:require [mid-fruits.time :as time]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
(def APP-STARTED-AT (time/elapsed))

; @constant (ms)
(def SEPARATOR-DELAY 2000)
