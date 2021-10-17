
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v0.4.8
; Compatibility: x4.1.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-db.backup-handler
    (:require [mid-fruits.candy           :refer [param]]
              [mid-fruits.map             :refer [dissoc-in]]
              [mid-fruits.vector          :as vector]
              [x.mid-core.api             :refer [r]]
              [x.mid-db.partition-handler :as partition-handler]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name Store backup-item
;  Biztonsági mentés készítése a kiválasztott adatbázis elemről.
;
; @name Restore backup-item
;  Eredeti adatbázis elem visszaállítása, a biztonsági mentés alapján.
;
; @name Remove backup-item
;  A kiválaszott adatbázis elemről készült biztonsági mentés törlése.
;
; @name Resolve backup-item
;  A biztonsági mentés alapján eldönti, hogy megváltozott-e a kiválasztott
;  adatbázis elem és meghívja az on-changed vagy on-unchanged eseményt.



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.partition-handler
(def data-item-path partition-handler/data-item-path)



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-path->backup-item-path
  ; @param (item-path vector) item-path
  ;
  ; @example
  ;  (db/item-path->backup-item-path [:my :item :path])
  ;  => [::backups :data-items :my :item :path]
  ;
  ; @return (data-item-path vector)
  [item-path]
  (vector/concat-items (data-item-path ::backups)
                       (param item-path)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-backup-item
  ; @param (item-path vector) item-path
  ;
  ; @return (*)
  [db [_ item-path]]
  (get-in db (item-path->backup-item-path item-path)))

(defn item-changed?
  ; @param (item-path vector) item-path
  ;
  ; @return (boolean)
  [db [_ item-path]]
  (not= (get-in db item-path)
        (r get-backup-item db item-path)))

(defn item-unchanged?
  ; @param (item-path vector) item-path
  ;
  ; @return (boolean)
  [db [_ item-path]]
  (let [item-changed? (r item-changed? db item-path)]
       (not item-changed?)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-backup-item!
  ; @param (item-path vector) item-path
  ;
  ; @return (map)
  [db [_ item-path]]
  (assoc-in db (item-path->backup-item-path item-path)
               (get-in db item-path)))

(defn restore-backup-item!
  ; @param (item-path vector) item-path
  ;
  ; @return (map)
  [db [_ item-path]]
  (assoc-in db item-path (r get-backup-item db item-path)))

(defn remove-backup-item!
  ; @param (item-path vector) item-path
  ;
  ; @return (map)
  [db [_ item-path]]
  (dissoc-in db (item-path->backup-item-path item-path)))
