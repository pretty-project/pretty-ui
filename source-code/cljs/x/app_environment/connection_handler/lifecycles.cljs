
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.connection-handler.lifecycles
    (:require [x.app-core.api :as a]))

  

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-app-init {:fx       [:environment/listen-to-connection-change!]
                 :dispatch [:environment/connection-changed]}})
