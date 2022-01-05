

(ns playground.crypto
  (:require
    [x.server-core.api :as a]))

;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-boot [:view-selector/initialize! :crypto {:allowed-view-ids [:main]
                                                        :default-view-id   :main}]})
