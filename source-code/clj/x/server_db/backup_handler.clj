
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v0.4.0
; Compatibility: x4.4.6



;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-db.backup-handler
    (:require [re-frame.api            :as r :refer [r]]
              [x.mid-db.backup-handler :as backup-handler]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.backup-handler
(def item-path->backup-item-path backup-handler/item-path->backup-item-path)
(def get-backup-item             backup-handler/get-backup-item)
(def item-changed?               backup-handler/item-changed?)
(def item-unchanged?             backup-handler/item-unchanged?)
(def store-backup-item!          backup-handler/store-backup-item!)
(def restore-backup-item!        backup-handler/restore-backup-item!)
(def remove-backup-item!         backup-handler/remove-backup-item!)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-db :db/store-backup-item!   store-backup-item!)
(r/reg-event-db :db/restore-backup-item! restore-backup-item!)
(r/reg-event-db :db/remove-backup-item!  remove-backup-item!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :db/resolve-backup-item!
  ; @param (vector) item-path
  ; @param (map) events
  ;  {:on-changed   (metamorphic-event)
  ;   :on-unchanged (metamorphic-event)}
  (fn [{:keys [db]} [_ item-path {:keys [:on-changed :on-unchanged]}]]
      {:dispatch-if [(r item-changed? db item-path) on-changed on-unchanged]}))
