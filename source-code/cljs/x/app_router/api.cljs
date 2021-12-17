
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v0.4.2
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.api
    (:require [x.app-router.engine        :as engine]
              [x.app-router.route-handler :as route-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-router.engine
(def valid-route-path engine/valid-route-path)

; x.app-router.route-handler
(def get-app-home                   route-handler/get-app-home)
(def get-routes                     route-handler/get-routes)
(def get-current-route-string       route-handler/get-current-route-string)
(def get-current-route-id           route-handler/get-current-route-id)
(def get-current-route-path         route-handler/get-current-route-path)
(def get-current-route-template     route-handler/get-current-route-template)
(def get-current-route-path-params  route-handler/get-current-route-path-params)
(def get-current-route-path-param   route-handler/get-current-route-path-param)
(def get-current-route-query-params route-handler/get-current-route-query-params)
(def get-current-route-query-param  route-handler/get-current-route-query-param)
(def get-current-route-fragment     route-handler/get-current-route-fragment)
(def get-current-route-parent       route-handler/get-current-route-parent)
(def current-route-path-param?      route-handler/current-route-path-param?)
(def at-home?                       route-handler/at-home?)
