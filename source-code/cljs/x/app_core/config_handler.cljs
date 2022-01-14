
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.16
; Description:
; Version: v0.8.0
; Compatibility: x4.5.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.config-handler
    (:require [x.app-core.event-handler  :as event-handler]
              [x.mid-core.config-handler :as config-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.config-handler
(def get-app-config      config-handler/get-app-config)
(def get-app-config-item config-handler/get-app-config-item)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-sub :core/get-app-config      get-app-config)
(event-handler/reg-sub :core/get-app-config-item get-app-config-item)
