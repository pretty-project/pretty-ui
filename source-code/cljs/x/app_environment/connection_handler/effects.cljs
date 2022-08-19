
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.connection-handler.effects
    (:require [app-fruits.window :as window]
              [x.app-core.api    :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :environment/connection-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [browser-online? (window/browser-online?)]
           {:db          (assoc-in db [:environment :connection-handler/meta-items :browser-online?] browser-online?)
            :dispatch-if [browser-online? [:core/connect-app!]
                                          [:core/disconnect-app!]]})))
