
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns settings.view-selector.lifecycles
    (:require [plugins.view-selector.api]
              [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:view-selector/init-selector! :settings.view-selector
                                                  {:base-route  "/@app-home/settings"
                                                   :on-route    [:settings.view-selector/load-selector!]
                                                   :route-title :settings}]})
