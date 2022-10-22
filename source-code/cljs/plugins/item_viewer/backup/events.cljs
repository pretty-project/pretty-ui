
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.backup.events
    (:require [plugins.plugin-handler.backup.events :as backup.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.backup.events
(def backup-current-item! backup.events/backup-current-item!)
