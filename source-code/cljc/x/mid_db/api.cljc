
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-db.api
    (:require [x.mid-db.backup-handler.effects]
              [x.mid-db.backup-handler.events :as backup-handler.events]
              [x.mid-db.backup-handler.subs   :as backup-handler.subs]
              [x.mid-db.core.events           :as core.events]
              [x.mid-db.core.subs             :as core.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.backup-handler.events
(def store-backup-item!   backup-handler.events/store-backup-item!)
(def restore-backup-item! backup-handler.events/restore-backup-item!)
(def remove-backup-item!  backup-handler.events/remove-backup-item!)

; x.mid-db.backup-handler.subs
(def get-backup-item backup-handler.subs/get-backup-item)
(def item-changed?   backup-handler.subs/item-changed?)
(def item-unchanged? backup-handler.subs/item-unchanged?)

; x.mid-db.core.events
(def empty-db!           core.events/empty-db!)
(def toggle-item!        core.events/toggle-item!)
(def toggle-item-value!  core.events/toggle-item-value!)
(def copy-item!          core.events/copy-item!)
(def move-item!          core.events/move-item!)
(def set-item!           core.events/set-item!)
(def set-vector-item!    core.events/set-vector-item!)
(def remove-item!        core.events/remove-item!)
(def remove-vector-item! core.events/remove-vector-item!)
(def remove-item-n!      core.events/remove-item-n!)
(def inc-item-n!         core.events/inc-item-n!)
(def dec-item-n!         core.events/dec-item-n!)
(def apply-item!         core.events/apply-item!)

; x.mid-db.core.subs
(def subscribe-item   core.subs/subscribe-item)
(def subscribed-item  core.subs/subscribed-item)
(def get-db           core.subs/get-db)
(def get-item         core.subs/get-item)
(def item-exists?     core.subs/item-exists?)
(def get-item-count   core.subs/get-item-count)
(def get-applied-item core.subs/get-applied-item)
