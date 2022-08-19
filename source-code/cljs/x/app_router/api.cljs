

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.api
    (:require [x.app-router.route-handler.effects]
              [x.app-router.route-handler.events]
              [x.app-router.route-handler.lifecycles]
              [x.app-router.route-handler.side-effects]
              [x.app-router.route-handler.subs :as route-handler.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-router.route-handler.subs
(def get-app-home                   route-handler.subs/get-app-home)
(def use-app-home                   route-handler.subs/use-app-home)
(def use-path-params                route-handler.subs/use-path-params)
(def get-client-routes              route-handler.subs/get-client-routes)
(def get-current-route-string       route-handler.subs/get-current-route-string)
(def get-current-route-id           route-handler.subs/get-current-route-id)
(def get-current-route-path         route-handler.subs/get-current-route-path)
(def get-current-route-template     route-handler.subs/get-current-route-template)
(def get-current-route-path-params  route-handler.subs/get-current-route-path-params)
(def get-current-route-path-param   route-handler.subs/get-current-route-path-param)
(def get-current-route-query-params route-handler.subs/get-current-route-query-params)
(def get-current-route-query-param  route-handler.subs/get-current-route-query-param)
(def get-current-route-fragment     route-handler.subs/get-current-route-fragment)
(def get-current-route-parent       route-handler.subs/get-current-route-parent)
(def current-route-path-param?      route-handler.subs/current-route-path-param?)
(def at-home?                       route-handler.subs/at-home?)
(def home-next-door?                route-handler.subs/home-next-door?)
