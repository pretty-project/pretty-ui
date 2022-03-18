
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.connection-handler.side-effects
    (:require [dom.api                                      :as dom]
              [x.app-core.api                               :as a]
              [x.app-environment.connection-handler.helpers :as connection-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn listen-to-connection-change!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (do (dom/add-event-listener!  "online" connection-handler.helpers/connection-change-listener)
      (dom/add-event-listener! "offline" connection-handler.helpers/connection-change-listener)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :environment/listen-to-connection-change! listen-to-connection-change!)
