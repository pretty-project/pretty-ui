
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.api
    (:require [x.core.build-handler.installer]
              [x.core.build-handler.transfer]
              [x.core.config-handler.side-effects]
              [x.core.config-handler.transfer]
              [x.core.database-handler.effects]
              [x.core.error-handler.side-effects]
              [x.core.resource-handler.subs]
              [x.core.server-handler.events]
              [x.core.server-handler.side-effects]
              [x.core.time-handler.transfer]
              [x.core.transfer-handler.lifecycles]
              [x.core.build-handler.side-effects     :as build-handler.side-effects]
              [x.core.build-handler.subs             :as build-handler.subs]
              [x.core.cache-handler.helpers          :as cache-handler.helpers]
              [x.core.config-handler.config          :as config-handler.config]
              [x.core.config-handler.subs            :as config-handler.subs]
              [x.core.debug-handler.helpers          :as debug-handler.helpers]
              [x.core.install-handler.side-effects   :as install-handler.side-effects]
              [x.core.js-handler.config              :as js-handler.config]
              [x.core.lifecycle-handler.side-effects :as lifecycle-handler.side-effects]
              [x.core.lifecycle-handler.subs         :as lifecycle-handler.subs]
              [x.core.server-handler.subs            :as server-handler.subs]
              [x.core.transfer-handler.side-effects  :as transfer-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.core.build-handler.side-effects
(def build-version         build-handler.side-effects/build-version)
(def update-build-version! build-handler.side-effects/update-build-version!)

; x.core.build-handler.subs
(def get-build-version build-handler.subs/get-build-version)

; x.core.cache-handler.helpers
(def request->app-cached? cache-handler.helpers/request->app-cached?)
(def cache-control-uri    cache-handler.helpers/cache-control-uri)

; x.core.config-handler.config
(def APP-CONFIG-FILEPATH    config-handler.config/APP-CONFIG-FILEPATH)
(def SERVER-CONFIG-FILEPATH config-handler.config/SERVER-CONFIG-FILEPATH)

; x.core.config-handler.subs
(def get-app-config         config-handler.subs/get-app-config)
(def get-app-config-item    config-handler.subs/get-app-config-item)
(def get-server-config      config-handler.subs/get-server-config)
(def get-server-config-item config-handler.subs/get-server-config-item)

; x.core.install-handler.side-effects
(def reg-installer! install-handler.side-effects/reg-installer!)

; x.core.js-handler.config
(def DEFAULT-JS-BUILD js-handler.config/DEFAULT-JS-BUILD)

; x.core.debug-handler.helpers
(def request->debug-mode debug-handler.helpers/request->debug-mode)

; x.core.lifecycle-handler.side-effects
(def reg-lifecycles! lifecycle-handler.side-effects/reg-lifecycles!)

; x.core.lifecycle-handler.subs
(def get-period-events lifecycle-handler.subs/get-period-events)

; x.core.server-handler.subs
(def dev-mode? server-handler.subs/dev-mode?)

; x.core.transfer-handler.side-effects
(def reg-transfer! transfer-handler.side-effects/reg-transfer!)
