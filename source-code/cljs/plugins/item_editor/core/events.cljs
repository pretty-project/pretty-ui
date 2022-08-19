
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
              [plugins.item-editor.body.subs      :as body.subs]
              [plugins.item-editor.core.subs      :as core.subs]
              [plugins.plugin-handler.core.events :as core.events]
              [x.app-core.api                     :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.events
(def set-meta-item!     core.events/set-meta-item!)
(def remove-meta-items! core.events/remove-meta-items!)
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
  (assoc-in db [:plugins :plugin-handler/meta-items editor-id :error-mode?] true))

(defn set-recovery-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; A {:recovery-mode? true} beállítással elindítitott szerkesztő visszaállítja
  ; az elemet az utoljára eltárolt másolat alapján.
  (assoc-in db [:plugins :plugin-handler/meta-items editor-id :recovery-mode?] true))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-downloads!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
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
  ;   ezért az elem kezdeti állapota ha a body komponens React-fába csatolásakor lenne beálíltva,
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
           (assoc-in db item-path initial-item))
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
