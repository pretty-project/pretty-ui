
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.02.14
; Description:
; Version: v0.2.4
; Compatibility: x3.9.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.login-handler
    (:require [x.app-core.api     :as a :refer [r]]
              [mid-fruits.vector  :as vector]
              [x.app-core.lifecycle-handler :as lifecycle-handler]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-core/login-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-n (r lifecycle-handler/get-period-events db :on-login)}))
