
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.backup.events
    (:require [engines.engine-handler.core.subs  :as core.subs]
              [engines.engine-handler.items.subs :as items.subs]
              [mid-fruits.map                    :as map :refer [dissoc-in]]
              [re-frame.api                      :refer [r]]))



;; -- Current item events -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn backup-current-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  ; - Az egyes elemek aktuális változatáról készített másolatok az elem azonosítójával vannak
  ;   tárolva. Így egy időben több elemről is lehetséges másolatot tárolni.
  ;   Pl.: A gyors egymás utánban kitörölt elemek törlésének visszavonhatósága időbeni átfedésbe
  ;        kerülhet egymással – egyszerre több értesítés jelenhet meg, különböző elemek törlésének
  ;        visszavonásának lehetőségével – amiért szükséges az egyes elemekről készült másolatokat
  ;        azonosítóval megkülönböztetve kezelni és tárolni.
  (let [current-item-id (r core.subs/get-current-item-id db engine-id)
        current-item    (r core.subs/get-current-item    db engine-id)]
       (assoc-in db [:engines :engine-handler/backup-items engine-id current-item-id] current-item)))



;; -- Single item events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn backup-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ engine-id item-id]]
  (let [backup-item (r items.subs/get-item db engine-id item-id)]
       (assoc-in db [:engines :engine-handler/backup-items engine-id item-id] backup-item)))

(defn clean-backup-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ engine-id item-id]]
  (dissoc-in db [:engines :engine-handler/backup-items engine-id item-id]))



;; -- Multiple item events ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clean-backup-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (strings in vector) item-ids
  ;
  ; @return (map)
  [db [_ engine-id item-ids]]
  ; Ha az egyes elemeken végzett műveletek (pl. törlés) visszavonására rendelkezésre álló lejárt,
  ; vagy megtörtént az elemek helyreállítása és már nincs szükség az elemekről készült biztonsági
  ; másolatokra ...
  (update-in db [:engines :engine-handler/backup-items engine-id] map/remove-keys item-ids))
