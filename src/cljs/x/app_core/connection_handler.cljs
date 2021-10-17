
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.20
; Description:
; Version: v0.2.8
; Compatibility: x4.2.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.connection-handler
    (:require [x.app-core.api     :as a :refer [r]]
              [mid-fruits.vector  :as vector]
              [x.app-core.lifecycle-handler :as lifecycle-handler]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-core/connect-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-n (r lifecycle-handler/get-period-events db :on-browser-online)}))

(a/reg-event-fx
  :x.app-core/disconnect-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-n (r lifecycle-handler/get-period-events db :on-browser-offline)}))
