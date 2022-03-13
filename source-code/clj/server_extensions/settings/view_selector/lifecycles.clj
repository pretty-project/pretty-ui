
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.settings.view-selector.lifecycles
    (:require [plugins.view-selector.api]
              [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:view-selector/init-selector! :settings
                                                  {:allowed-view-ids [:personal :privacy :appearance :notifications]
                                                   :default-view-id   :personal}]})
