
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.api
    (:require [developer-tools.core.effects]
              [developer-tools.core.subs]
              [developer-tools.event-browser.effects]
              [developer-tools.magic-widget.effects]
              [developer-tools.re-frame-browser.events]
              [developer-tools.re-frame-browser.subs]
              [developer-tools.request-inspector.events]
              [developer-tools.request-inspector.subs]
              [developer-tools.subscription-browser.effects]
              [developer-tools.database-screen.views :as database-screen.views]
              [developer-tools.magic-button.views    :as magic-button.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; developer-tools.database-screen.views
(def database-screen database-screen.views/view)

; developer-tools.magic-button.views
(def magic-button magic-button.views/element)
