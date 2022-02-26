
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns playground.view-selector.lifecycles
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:view-selector/initialize-selector! :playground {:default-view-id :anchors}]})
