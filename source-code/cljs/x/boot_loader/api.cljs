
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader.api
    (:require [x.app-activities.api]
              [x.app-dictionary.api]
              [x.app-environment.api]
              [x.locales.api]
              [x.app-media.api]
              [x.app-sync.api]
              [x.views.api]
              [x.boot-loader.effects]
              [x.boot-loader.subs]
              [x.boot-loader.events       :as events]
              [x.boot-loader.side-effects :as side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.boot-loader.events
(def set-restart-target! events/set-restart-target!)

; x.boot-loader.side-effects
(def start-app!  side-effects/start-app!)
(def render-app! side-effects/render-app!)
