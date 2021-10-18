
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.02.14
; Description:
; Version: v0.2.8
; Compatibility: x4.4.1



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.login-handler
    (:require [mid-fruits.vector            :as vector]
              [x.app-core.event-handler     :as event-handler :refer [r]]
              [x.app-core.lifecycle-handler :as lifecycle-handler]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :x.app-core/login-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-n (r lifecycle-handler/get-period-events db :on-login)}))
