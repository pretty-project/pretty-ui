
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.api
    (:require [x.app-developer.developer-tools.effects]
              [x.app-developer.developer-tools.subs]
              [x.app-developer.event-browser.effects]
              [x.app-developer.re-frame-browser.events]
              [x.app-developer.re-frame-browser.subs]
              [x.app-developer.request-inspector.events]
              [x.app-developer.request-inspector.subs]
              [x.app-developer.database-screen.views :as database-screen.views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-developer.database-screen.views
(def database-screen database-screen.views/view)
