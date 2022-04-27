
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.backup.events
    (:require [plugins.plugin-handler.backup.events :as backup.events]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.backup.events
(def backup-current-item! backup.events/backup-current-item!)
