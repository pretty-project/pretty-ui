
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.re-frame.core
    (:require [re-frame.core :as core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING!
; A mid.re-frame.core névtér egyedüli feladata a re-frame.core névtér
; egyes függvényeinek átirányítása. A monoset könyvtáron belül CLJS és CLJ oldalon
; nem lehetséges ennek a névtérnek a re-frame.core nevű megfelelőit létrehozni,
; mivel azok névütközésben lennének az eredeti re-frame.core névtérrel!



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; re-frame.core
(def reg-cofx      core/reg-cofx)
(def reg-sub       core/reg-sub)
(def reg-event-db  core/reg-event-db)
(def reg-event-fx  core/reg-event-fx)
(def reg-fx        core/reg-fx)
(def subscribe     core/subscribe)
(def ->interceptor core/->interceptor)
(def inject-cofx   core/inject-cofx)
(def dispatch      core/dispatch)
(def dispatch-sync core/dispatch-sync)
