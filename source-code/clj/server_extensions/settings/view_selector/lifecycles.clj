
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.settings.view-selector.lifecycles
    (:require [server-plugins.view-selector.api]
              [x.server-core.api :as a]
              [server-extensions.settings.view-selector.engine :as view-selector.engine]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:view-selector/initialize-selector! :settings
                                                        {:allowed-view-ids view-selector.engine/ALLOWED-VIEW-IDS
                                                         :default-view-id  view-selector.engine/DEFAULT-VIEW-ID}]})
