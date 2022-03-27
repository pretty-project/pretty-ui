
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.settings.view-selector.lifecycles
    (:require [plugins.view-selector.api]
              [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:view-selector/init-selector! :settings.view-selector
                                                  {:on-route       [:settings.view-selector/load-selector!]
                                                   :route-template "/@app-home/settings/:view-id"
                                                   :route-title    :settings}]})
                                                   
