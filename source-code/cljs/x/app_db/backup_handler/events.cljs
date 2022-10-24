
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-db.backup-handler.events
    (:require [x.mid-db.backup-handler.events :as backup-handler.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.backup-handler.events
(def store-backup-item!   backup-handler.events/store-backup-item!)
(def restore-backup-item! backup-handler.events/restore-backup-item!)
(def remove-backup-item!  backup-handler.events/remove-backup-item!)
