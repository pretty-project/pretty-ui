
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-db.backup-handler.events
    (:require [mid-fruits.map               :refer [dissoc-in]]
              [re-frame.api                 :as r :refer [r]]
              [x.mid-db.backup-handler.subs :as backup-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-backup-item!
  ; @param (vector) item-path
  ;
  ; @usage
  ;  (r store-backup-item! db [:my-item])
  ;
  ; @return (map)
  [db [_ item-path]]
  (assoc-in db [:db :backup-handler/backup-items item-path]
               (get-in db item-path)))

(defn restore-backup-item!
  ; @param (vector) item-path
  ;
  ; @usage
  ;  (r restore-backup-item! db [:my-item])
  ;
  ; @return (map)
  [db [_ item-path]]
  (assoc-in db item-path (r backup-handler.subs/get-backup-item db item-path)))

(defn remove-backup-item!
  ; @param (vector) item-path
  ;
  ; @usage
  ;  (r remove-backup-item! db [:my-item])
  ;
  ; @return (map)
  [db [_ item-path]]
  (dissoc-in db [:db :backup-handler/backup-items item-path]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:db/store-backup-item! [:my-item]]
(r/reg-event-db :db/store-backup-item! store-backup-item!)

; @usage
;  [:db/restore-backup-item! [:my-item]]
(r/reg-event-db :db/restore-backup-item! restore-backup-item!)

; @usage
;  [:db/remove-backup-item! [:my-item]]
(r/reg-event-db :db/remove-backup-item! remove-backup-item!)
