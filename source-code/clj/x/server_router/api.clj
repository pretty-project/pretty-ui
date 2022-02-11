
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.16
; Description:
; Version: v0.3.8
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.api
    (:require [x.server-router.system-routes]
              [x.server-router.default-handler :as default-handler]
              [x.server-router.route-handler   :as route-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-router.default-handler
(def get-default-routes default-handler/get-default-routes)
(def get-default-route  default-handler/get-default-route)
(def set-default-route! default-handler/set-default-route!)

; x.server-router.route-handler
(def request->route-prop route-handler/request->route-prop)
(def get-app-home        route-handler/get-app-home)
(def get-resolved-uri    route-handler/get-resolved-uri)
(def get-client-routes   route-handler/get-client-routes)
(def get-server-routes   route-handler/get-server-routes)
(def get-ordered-routes  route-handler/get-ordered-routes)
(def add-routes!         route-handler/add-routes!)
(def add-route!          route-handler/add-route!)
