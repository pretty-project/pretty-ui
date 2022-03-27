
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.backup.subs
    (:require [mid-fruits.map                    :as map]
              [plugins.item-editor.core.subs     :as core.subs]
              [plugins.item-editor.mount.subs    :as mount.subs]
              [plugins.item-editor.transfer.subs :as transfer.subs]
              [x.app-core.api                    :refer [r]]
              [x.app-db.api                      :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-backup-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ editor-id item-id]]
  (get-in db [:plugins :item-editor/backup-items editor-id item-id]))

(defn export-backup-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [db [_ editor-id item-id]]
  (let [item-namespace (r transfer.subs/get-transfer-item db editor-id :item-namespace)
        backup-item    (r get-backup-item                 db editor-id item-id)]
       (db/document->namespaced-document backup-item item-namespace)))

(defn get-local-changes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [current-item-id (r core.subs/get-current-item-id db editor-id)]
       (get-in db [:plugins :item-editor/local-changes editor-id current-item-id])))

(defn item-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  ; - Az item-changed? függvény összehasonlítja az elem (megnyitáskor eltárolt!) biztonsági másolatát
  ;   az elem jelenlegi állapotával.
  ; - Az initial-item értékét szükséges kivonni az elemből a vizsgálat előtt, mert befolyásolja
  ;   az elem változásának vizsgálatát, az hogy a szerkesztő indításakor automatikusan az elem állapotához
  ;   adódik az initial-item térkép értéke!
  ; - Az initial-item értékének kivonásakor figyelembe kell venni, azt hogy lehetséges, hogy
  ;   az elem megnyitáskori állapotában már tartalmazta az initial-item térkép adatait,
  ;   ezért az összehasonlításkor a backup-item és a current-item térképekből egyaránt szükséges
  ;   kivonni az initial-item értékét.
  (let [current-item-id (r core.subs/get-current-item-id db editor-id)
        current-item    (r core.subs/get-current-item    db editor-id)
        backup-item     (r get-backup-item               db editor-id current-item-id)
        initial-item    (r mount.subs/get-body-prop      db editor-id :initial-item)]
       (not= (map/difference backup-item  initial-item)
             (map/difference current-item initial-item))))
