
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.backup.events
    (:require [mid-fruits.candy                     :refer [return]]
              [mid-fruits.map                       :as map :refer [dissoc-in]]
              [plugins.item-editor.backup.subs      :as backup.subs]
              [plugins.item-editor.body.subs        :as body.subs]
              [plugins.item-editor.core.subs        :as core.subs]
              [plugins.plugin-handler.backup.events :as backup.events]
              [re-frame.api                         :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.backup.events
(def backup-current-item! backup.events/backup-current-item!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-current-item-changes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  ; A store-current-item-changes! eltárolja az elem aktuális állapota és az elemről utoljára
  ; készült másolat közötti különbséget.
  ; Pl.: Ha az el nem mentett változtatásokat tartalmazó szerkesztő elhagyásakor megjelenő
  ;      értesítésen, a felhasználó a "Visszaállítás (restore)" gombra kattint, akkor szükséges
  ;      a szerkesztő elindulása után az elem állapotát visszaállítani úgy, hogy
  ;      a "Visszaállítás (revert)" gomb használatával az elem az előző szerkesztés megnyitáskori
  ;      állapotára is visszaállítható legyen.
  ;      Ehhez szükséges az elemről a letöltéskor készült másolatot megőrizni és kilépéskor
  ;      eltárolni a másolat és az aktuális állapot közötti különbséget.
  (let [current-item-id (r core.subs/get-current-item-id db plugin-id)
        current-item    (r core.subs/get-current-item    db plugin-id)
        backup-item     (r backup.subs/get-backup-item   db plugin-id current-item-id)
        item-changes    (map/difference current-item backup-item)]
       (assoc-in db [:plugins :plugin-handler/item-changes plugin-id current-item-id] item-changes)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clean-recovery-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ editor-id item-id]]
  ; Ha a clean-recovery-data! függvény alkalmazásakor ismételten ugyanaz az elem van megnyitva
  ; szerkesztésre, akkor a clean-recovery-data! függvény nem végez műveletet.
  ; Pl.: A felhasználó a :plugins.item-editor/changes-discarded-dialog értesítésen
  ;      a "Visszaállítás" lehetőséget választja és a szerkesztő {:recovery-mode? true}
  ;      beállítással megnyitja ugyanazt az elemet szerkesztésre, mielőtt
  ;      az [:item-editor/clean-recovery-data! ...] esemény megtörténne.
  ; Pl.: A felhasználó újra megnyitja ugyanazt az elemet szerkesztésre, mielőtt
  ;      az [:item-editor/clean-recovery-data! ...] esemény megtörténne.
  (if-let [editing-item? (r core.subs/editing-item? db editor-id item-id)]
          (return db)
          (-> db (dissoc-in [:plugins :plugin-handler/backup-items editor-id item-id])
                 (dissoc-in [:plugins :plugin-handler/item-changes editor-id item-id]))))

(defn recover-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; A {:recovery-mode? true} állapotban elinduló szerkesztő ...
  (let [item-path       (r body.subs/get-body-prop       db editor-id :item-path)
        current-item-id (r core.subs/get-current-item-id db editor-id)
        backup-item     (r backup.subs/get-backup-item   db editor-id current-item-id)
        item-changes    (r backup.subs/get-item-changes  db editor-id current-item-id)]
       (-> db (assoc-in  item-path backup-item)
              (update-in item-path merge item-changes))))

(defn revert-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; A revert-item! függvény visszaállítja az aktuálisan szerkesztett elemet a megnyitáskori
  ; állapotára az elem letöltésekor eltárolt másolat alapján.
  (let [item-path       (r body.subs/get-body-prop       db editor-id :item-path)
        current-item-id (r core.subs/get-current-item-id db editor-id)
        backup-item     (r backup.subs/get-backup-item   db editor-id current-item-id)]
       (assoc-in db item-path backup-item)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :item-editor/clean-recovery-data! clean-recovery-data!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :item-editor/revert-item! revert-item!)
