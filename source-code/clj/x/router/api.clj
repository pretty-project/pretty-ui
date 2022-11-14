
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.router.api
    (:require [x.router.route-handler.transfer]
              [x.router.default-handler.events :as default-handler.events]
              [x.router.default-handler.subs   :as default-handler.subs]
              [x.router.route-handler.config   :as route-handler.config]
              [x.router.route-handler.helpers  :as route-handler.helpers]
              [x.router.route-handler.events   :as route-handler.events]
              [x.router.route-handler.subs     :as route-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.router.default-handler.events
(def set-default-route! default-handler.events/set-default-route!)

; x.router.default-handler.subs
(def get-default-routes default-handler.subs/get-default-routes)
(def get-default-route  default-handler.subs/get-default-route)

; x.router.route-handler.helpers
(def request->route-prop              route-handler.helpers/request->route-prop)
(def request->route-template-matched? route-handler.helpers/request->route-template-matched?)

; x.router.route-handler.events
(def add-routes! route-handler.events/add-routes!)
(def add-route!  route-handler.events/add-route!)

; x.router.route-handler.subs
(def get-app-home       route-handler.subs/get-app-home)
(def use-app-home       route-handler.subs/use-app-home)
(def get-app-domain     route-handler.subs/get-app-domain)
(def use-app-domain     route-handler.subs/use-app-domain)
(def get-sitemap-routes route-handler.subs/get-sitemap-routes)
(def get-server-routes  route-handler.subs/get-server-routes)
(def get-client-routes  route-handler.subs/get-client-routes)
(def get-ordered-routes route-handler.subs/get-ordered-routes)
