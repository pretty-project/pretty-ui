
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.db.backup-handler.events
    (:require [map.api                      :refer [dissoc-in]]
              [mid.x.db.backup-handler.subs :as backup-handler.subs]
              [re-frame.api                 :as r :refer [r]]))



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
;  [:x.db/store-backup-item! [:my-item]]
(r/reg-event-db :x.db/store-backup-item! store-backup-item!)

; @usage
;  [:x.db/restore-backup-item! [:my-item]]
(r/reg-event-db :x.db/restore-backup-item! restore-backup-item!)

; @usage
;  [:x.db/remove-backup-item! [:my-item]]
(r/reg-event-db :x.db/remove-backup-item! remove-backup-item!)
