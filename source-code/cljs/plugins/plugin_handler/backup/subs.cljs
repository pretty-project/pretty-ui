
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.backup.subs
    (:require [mid-fruits.vector                    :as vector]
              [plugins.plugin-handler.transfer.subs :as transfer.subs]
              [x.app-core.api                       :refer [r]]
              [x.app-db.api                         :as db]))



;; -- Single item subscriptions -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-backup-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ plugin-id item-id]]
  (get-in db [:plugins :plugin-handler/backup-items plugin-id item-id]))

(defn export-backup-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [db [_ plugin-id item-id]]
  ; Az export-backup-item függvény az item-id tulajdonságként átadott azonosítójú elem névtérrel
  ; ellátott változatával tér vissza.
  (let [item-namespace (r transfer.subs/get-transfer-item db plugin-id :item-namespace)
        backup-item    (r get-backup-item                 db plugin-id item-id)]
       (db/document->namespaced-document backup-item item-namespace)))



;; -- Multiple item subscriptions ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-backup-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  (get-in db [:plugins :plugin-handler/backup-items plugin-id]))

(defn export-backup-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (strings in vector) item-ids
  ;
  ; @return (namespaced maps in vector)
  [db [_ plugin-id item-ids]]
  ; Az export-backup-items függvény az item-ids vektorban átadott azonosítójú elemek névtérrel
  ; ellátott változatával tér vissza.
  (let [item-namespace (r transfer.subs/get-transfer-item db plugin-id :item-namespace)]
       (vector/->items item-ids #(let [backup-item (r get-backup-item db plugin-id %)]
                                      (db/document->namespaced-document backup-item item-namespace)))))
