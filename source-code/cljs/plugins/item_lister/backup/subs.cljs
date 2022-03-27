
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.backup.subs
    (:require [mid-fruits.vector                 :as vector]
              [plugins.item-lister.transfer.subs :as transfer.subs]
              [x.app-core.api                    :refer [r]]
              [x.app-db.api                      :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-backup-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  ;
  ; @return (namespaced maps in vector)
  [db [_ lister-id item-ids]]
  ; Az export-backup-items függvény az item-ids vektorban átadott azonosítójú elemek névtérrel ellátott
  ; változatával tér vissza.
  (let [item-namespace (r transfer.subs/get-transfer-item db lister-id :item-namespace)]
       (vector/->items item-ids #(let [backup-item (get-in db [:plugins :item-lister/backup-items lister-id %])]
                                      (db/document->namespaced-document backup-item item-namespace)))))
