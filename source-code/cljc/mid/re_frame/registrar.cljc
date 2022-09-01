
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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING!
; A mid.re-frame.registrar névtér egyedüli feladata a re-frame.registrar névtér
; egyes függvényeinek átirányítása. A monoset könyvtáron belül CLJS és CLJ oldalon
; nem lehetséges ennek a névtérnek a re-frame.registrar nevű megfelelőit létrehozni,
; mivel azok névütközésben lennének az eredeti re-frame.registrar névtérrel!



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; re-frame.registrar
(def kind->id->handler registrar/kind->id->handler)
(def clear-handlers    registrar/clear-handlers)
(def get-handler       registrar/get-handler)
