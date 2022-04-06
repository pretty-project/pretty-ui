
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.backup.subs
    (:require [mid-fruits.map                     :as map]
              [plugins.item-editor.core.subs      :as core.subs]
              [plugins.item-editor.mount.subs     :as mount.subs]
              [plugins.plugin-handler.backup.subs :as backup.subs]
              [x.app-core.api                     :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.backup.subs
(def get-backup-item    backup.subs/get-backup-item)
(def export-backup-item backup.subs/export-backup-item)
(def get-local-changes  backup.subs/get-local-changes)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  ; - Az item-changed? függvény összehasonlítja az elem (megnyitáskor eltárolt!) másolatát
  ;   az elem jelenlegi állapotával.
  ;
  ; - Az initial-item értékét szükséges kivonni az elemből a vizsgálat előtt, mert befolyásolja
  ;   az elem változásának vizsgálatát, az hogy a szerkesztő indításakor automatikusan az elem
  ;   állapotához adódik az initial-item térkép értéke!
  ;
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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/item-changed? :my-editor]
(a/reg-sub :item-editor/item-changed? item-changed?)
