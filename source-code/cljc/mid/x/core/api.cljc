
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.core.api
    (:require [mid.x.core.build-handler.events]
              [mid.x.core.build-handler.subs]
              [mid.x.core.config-handler.events]
              [mid.x.core.build-handler.side-effects :as build-handler.side-effects]
              [mid.x.core.cache-handler.helpers      :as cache-handler.helpers]
              [mid.x.core.config-handler.subs        :as config-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.core.build-handler.side-effects
(def build-version build-handler.side-effects/build-version)

; mid.x.core.cache-handler.helpers
(def cache-control-uri cache-handler.helpers/cache-control-uri)

; mid.x.core.config-handler.subs
(def get-app-config         config-handler.subs/get-app-config)
(def get-app-config-item    config-handler.subs/get-app-config-item)
(def get-server-config      config-handler.subs/get-server-config)
(def get-server-config-item config-handler.subs/get-server-config-item)
