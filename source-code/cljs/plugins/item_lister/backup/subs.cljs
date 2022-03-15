
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.backup.subs
    (:require [mid-fruits.vector :as vector]
              [x.app-db.api      :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-backup-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (namespaced maps in vector)
  [db [_ extension-id item-namespace item-ids]]
  (vector/->items item-ids #(let [backup-item (get-in db [:plugins :item-lister/backup-items extension-id %])]
                                 (db/document->namespaced-document backup-item item-namespace))))
