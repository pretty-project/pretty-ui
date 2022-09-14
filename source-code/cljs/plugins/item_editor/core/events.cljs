
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.events
    (:require [mid-fruits.candy                   :refer [return]]
              [mid-fruits.map                     :refer [dissoc-in]]
              [plugins.item-editor.backup.events  :as backup.events]
              [plugins.item-editor.body.subs      :as body.subs]
              [plugins.item-editor.core.subs      :as core.subs]
              [plugins.plugin-handler.core.events :as core.events]
              [x.app-core.api                     :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.events
(def set-meta-item!     core.events/set-meta-item!)
(def remove-meta-items! core.events/remove-meta-items!)
(def set-mode!          core.events/set-mode!)
(def set-item-id!       core.events/set-item-id!)
(def update-item-id!    core.events/update-item-id!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (r set-mode! db editor-id :error-mode?))

(defn set-recovery-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; A {:recovery-mode? true} beállítással elindítitott szerkesztő visszaállítja
  ; az elemet az utoljára eltárolt másolat alapján.
  (r set-mode! db editor-id :recovery-mode?))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-downloads!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; A body komponens component-will-unmount életciklusa által alkalmazott
  ; reset-downloads! függvény nem törli ki az elemről készült backup-item másolatot,
  ; hogy a plugin elhagyása utáni esetleges "El nem mentett változtatások visszaállítása"
  ; funkció használatakor a {:recovery-mode? true} állapotban újra elinduló plugin
  ; számára elérhetők legyenek a visszaállításhoz szükséges adatok.
  (let [item-path        (r body.subs/get-body-prop db editor-id :item-path)
        suggestions-path (r body.subs/get-body-prop db editor-id :suggestions-path)]
       (-> db (dissoc-in [:plugins :plugin-handler/meta-items editor-id :data-received?])
              (dissoc-in item-path)
              (dissoc-in suggestions-path))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-initial-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; - A body komponens {:initial-item {...}} tulajdonsága új elem hozzáadásakor
  ;   van használva. Létező elemek szerkesztésekor már nem szükséges az elem
  ;   kezdeti értékét beállítani.
  ;
  ; - XXX#3005
  ;   Az adatok letöltésekor a request-item! függvény alkalmazza a reset-downloads! függvényt,
  ;   ezért az elem kezdeti állapota ha a body komponens React-fába csatolásakor lenne beállítva,
  ;   akkor törlődne az adatok letöltésekor, ezért az elem kezdeti állapota az adatok letöltésének
  ;   befejezésekor állítódik be.
  ;
  ; - A body komponens {:initial-item {...}} tulajdonsága alapján beállított kezdeti állapot
  ;   használatával ...
  ;   ... elkerülthető, hogy a szerkesztőben megjelenített input mezők {:initial-value ...}
  ;       tulajdonságát kelljen használni, ami miatt a szerkesztő egy változatlan elemen is
  ;       tévesen azt érzékelné, hogy az megváltozott, ha a mező {:initial-value ...} tulajdonsága
  ;       a szerkesztett elemre hatással lenne. Kilépéskor a "Nem mentett változatások visszaállítása"
  ;       értesítés jelenne meg abban az esetben is, amikor az elemen nem történt változtatás
  ;       a felhasználó részéről, csak az input mezők {:initial-value ...} tulajdonsága
  ;       változtattta meg az elemet.
  ;   ... beállíthatók a dokumentum felhasználó által nem szerkeszthető tulajdonságai.
  (if (r core.subs/new-item? db editor-id)
      (let [initial-item (r body.subs/get-body-prop db editor-id :initial-item)
            item-path    (r body.subs/get-body-prop db editor-id :item-path)]
           ; x4.7.3 #15 verzióban a use-initial-item! függvény része lett (újra?)
           ; a backup-current-item! lépés.
           ;
           ; Az elem letöltése után eddig is készült másolat az elemről, viszont abban
           ; az esetben, amikor a szerkesztő "Új elem szerkesztése" módban nyílt meg,
           ; tehát nem volt szükség az elem letöltésére és a body-props térkép
           ; {:initial-item ...} tulajdonságával az elemnek kezdeti érték lett beállítva,
           ; akkor hiányzott a kezdeti érték beállítása után a másolat készítése.
           ;
           ; Emiatt backup.subs/form-changed? függvénye {:initial-item ...} tulajdonsággal
           ; használt szerkesztő esetén, mindjárt a megnyitást követően azt jelezte, hogy
           ; az elem megváltozott. Ezért szükséges a kezdeti érték beállítása után elkészíteni
           ; a másolatot az elemről, hogy a másolat és az elem a szerkesztő indulásakor
           ; megegyezzenek és a form-changed? függvény ne jelezzen különbséget.
           (as-> db % (assoc-in % item-path initial-item)
                      (r backup.events/backup-current-item! % editor-id)))
      (return db)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (r update-item-id! db editor-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-editor/set-error-mode! set-error-mode!)
