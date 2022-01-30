
(ns server-extensions.settings.view-selector
    (:require [server-plugins.view-selector.api]
              [x.server-core.api :as a]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keywords in vector)
(def ALLOWED-VIEW-IDS [:personal :privacy :appearance :notifications])

; @constant (keywords)
(def DEFAULT-VIEW-ID :personal)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-boot [:view-selector/initialize-selector! :settings
                                                        {:allowed-view-ids ALLOWED-VIEW-IDS
                                                         :default-view-id  DEFAULT-VIEW-ID}]})
