
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.backup.subs
    (:require [mid-fruits.map                 :as map]
              [plugins.item-editor.core.subs  :as core.subs]
              [plugins.item-editor.mount.subs :as mount.subs]
              [x.app-core.api                 :refer [r]]
              [x.app-db.api                   :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-backup-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (get-in db [:plugins :item-editor/backup-items extension-id item-id]))

(defn export-backup-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [db [_ extension-id item-namespace item-id]]
  (let [backup-item (r get-backup-item db extension-id item-namespace item-id)]
       (db/document->namespaced-document backup-item item-namespace)))

(defn get-local-changes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r core.subs/get-current-item-id db extension-id item-namespace)]
       (get-in db [:plugins :item-editor/local-changes extension-id current-item-id])))

(defn item-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  ; - Az item-changed? függvény összehasonlítja az elem (megnyitáskor eltárolt!) biztonsági másolatát
  ;   az elem jelenlegi állapotával.
  ; - Az initial-item értékét szükséges kivonni az elemből a vizsgálat előtt, mert befolyásolja
  ;   az elem változásának vizsgálatát, az hogy a szerkesztő indításakor automatikusan az elem állapotához
  ;   adódik az initial-item térkép értéke!
  ; - Az initial-item értékének kivonásakor figyelembe kell venni, azt hogy lehetséges, hogy
  ;   az elem megnyitáskori állapotában már tartalmazta az initial-item térkép adatait,
  ;   ezért az összehasonlításkor a backup-item és a current-item térképekből egyaránt szükséges
  ;   kivonni az initial-item értékét.
  (let [current-item-id (r core.subs/get-current-item-id db extension-id item-namespace)
        current-item    (r core.subs/get-current-item    db extension-id item-namespace)
        backup-item     (r get-backup-item               db extension-id item-namespace current-item-id)
        initial-item    (r mount.subs/get-body-prop      db extension-id item-namespace :initial-item)]
       (not= (map/difference backup-item  initial-item)
             (map/difference current-item initial-item))))
