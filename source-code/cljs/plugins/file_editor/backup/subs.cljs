
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.file-editor.backup.subs
    (:require [mid-fruits.candy                  :refer [return]]
              [mid-fruits.map                    :as map]
              [mid-fruits.mixed                  :as mixed]
              [plugins.file-editor.body.subs     :as body.subs]
              [plugins.file-editor.core.subs     :as core.subs]
              [plugins.file-editor.download.subs :as download.subs]
              [re-frame.api                      :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-backup-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (get-in db [:plugins :plugin-handler/backup-items editor-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn content-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  ; A content-changed? függvény összehasonlítja a tartalom (letöltéskor eltárolt!)
  ; másolatát a tartalom jelenlegi állapotával.
  ;
  ; A default-item alkalmazása befolyásolja a tartalom változásának vizsgálhatóságát!
  ;
  ; A) Ha a vizsgált érték a default-item térkép azonos kulcsú értékével megegyezik,
  ;    akkor nem vizsgálja a változást.
  ;
  ; B) Ha a vizsgált érték üres (NIL, "", [], ...), de a tárolt érték NEM üres,
  ;    akkor az elem megváltozott.
  ;
  ; C) Ha a vizsgált érték a backup-item azonos kulcsú elemével NEM egyezik meg,
  ;    akkor az elem megváltozott.
  ;
  ; XXX#5671
  (let [current-content (r core.subs/get-current-content db editor-id)
        backup-content  (r get-backup-content            db editor-id)
        default-content (r body.subs/get-body-prop       db editor-id :default-content)]
       (letfn [(f [[key value]]
                  (cond ; A)
                        (= value (key default-content))
                        (return false)
                        ; B)
                        (-> value              mixed/=empty?)
                        (-> backup-content key mixed/nonempty?)
                        ; C)
                        :else
                        (not= value (key backup-content))))]
              (some f current-content))))

(defn form-changed?
  ; @param (keyword) editor-id
  ; @param (keywords in vector) change-keys
  ;
  ; @usage
  ;  (r file-editor/form-changed? db :my-editor [:name :email-address])
  ;
  ; @return (boolean)
  [db [_ editor-id change-keys]]
  ; A form-changed? függvény összehasonlítja az elem {:change-keys [...]} paraméterként
  ; átadott kulcsainak értékeit az elemről tárolt másolat azonos értékeivel.
  ;
  ; Az egyes értékek vizsgálatakor, ha az adott érték üres (pl. NIL, "", []), akkor figyelembe
  ; veszi a NIL és a különböző üres típusokat és egyenlőnek tekinti őket!
  ; Pl.: Az egyes input mezők használatakor ha a felhasználó kiüríti a mezőt, akkor a visszamaradó
  ;      üres string értéket egyenlőnek tekinti a mező használata előtti NIL értékkel!
  (if-let [data-received? (r download.subs/data-received? db editor-id)]
          (let [current-content (r core.subs/get-current-content db editor-id)
                backup-content  (r get-backup-content            db editor-id)]
               (map/items-different? current-content backup-content change-keys))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
;
; @usage
;  [:file-editor/content-changed? :my-editor]
(r/reg-sub :file-editor/content-changed? content-changed?)

; @param (keyword) editor-id
; @param (keywords in vector) change-keys
;
; @usage
;  [:file-editor/form-changed? :my-editor [:name :email-address]]
(r/reg-sub :file-editor/form-changed? form-changed?)
