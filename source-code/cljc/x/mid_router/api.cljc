
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.17
; Description:
; Version: v0.1.6
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-router.api
    (:require [x.mid-router.engine        :as engine]
              [x.mid-router.route-handler :as route-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-router.engine
(def valid-route-path engine/valid-route-path)

; x.mid-router.route-handler
(def get-app-home route-handler/get-app-home)
