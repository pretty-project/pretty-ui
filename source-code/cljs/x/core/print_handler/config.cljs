
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.print-handler.config
    (:require [time.api :as time]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
(def APP-STARTED-AT (time/elapsed))

; @constant (ms)
(def SEPARATOR-DELAY 2000)
