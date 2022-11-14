
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.db.backup-handler.events
    (:require [mid.x.db.backup-handler.events :as backup-handler.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.db.backup-handler.events
(def store-backup-item!   backup-handler.events/store-backup-item!)
(def restore-backup-item! backup-handler.events/restore-backup-item!)
(def remove-backup-item!  backup-handler.events/remove-backup-item!)
