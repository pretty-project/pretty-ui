
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.backup.events
    (:require [candy.api                            :refer [return]]
              [engines.engine-handler.backup.events :as backup.events]
              [engines.item-editor.backup.subs      :as backup.subs]
              [engines.item-editor.body.subs        :as body.subs]
              [engines.item-editor.core.subs        :as core.subs]
              [map.api                              :as map :refer [dissoc-in]]
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
  ;     értesítésen, a felhasználó a "Visszaállítás (restore)" gombra kattint, akkor szükséges
  ;     a szerkesztő elindulása után az elem állapotát visszaállítani úgy, hogy
  ;     a "Visszaállítás (revert)" gomb használatával az elem az előző szerkesztés megnyitáskori
  ;     állapotára is visszaállítható legyen.
  ;     Ehhez szükséges az elemről a letöltéskor készült másolatot megőrizni és kilépéskor
  ;     eltárolni a másolat és az aktuális állapot közötti különbséget.
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
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ editor-id item-id]]
  ; Ha a clean-recovery-data! függvény alkalmazásakor ismételten ugyanaz az elem van megnyitva
  ; szerkesztésre, akkor a clean-recovery-data! függvény nem végez műveletet.
  ; Pl.: A felhasználó a :engines.item-editor/changes-discarded-dialog értesítésen
  ;     a "Visszaállítás" lehetőséget választja és a szerkesztő {:recovery-mode? true}
  ;     beállítással megnyitja ugyanazt az elemet szerkesztésre, mielőtt
  ;     az [:item-editor/clean-recovery-data! ...] esemény megtörténne.
  ; Pl.: A felhasználó újra megnyitja ugyanazt az elemet szerkesztésre, mielőtt
  ;     az [:item-editor/clean-recovery-data! ...] esemény megtörténne.
  (if-let [editing-item? (r core.subs/editing-item? db editor-id item-id)]
          (return db)
          (-> db (dissoc-in [:engines :engine-handler/backup-items editor-id item-id])
                 (dissoc-in [:engines :engine-handler/item-changes editor-id item-id]))))

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
