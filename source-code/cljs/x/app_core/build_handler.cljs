
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.22
; Description:
; Version: v0.3.6
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.build-handler
    (:require [x.mid-core.build-handler :as build-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.build-handler
(def app-build        build-handler/app-build)
(def get-app-build    build-handler/get-app-build)
(def store-app-build! build-handler/store-app-build!)
