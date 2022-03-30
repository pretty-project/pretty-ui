
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.backup.events
    (:require [mid-fruits.map :as map]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clean-backup-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (strings in vector) item-ids
  ;
  ; @return (map)
  [db [_ plugin-id item-ids]]
  ; Ha az egyes elemeken végzett műveletek (pl. törlés) visszavonására rendelkezésre álló lejárt,
  ; vagy megtörtént az elemek helyreállítása és már nincs szükség az elemekről készült biztonsági
  ; másolatokra ...
  (update-in db [:plugins :plugin-handler/backup-items plugin-id] map/remove-keys item-ids))
