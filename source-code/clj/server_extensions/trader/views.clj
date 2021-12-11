
(ns server-extensions.trader.views
    (:require [server-plugins.view-selector.api]
              [x.server-core.api :as a]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def DEFAULT-VIEW-ID :overview)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:view-selector/initialize! :trader {:default-view-id DEFAULT-VIEW-ID}]})
