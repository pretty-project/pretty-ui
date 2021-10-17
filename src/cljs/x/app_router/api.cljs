
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v0.3.6
; Compatibility: x4.2.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.api
    (:require [x.app-router.route-handler :as route-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-router.route-handler
(def get-current-route-string       route-handler/get-current-route-string)
(def get-current-route-id           route-handler/get-current-route-id)
(def get-current-route-path         route-handler/get-current-route-path)
(def get-current-route-template     route-handler/get-current-route-template)
(def get-current-route-path-params  route-handler/get-current-route-path-params)
(def get-current-route-path-param   route-handler/get-current-route-path-param)
(def get-current-route-query-params route-handler/get-current-route-query-params)
(def get-current-route-query-param  route-handler/get-current-route-query-param)
(def get-current-route-fragment     route-handler/get-current-route-fragment)
(def get-freezed-route-string       route-handler/get-freezed-route-string)
(def get-freezed-route-id           route-handler/get-freezed-route-id)
(def get-freezed-route-path         route-handler/get-freezed-route-path)
(def get-freezed-route-template     route-handler/get-freezed-route-template)
(def get-freezed-route-path-params  route-handler/get-freezed-route-path-params)
(def get-freezed-route-path-param   route-handler/get-freezed-route-path-param)
(def get-freezed-route-query-params route-handler/get-freezed-route-query-params)
(def get-freezed-route-query-param  route-handler/get-freezed-route-query-param)
(def get-freezed-route-fragment     route-handler/get-freezed-route-fragment)
(def at-home?                       route-handler/at-home?)
(def get-reserved-app-routes        route-handler/get-reserved-app-routes)
(def get-reserved-server-routes     route-handler/get-reserved-server-routes)
(def get-reserved-routes            route-handler/get-reserved-routes)
(def fragment-marker                route-handler/fragment-marker)
