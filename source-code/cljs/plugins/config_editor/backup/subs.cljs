
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.config-editor.backup.subs
    (:require [mid-fruits.map                      :as map]
              [plugins.config-editor.core.subs     :as core.subs]
              [plugins.config-editor.download.subs :as download.subs]
              [x.app-core.api                      :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-backup-config
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (get-in db [:plugins :plugin-handler/backup-items editor-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn config-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  ; - Az config-changed? függvény összehasonlítja az elem (letöltéskor eltárolt!)
  ;   másolatát az elem jelenlegi állapotával.
  ;
  ; A) Ha a vizsgált érték üres (NIL, "", [], ...), de a tárolt érték NEM üres,
  ;    akkor az elem megváltozott.
  ;
  ; B) Ha a vizsgált érték a backup-item azonos kulcsú elemével NEM egyezik meg,
  ;    akkor az elem megváltozott!
  (let [current-config (r core.subs/get-current-config db editor-id)
        backup-config  (r get-backup-config            db editor-id)]
       (letfn [(f [[key value]]
                  (if (-> value empty?)
                      ; A)
                      (-> backup-config key empty? not)
                      ; B)
                      (not= value (key backup-config))))]
              (some f current-config))))

(defn form-changed?
  ; @param (keyword) editor-id
  ; @param (keywords in vector) change-keys
  ;
  ; @usage
  ;  (r config-editor/form-changed? db :my-editor [:name :email-address])
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
          (let [current-config (r core.subs/get-current-config db editor-id)
                backup-config  (r get-backup-config            db editor-id)]
               (map/items-different? current-config backup-config change-keys))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
;
; @usage
;  [:config-editor/config-changed? :my-editor]
(a/reg-sub :config-editor/config-changed? config-changed?)

; @param (keyword) editor-id
; @param (keywords in vector) change-keys
;
; @usage
;  [:config-editor/form-changed? :my-editor [:name :email-address]]
(a/reg-sub :config-editor/form-changed? form-changed?)
