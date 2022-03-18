
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.backup.events
    (:require [mid-fruits.candy                :refer [return]]
              [mid-fruits.map                  :as map :refer [dissoc-in]]
              [plugins.item-editor.backup.subs :as backup.subs]
              [plugins.item-editor.core.subs   :as core.subs]
              [plugins.item-editor.mount.subs  :as mount.subs]
              [x.app-core.api                  :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn backup-current-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; - Az egyes elemek aktuális változatáról készített másolatok az elem azonosítójával vannak
  ;   tárolva. Így egy időben több elemről is lehetséges másolatot tárolni.
  ; - A gyors egymás utánban kitörölt elemek törlésének visszavonhatósága időbeni átfedésbe
  ;   kerülhet egymással – egyszerre több értesítés jelenhet meg, különböző elemek törlésének
  ;   visszavonásának lehetőségével – amiért szükséges az egyes elemekről készült másolatokat
  ;   azonosítóval megkülönböztetve kezelni és tárolni.
  ; - A gyors egymás utánban elvetett szerkesztett elemek elvetésének visszavonhatósága időbeni
  ;   átfedésbe kerülhet egymással ...
  (let [current-item-id (r core.subs/get-current-item-id db extension-id item-namespace)
        current-item    (r core.subs/get-current-item    db extension-id item-namespace)]
       (assoc-in db [:plugins :item-editor/backup-items extension-id current-item-id] current-item)))

(defn store-local-changes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; - A szerkesztő elhagyásakor az elem el nem mentett változtatásainak helyreállításához szükséges
  ;   eltárolni az elem ... változtatásait.
  ; - Az elem törlésének visszaállítása után újra megnyíló szerkesztő a visszaállított elemen
  ;   alkalmazza annak (törléskori) el nem mentett változtatásait.
  (let [current-item-id (r core.subs/get-current-item-id db extension-id item-namespace)
        current-item    (r core.subs/get-current-item    db extension-id item-namespace)
        backup-item     (r backup.subs/get-backup-item   db extension-id item-namespace current-item-id)
        local-changes   (map/difference current-item backup-item)]
       (assoc-in db [:plugins :item-editor/local-changes extension-id current-item-id] local-changes)))

(defn clean-recovery-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (return db))

(defn recover-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; - Az elem törlése utáni visszaállításkor az elem szerkesztőbe betöltött kliens-oldali változata
  ;   a törléskor eltárolt biztonsági mentéséből és az elem el nem mentett változtatásainak
  ;   összefésüléséből készül.
  ; - Ha szükséges, akkor ez a logika módosítható úgy, hogy a törlés visszaállítása lekérésre
  ;   érkezett szerver-válaszból kiolvasott dokumentum (az elem elmentett szerver-oldali változata)
  ;   és a törléskor eltárolt el nem mentett változtatások legyenek összefésülve.
  (let [item-path       (r mount.subs/get-body-prop       db extension-id item-namespace :item-path)
        current-item-id (r core.subs/get-current-item-id  db extension-id item-namespace)
        recovered-item  (r backup.subs/get-recovered-item db extension-id item-namespace)]
       (-> db (assoc-in  item-path recovered-item)
              (dissoc-in [:plugins :item-editor/local-changes extension-id current-item-id]))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-editor/clean-recovery-data! clean-recovery-data!)
