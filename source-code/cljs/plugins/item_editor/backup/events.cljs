
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.backup.events
    (:require [mid-fruits.candy                :refer [return]]
              [mid-fruits.map                  :as map :refer [dissoc-in]]
              [plugins.item-editor.backup.subs :as backup.subs]
              [plugins.item-editor.body.subs   :as body.subs]
              [plugins.item-editor.core.subs   :as core.subs]
              [x.app-core.api                  :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn backup-current-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; - Az egyes elemek aktuális változatáról készített másolatok az elem azonosítójával vannak
  ;   tárolva. Így egy időben több elemről is lehetséges másolatot tárolni.
  ;
  ; - A gyors egymás utánban kitörölt elemek törlésének visszavonhatósága időbeni átfedésbe
  ;   kerülhet egymással – egyszerre több értesítés jelenhet meg, különböző elemek törlésének
  ;   visszavonásának lehetőségével – amiért szükséges az egyes elemekről készült másolatokat
  ;   azonosítóval megkülönböztetve kezelni és tárolni.
  ;
  ; - A gyors egymás utánban elvetett szerkesztett elemek elvetésének visszavonhatósága időbeni
  ;   átfedésbe kerülhet egymással ...
  (let [current-item-id (r core.subs/get-current-item-id db editor-id)
        current-item    (r core.subs/get-current-item    db editor-id)]
       (assoc-in db [:plugins :plugin-handler/backup-items editor-id current-item-id] current-item)))

(defn store-local-changes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; - A szerkesztő elhagyásakor az elem el nem mentett változtatásainak helyreállításához szükséges
  ;   eltárolni az elem ... változtatásait.
  ;
  ; - Az elem törlésének visszaállítása után újra megnyíló szerkesztő a visszaállított elemen
  ;   alkalmazza annak (törléskori) el nem mentett változtatásait.
  (let [current-item-id (r core.subs/get-current-item-id db editor-id)
        current-item    (r core.subs/get-current-item    db editor-id)
        backup-item     (r backup.subs/get-backup-item   db editor-id current-item-id)
        local-changes   (map/difference current-item backup-item)]
       (assoc-in db [:plugins :plugin-handler/local-changes editor-id current-item-id] local-changes)))

(defn clean-recovery-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ editor-id item-id]]
  ; Ha a clean-recovery-data! függvény alkalmazásakor ismételten ugyanaz az elem van megnyitva
  ; szerkesztésre, akkor a függvény nem végez műveletet.
  ; Pl.: A felhasználó a :plugins.item-editor/changes-discarded-dialog értesítésen
  ;      a "Visszaállítás" lehetőséget választja és a szerkesztő {:recovery-mode? true}
  ;      beállítással megnyitja ugyanazt az elemet szerkesztésre, mielőtt
  ;      az [:item-editor/clean-recovery-data! ...] esemény megtörténne.
  ; Pl.: A felhasználó újra megnyitja ugyanazt az elemet szerkesztésre, mielőtt
  ;      az [:item-editor/clean-recovery-data! ...] esemény megtörténne.
  (if-let [editing-item? (r core.subs/editing-item? db editor-id item-id)]
          (return db)
          (->     db (dissoc-in [:plugins :plugin-handler/backup-items  editor-id item-id])
                     (dissoc-in [:plugins :plugin-handler/local-changes editor-id item-id]))))

(defn recover-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [item-path       (r body.subs/get-body-prop       db editor-id :item-path)
        current-item-id (r core.subs/get-current-item-id db editor-id)]))
        
        ; Mivel nincs már törlés az item-editorban ezért nem kell külön tárolni a megnyitáskori
        ; állapotot (backup-item) és a kilépéskori állapotot (local-changes) elegendő a kilépéskor
        ; késziíteni egy backup-item-et amivel vissza lehet állitani a discard-changes esmeeénnyel.

        ;local-changes   (r backup.subs/get-local-changes db editor-id current-item-id)]
       ;(-> db (update-in item-path merge local-changes)
        ;      (dissoc-in [:plugins :plugin-handler/local-changes editor-id current-item-id])]))

(defn revert-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [item-path       (r body.subs/get-body-prop       db editor-id :item-path)
        current-item-id (r core.subs/get-current-item-id db editor-id)
        backup-item     (r backup.subs/get-backup-item   db editor-id current-item-id)]
       (assoc-in db item-path backup-item)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-editor/clean-recovery-data! clean-recovery-data!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-editor/revert-item! revert-item!)
