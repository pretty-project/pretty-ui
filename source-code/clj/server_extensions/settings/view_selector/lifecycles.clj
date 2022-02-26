
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.settings.view-selector.lifecycles
    (:require [server-plugins.view-selector.api]
              [x.server-core.api :as a]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:view-selector/initialize-selector! :settings
                                                        {:allowed-view-ids [:personal :privacy :appearance :notifications]
                                                         :default-view-id   :personal}]})
