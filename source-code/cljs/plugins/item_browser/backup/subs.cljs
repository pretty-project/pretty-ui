
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.backup.subs
    (:require [plugins.item-browser.transfer.subs :as transfer.subs]
              [x.app-core.api                     :refer [r]]
              [x.app-db.api                       :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-backup-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ browser-id item-id]]
  (get-in db [:plugins :item-browser/backup-items browser-id item-id]))

(defn export-backup-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [db [_ browser-id item-id]]
  (let [item-namespace (r transfer.subs/get-transfer-item db browser-id :item-namespace)
        backup-item    (r get-backup-item                 db browser-id item-id)]
       (db/document->namespaced-document backup-item item-namespace)))
