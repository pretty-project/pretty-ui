
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.api
    (:require [x.server-router.route-handler.transfer]
              [x.server-router.default-handler.events :as default-handler.events]
              [x.server-router.default-handler.subs   :as default-handler.subs]
              [x.server-router.route-handler.helpers  :as route-handler.helpers]
              [x.server-router.route-handler.events   :as route-handler.events]
              [x.server-router.route-handler.subs     :as route-handler.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-router.default-handler.events
(def set-default-route! default-handler.events/set-default-route!)

; x.server-router.default-handler.subs
(def get-default-routes default-handler.subs/get-default-routes)
(def get-default-route  default-handler.subs/get-default-route)

; x.server-router.route-handler.helpers
(def request->route-prop route-handler.helpers/request->route-prop)
(def request->core-js    route-handler.helpers/request->core-js)

; x.server-router.route-handler.events
(def add-routes! route-handler.events/add-routes!)
(def add-route!  route-handler.events/add-route!)

; x.server-router.route-handler.subs
(def get-app-home       route-handler.subs/get-app-home)
(def use-app-home       route-handler.subs/use-app-home)
(def get-sitemap-routes route-handler.subs/get-sitemap-routes)
(def get-client-routes  route-handler.subs/get-client-routes)
(def get-server-routes  route-handler.subs/get-server-routes)
(def get-ordered-routes route-handler.subs/get-ordered-routes)
