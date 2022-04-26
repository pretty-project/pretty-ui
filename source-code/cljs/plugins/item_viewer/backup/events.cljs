
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.backup.events
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
  ; @param (keyword) viewer-id
  ;
  ; @return (map)
  [db [_ viewer-id]]
  ; - Az egyes elemek aktuális változatáról készített másolatok az elem azonosítójával vannak
  ;   tárolva. Így egy időben több elemről is lehetséges másolatot tárolni.
  ;
  ; - A gyors egymás utánban kitörölt elemek törlésének visszavonhatósága időbeni átfedésbe
  ;   kerülhet egymással – egyszerre több értesítés jelenhet meg, különböző elemek törlésének
  ;   visszavonásának lehetőségével – amiért szükséges az egyes elemekről készült másolatokat
  ;   azonosítóval megkülönböztetve kezelni és tárolni.
  (let [current-item-id (r core.subs/get-current-item-id db viewer-id)
        current-item    (r core.subs/get-current-item    db viewer-id)]
       (assoc-in db [:plugins :plugin-handler/backup-items viewer-id current-item-id] current-item)))

(defn clean-recovery-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ viewer-id item-id]]
  ; Ha a clean-recovery-data! függvény alkalmazásakor ismételten ugyanaz az elem van megnyitva
  ; megtekintésre, akkor a függvény nem végez műveletet.
  ; Pl.: A felhasználó a :plugins.item-editor/changes-discarded-dialog értesítésen
  ;      a "Visszaállítás" lehetőséget választja és a szerkesztő {:recovery-mode? true}
  ;      beállítással megnyitja ugyanazt az elemet szerkesztésre, mielőtt
  ;      az [:item-editor/clean-recovery-data! ...] esemény megtörténne.
  ; Pl.: A felhasználó újra megnyitja ugyanazt az elemet szerkesztésre, mielőtt
  ;      az [:item-editor/clean-recovery-data! ...] esemény megtörténne.
  ;(if-let [editing-item? (r core.subs/editing-item? db editor-id item-id)]
  ;        (return db)
  ;        (->     db (dissoc-in [:plugins :plugin-handler/backup-items  editor-id item-id])
  ;                   (dissoc-in [:plugins :plugin-handler/local-changes editor-id item-id])]])
  db)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-editor/clean-recovery-data! clean-recovery-data!)
