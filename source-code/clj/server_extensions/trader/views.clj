
(ns server-extensions.trader.views
    (:require [server-plugins.view-selector.api]
              [x.server-core.api :as a]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:view-selector/initialize! :trader {}]})
