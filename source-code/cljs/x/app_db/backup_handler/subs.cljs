
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-db.backup-handler.subs
    (:require [mid.x.db.backup-handler.subs :as backup-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.db.backup-handler.subs
(def get-backup-item backup-handler.subs/get-backup-item)
(def item-changed?   backup-handler.subs/item-changed?)
(def item-unchanged? backup-handler.subs/item-unchanged?)
