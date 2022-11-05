
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.backup.events
    (:require [engines.engine-handler.backup.events :as backup.events]
              [engines.item-handler.backup.subs     :as backup.subs]
              [engines.item-handler.body.subs       :as body.subs]
              [engines.item-handler.core.subs       :as core.subs]
              [mid-fruits.candy                     :refer [return]]
              [mid-fruits.map                       :as map :refer [dissoc-in]]
              [re-frame.api                         :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.backup.events
(def backup-current-item! backup.events/backup-current-item!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-current-item-changes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  ; A store-current-item-changes! eltárolja az elem aktuális állapota és az elemről utoljára
  ; készült másolat közötti különbséget.
  ; Pl.: Ha az el nem mentett változtatásokat tartalmazó szerkesztő elhagyásakor megjelenő
  ;      értesítésen, a felhasználó a "Visszaállítás (restore)" gombra kattint, akkor szükséges
  ;      a szerkesztő elindulása után az elem állapotát visszaállítani úgy, hogy
  ;      a "Visszaállítás (revert)" gomb használatával az elem az előző szerkesztés megnyitáskori
  ;      állapotára is visszaállítható legyen.
  ;      Ehhez szükséges az elemről a letöltéskor készült másolatot megőrizni és kilépéskor
  ;      eltárolni a másolat és az aktuális állapot közötti különbséget.
  (let [current-item-id (r core.subs/get-current-item-id db engine-id)
        current-item    (r core.subs/get-current-item    db engine-id)
        backup-item     (r backup.subs/get-backup-item   db engine-id current-item-id)
        item-changes    (map/difference current-item backup-item)]
       (assoc-in db [:engines :engine-handler/item-changes engine-id current-item-id] item-changes)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clean-recovery-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ handler-id item-id]]
  ; Ha a clean-recovery-data! függvény alkalmazásakor ismételten ugyanaz az elem van megnyitva
  ; szerkesztésre, akkor a clean-recovery-data! függvény nem végez műveletet.
  ; Pl.: A felhasználó a :engines.item-handler/changes-discarded-dialog értesítésen
  ;      a "Visszaállítás" lehetőséget választja és a szerkesztő {:recovery-mode? true}
  ;      beállítással megnyitja ugyanazt az elemet szerkesztésre, mielőtt
  ;      az [:item-handler/clean-recovery-data! ...] esemény megtörténne.
  ; Pl.: A felhasználó újra megnyitja ugyanazt az elemet szerkesztésre, mielőtt
  ;      az [:item-handler/clean-recovery-data! ...] esemény megtörténne.
  (if-let [editing-item? (r core.subs/editing-item? db handler-id item-id)]
          (return db)
          (-> db (dissoc-in [:engines :engine-handler/backup-items handler-id item-id])
                 (dissoc-in [:engines :engine-handler/item-changes handler-id item-id]))))

(defn recover-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  [db [_ handler-id]]
  ; A {:recovery-mode? true} állapotban elinduló szerkesztő ...
  (let [item-path       (r body.subs/get-body-prop       db handler-id :item-path)
        current-item-id (r core.subs/get-current-item-id db handler-id)
        backup-item     (r backup.subs/get-backup-item   db handler-id current-item-id)
        item-changes    (r backup.subs/get-item-changes  db handler-id current-item-id)]
       (-> db (assoc-in  item-path backup-item)
              (update-in item-path merge item-changes))))

(defn revert-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  [db [_ handler-id]]
  ; A revert-item! függvény visszaállítja az aktuálisan szerkesztett elemet a megnyitáskori
  ; állapotára az elem letöltésekor eltárolt másolat alapján.
  (let [item-path       (r body.subs/get-body-prop       db handler-id :item-path)
        current-item-id (r core.subs/get-current-item-id db handler-id)
        backup-item     (r backup.subs/get-backup-item   db handler-id current-item-id)]
       (assoc-in db item-path backup-item)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :item-handler/clean-recovery-data! clean-recovery-data!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :item-handler/revert-item! revert-item!)
