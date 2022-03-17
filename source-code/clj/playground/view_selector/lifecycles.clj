
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns playground.view-selector.lifecycles
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:view-selector/init-selector! :playground {:on-load [:playground.view-selector/load-selector!]
                                                               :route-template "/@app-home/playground/:view-id"}]})
