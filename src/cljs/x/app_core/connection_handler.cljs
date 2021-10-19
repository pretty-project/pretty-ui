
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.20
; Description:
; Version: v0.3.4
; Compatibility: x4.4.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.connection-handler
    (:require [mid-fruits.vector            :as vector]
              [x.app-core.event-handler     :as event-handler :refer [r]]
              [x.app-core.lifecycle-handler :as lifecycle-handler]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :x.app-core/connect-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-n (r lifecycle-handler/get-period-events db :on-browser-online)}))

(event-handler/reg-event-fx
  :x.app-core/disconnect-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-n (r lifecycle-handler/get-period-events db :on-browser-offline)}))
