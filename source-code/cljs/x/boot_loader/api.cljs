

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader.api
    (:require [x.app-components.api]
              [x.app-developer.api]
              [x.app-dictionary.api]
              [x.app-environment.api]
              [x.app-locales.api]
              [x.app-media.api]
              [x.app-sync.api]
              [x.app-tools.api]
              [x.app-views.api]
              [x.boot-loader.effects]
              [x.boot-loader.subs]
              [x.boot-loader.events       :as events]
              [x.boot-loader.side-effects :as side-effects]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.boot-loader.events
(def set-restart-target! events/set-restart-target!)

; x.boot-loader.side-effects
(def start-app!  side-effects/start-app!)
(def render-app! side-effects/render-app!)
