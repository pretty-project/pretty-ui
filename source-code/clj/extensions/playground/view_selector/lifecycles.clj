
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.playground.view-selector.lifecycles
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:view-selector/init-selector! :playground.view-selector
                                                  {:on-route       [:playground.view-selector/load-selector!]
                                                   :route-template "/@app-home/playground/:view-id"
                                                   :route-title    "Playground"}]})
