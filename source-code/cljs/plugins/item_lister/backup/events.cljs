

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.backup.events
    (:require [mid-fruits.vector                    :as vector]
              [plugins.item-lister.body.subs        :as body.subs]
              [plugins.item-lister.core.subs        :as core.subs]
              [plugins.plugin-handler.backup.events :as backup.events]
              [x.app-core.api                       :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.backup.events
(def clean-backup-items! backup.events/clean-backup-items!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn backup-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  ; - A kijelölt elemeken végzett műveletek előtt (pl. törlés) szükséges lehet azokról biztonsági
  ;   másolatot készíteni, amiből esetlegesen visszaállíthatók az elemek, ha a felhasználó
  ;   a művelet visszavonása lehetőséget választja.
  ;
  ; - A (kijelölt) elemekről készült biztonsági másolatok az elemek azonosítójával kerülnek eltárolásra.
  (let [items-path     (r body.subs/get-body-prop db plugin-id :items-path)
        selected-items (r core.subs/get-meta-item db plugin-id :selected-items)]
       (letfn [(f [db item-dex]
                  (let [item-id (get-in db (vector/concat-items items-path [item-dex :id]))
                        item    (get-in db (vector/conj-item    items-path item-dex))]
                       (assoc-in db [:plugins :plugin-handler/backup-items plugin-id item-id] item)))]
              (reduce f db selected-items))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/clean-backup-items! clean-backup-items!)
