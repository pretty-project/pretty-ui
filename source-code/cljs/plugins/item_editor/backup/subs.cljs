
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.backup.subs
    (:require [mid-fruits.candy                   :refer [return]]
              [mid-fruits.map                     :as map]
              [plugins.item-editor.body.subs      :as body.subs]
              [plugins.item-editor.core.subs      :as core.subs]
              [plugins.item-editor.download.subs  :as download.subs]
              [plugins.plugin-handler.backup.subs :as backup.subs]
              [re-frame.api                       :as r :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
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
  ; - Az item-changed? függvény összehasonlítja az elem (letöltéskor eltárolt!)
  ;   másolatát az elem jelenlegi állapotával.
  ;
  ; - Az initial-item alkalmazása befolyásolja az elem változásának vizsgálhatóságát!
  ;
  ; A) Ha a vizsgált érték az initial-item térkép azonos kulcsú értékével megegyezik,
  ;    akkor nem vizsgálja a változást.
  ;
  ; B) Ha a vizsgált érték üres (NIL, "", [], ...), de a tárolt érték NEM üres,
  ;    akkor az elem megváltozott.
  ;    (Az empty? függvény csak seqable értékeken alkalmazható!)
  ;
  ; C) Ha a vizsgált érték a backup-item azonos kulcsú elemével NEM egyezik meg,
  ;    akkor az elem megváltozott!
  (let [current-item-id (r core.subs/get-current-item-id db editor-id)
        current-item    (r core.subs/get-current-item    db editor-id)
        backup-item     (r get-backup-item               db editor-id current-item-id)
        initial-item    (r body.subs/get-body-prop       db editor-id :initial-item)]
       (letfn [(f [[key value]]
                  (cond ; A)
                        (= value (key initial-item))
                        (return false)
                        ; B)
                        ; Az empty? függvényt csak a seqable értékeken lehetséges alkalmazni!
                        (and (-> value seqable?)
                             (-> value empty?))
                        (and (-> backup-item key seqable?)
                             (-> backup-item key empty? not))
                        ; C)
                        :else
                        (not= value (key backup-item))))]
              (some f current-item))))

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
  ;   Pl. Az egyes input mezők használatakor ha a felhasználó kiüríti a mezőt, akkor a visszamaradó
  ;       üres string értéket egyenlőnek tekinti a mező használata előtti NIL értékkel!
  (if-let [data-received? (r download.subs/data-received? db editor-id)]
          (let [current-item-id (r core.subs/get-current-item-id db editor-id)
                current-item    (r core.subs/get-current-item    db editor-id)
                backup-item     (r get-backup-item               db editor-id current-item-id)]
               (map/items-different? current-item backup-item change-keys))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/item-changed? :my-editor]
(r/reg-sub :item-editor/item-changed? item-changed?)

; @param (keyword) editor-id
; @param (keywords in vector) change-keys
;
; @usage
;  [:item-editor/form-changed? :my-editor [:name :email-address]]
(r/reg-sub :item-editor/form-changed? form-changed?)
