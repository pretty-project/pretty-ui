
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.settings.view-selector.engine
    (:require [server-plugins.view-selector.api]
              [x.server-core.api :as a]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keywords in vector)
(def ALLOWED-VIEW-IDS [:personal :privacy :appearance :notifications])

; @constant (keywords)
(def DEFAULT-VIEW-ID :personal)
