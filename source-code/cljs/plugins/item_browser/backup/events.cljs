
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.backup.events
    (:require [plugins.plugin-handler.backup.events :as backup.events]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.backup.events
(def backup-item! backup.events/backup-item!)
