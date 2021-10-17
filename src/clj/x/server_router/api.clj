
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.16
; Description:
; Version: v0.2.8
; Compatibility: x4.2.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.api
    (:require [x.server-router.default-handler]
              [x.server-router.system-routes]
              [x.server-router.route-handler :as route-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-router.route-handler
(def get-reserved-routes route-handler/get-reserved-routes)
(def add-routes!         route-handler/add-routes!)
(def add-route!          route-handler/add-route!)
