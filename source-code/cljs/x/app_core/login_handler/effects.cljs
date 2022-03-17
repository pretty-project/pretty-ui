
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.login-handler.effects
    (:require [x.app-core.event-handler          :as event-handler :refer [r]]
              [x.app-core.lifecycle-handler.subs :as lifecycle-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :core/login-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-n (r lifecycle-handler.subs/get-period-events db :on-login)}))