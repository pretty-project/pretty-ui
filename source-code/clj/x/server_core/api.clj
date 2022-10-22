
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.api
    (:require [x.server-core.build-handler.transfer]
              [x.server-core.config-handler.side-effects]
              [x.server-core.config-handler.transfer]
              [x.server-core.database-handler.effects]
              [x.server-core.error-handler.side-effects]
              [x.server-core.resource-handler.subs]
              [x.server-core.server-handler.events]
              [x.server-core.server-handler.side-effects]
              [x.server-core.time-handler.transfer]
              [x.server-core.transfer-handler.lifecycles]
              [x.server-core.build-handler.side-effects     :as build-handler.side-effects]
              [x.server-core.build-handler.subs             :as build-handler.subs]
              [x.server-core.cache-handler.helpers          :as cache-handler.helpers]
              [x.server-core.config-handler.config          :as config-handler.config]
              [x.server-core.config-handler.subs            :as config-handler.subs]
              [x.server-core.debug-handler.helpers          :as debug-handler.helpers]
              [x.server-core.lifecycle-handler.side-effects :as lifecycle-handler.side-effects]
              [x.server-core.lifecycle-handler.subs         :as lifecycle-handler.subs]
              [x.server-core.server-handler.subs            :as server-handler.subs]
              [x.server-core.transfer-handler.side-effects  :as transfer-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-core.build-handler.side-effects
(def app-build   build-handler.side-effects/app-build)
(def ->app-built build-handler.side-effects/->app-built)

; x.server-core.build-handler.subs
(def get-app-build build-handler.subs/get-app-build)

; x.server-core.cache-handler.helpers
(def request->app-cached? cache-handler.helpers/request->app-cached?)
(def cache-control-uri    cache-handler.helpers/cache-control-uri)

; x.server-core.config-handler.config
(def APP-CONFIG-FILEPATH    config-handler.config/APP-CONFIG-FILEPATH)
(def SERVER-CONFIG-FILEPATH config-handler.config/SERVER-CONFIG-FILEPATH)

; x.server-core.config-handler.subs
(def get-app-config         config-handler.subs/get-app-config)
(def get-app-config-item    config-handler.subs/get-app-config-item)
(def get-server-config      config-handler.subs/get-server-config)
(def get-server-config-item config-handler.subs/get-server-config-item)

; x.server-core.debug-handler.helpers
(def request->debug-mode debug-handler.helpers/request->debug-mode)

; x.server-core.lifecycle-handler.side-effects
(def reg-lifecycles! lifecycle-handler.side-effects/reg-lifecycles!)

; x.server-core.lifecycle-handler.subs
(def get-period-events lifecycle-handler.subs/get-period-events)

; x.server-core.server-handler.subs
(def dev-mode? server-handler.subs/dev-mode?)

; x.server-core.transfer-handler.side-effects
(def reg-transfer! transfer-handler.side-effects/reg-transfer!)
