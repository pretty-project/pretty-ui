
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.16
; Description:
; Version: v0.5.0
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.config-handler
    (:require [x.app-core.event-handler  :as event-handler]
              [x.mid-core.config-handler :as config-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.config-handler
(def get-app-details config-handler/get-app-details)
(def get-app-detail  config-handler/get-app-detail)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-sub :core/get-app-details get-app-details)
(event-handler/reg-sub :core/get-app-detail  get-app-detail)
