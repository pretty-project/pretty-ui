
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.api
    (:require [x.mid-core.build-handler.events]
              [x.mid-core.build-handler.subs]
              [x.mid-core.config-handler.events]
              [x.mid-core.build-handler.side-effects :as build-handler.side-effects]
              [x.mid-core.cache-handler.helpers      :as cache-handler.helpers]
              [x.mid-core.config-handler.subs        :as config-handler.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.build-handler.side-effects
(def app-build build-handler.side-effects/app-build)

; x.mid-core.cache-handler.helpers
(def cache-control-uri cache-handler.helpers/cache-control-uri)

; x.mid-core.config-handler.subs
(def get-app-config         config-handler.subs/get-app-config)
(def get-app-config-item    config-handler.subs/get-app-config-item)
(def get-server-config      config-handler.subs/get-server-config)
(def get-server-config-item config-handler.subs/get-server-config-item)
