
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns playground.view-selector.lifecycles
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:view-selector/init-selector! :playground.view-selector
                                                  {:base-route  "/@app-home/playground"
                                                   :on-route    [:playground.view-selector/load-selector!]
                                                   :route-title "Playground"}]})
