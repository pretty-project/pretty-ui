
(ns extensions.settings.views
    (:require [x.server-core.api :as a]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keywords in vector)
(def ALLOWED-VIEWS [:personal :privacy :appearance :notifications])

; @constant (keywords)
(def DEFAULT-VIEW :personal)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:view-selector/initialize! :settings {:allowed-views ALLOWED-VIEWS
                                                       :default-view  DEFAULT-VIEW}]})
