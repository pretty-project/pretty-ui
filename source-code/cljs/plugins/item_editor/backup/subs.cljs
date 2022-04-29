
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.backup.subs
    (:require [mid-fruits.map                     :as map]
              [plugins.item-editor.body.subs      :as body.subs]
              [plugins.item-editor.core.subs      :as core.subs]
              [plugins.item-editor.download.subs  :as download.subs]
              [plugins.plugin-handler.backup.subs :as backup.subs]
              [x.app-core.api                     :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.backup.subs
(def get-backup-item    backup.subs/get-backup-item)
(def export-backup-item backup.subs/export-backup-item)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-changes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ editor-id item-id]]
  (get-in db [:plugins :plugin-handler/item-changes editor-id item-id]))



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
        initial-item    (r body.subs/get-body-prop       db editor-id :initial-item)]
       (not= (map/difference backup-item  initial-item)
             (map/difference current-item initial-item))))

(defn form-changed?
  ; @param (keyword) editor-id
  ; @param (keywords in vector) change-keys
  ;
  ; @usage
  ;  (r item-editor/form-changed? db :my-editor [:name :email-address])
  ;
  ; @return (boolean)
  [db [_ editor-id change-keys]]
  ; - A form-changed? függvény összehasonlítja az elem {:change-keys [...]} paraméterként
  ;   átadott kulcsainak értékeit az elemről tárolt másolat azonos értékeivel.
  ;
  ; - Az egyes értékek vizsgálatakor, ha az adott érték üres (pl. NIL, "", []), akkor figyelembe
  ;   veszi a NIL és a különböző üres típusokat és egyenlőnek tekinti őket!
  ;   Pl.: Az egyes input mezők használatakor ha a felhasználó kiüríti a mezőt, akkor a visszamaradó
  ;        üres string értéket egyenlőnek tekinti a mező használata előtti NIL értékkel!
  (if-let [data-received? (r download.subs/data-received? db editor-id)]
          (let [current-item-id (r core.subs/get-current-item-id db editor-id)
                current-item    (r core.subs/get-current-item    db editor-id)
                backup-item     (r get-backup-item               db editor-id current-item-id)]
               (letfn [(f [change-key] (if (-> current-item change-key empty?)
                                           (-> backup-item  change-key empty? not)
                                           (not= (change-key current-item)
                                                 (change-key backup-item))))]
                      (some f change-keys)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/item-changed? :my-editor]
(a/reg-sub :item-editor/item-changed? item-changed?)

; @param (keyword) editor-id
; @param (keywords in vector) change-keys
;
; @usage
;  [:item-editor/form-completed? :my-editor [:name :email-address]]
(a/reg-sub :item-editor/form-changed? form-changed?)
