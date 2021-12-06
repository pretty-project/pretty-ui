
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.16
; Description:
; Version: v0.4.8
; Compatibility: x4.3.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.config-handler
    (:require [x.app-core.event-handler  :as event-handler]
              [x.mid-core.config-handler :as config-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.config-handler
(def app-detail-path     config-handler/app-detail-path)
(def storage-detail-path config-handler/storage-detail-path)
(def get-app-details     config-handler/get-app-details)
(def get-app-detail      config-handler/get-app-detail)
(def get-storage-details config-handler/get-storage-details)
(def get-storage-detail  config-handler/get-storage-detail)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-sub :core/get-app-details get-app-details)
(event-handler/reg-sub :core/get-app-detail  get-app-detail)
