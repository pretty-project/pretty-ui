
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.re-frame.registrar
    (:require [re-frame.registrar :as registrar]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; re-frame.registrar
(def kind->id->handler registrar/kind->id->handler)
(def clear-handlers    registrar/clear-handlers)
(def get-handler       registrar/get-handler)
