
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.build-handler.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
;  A "resources/public" mappában elhelyezett "x.build-version.edn" fájl minden
;  esetben a lefordított JAR fájl része!
(def BUILD-VERSION-FILEPATH "resources/public/x.build-version.edn")

; @constant (string)
(def INITIAL-BUILD-VERSION "0.0.1")
