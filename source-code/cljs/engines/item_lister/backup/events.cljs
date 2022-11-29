
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.backup.events
    (:require [engines.engine-handler.backup.events :as backup.events]
              [engines.item-lister.body.subs        :as body.subs]
              [engines.item-lister.core.subs        :as core.subs]
              [re-frame.api                         :as r :refer [r]]
              [vector.api                           :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.backup.events
(def clean-backup-items! backup.events/clean-backup-items!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn backup-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  ; A kijelölt elemeken végzett műveletek előtt (pl. törlés) szükséges lehet azokról biztonsági
  ; másolatot készíteni, amiből esetlegesen visszaállíthatók az elemek, ha a felhasználó
  ; a művelet visszavonása lehetőséget választja.
  ;
  ; A (kijelölt) elemekről készült biztonsági másolatok az elemek azonosítójával kerülnek eltárolásra.
  (let [items-path     (r body.subs/get-body-prop db lister-id :items-path)
        selected-items (r core.subs/get-meta-item db lister-id :selected-items)]
       (letfn [(f [db item-dex]
                  (let [item-id (get-in db (vector/concat-items items-path [item-dex :id]))
                        item    (get-in db (vector/conj-item    items-path item-dex))]
                       (assoc-in db [:engines :engine-handler/backup-items lister-id item-id] item)))]
              (reduce f db selected-items))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :item-lister/clean-backup-items! clean-backup-items!)
