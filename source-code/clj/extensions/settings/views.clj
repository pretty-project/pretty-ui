
(ns extensions.settings.views
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
  {:on-app-boot [:view-selector/initialize! :settings {:allowed-view-ids ALLOWED-VIEW-IDS
                                                       :default-view-id  DEFAULT-VIEW-ID}]})
