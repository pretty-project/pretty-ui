
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.backup.subs
    (:require [engines.engine-handler.transfer.subs :as transfer.subs]
              [map.api                              :as map]
              [re-frame.api                         :refer [r]]
              [vector.api                           :as vector]))



;; -- Single item subscriptions -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-backup-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ engine-id item-id]]
  (get-in db [:engines :engine-handler/backup-items engine-id item-id]))

(defn export-backup-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [db [_ engine-id item-id]]
  ; Az export-backup-item függvény az item-id tulajdonságként átadott azonosítójú elem névtérrel
  ; ellátott változatával tér vissza.
  (let [item-namespace (r transfer.subs/get-transfer-item db engine-id :item-namespace)
        backup-item    (r get-backup-item                 db engine-id item-id)]
       (map/add-namespace backup-item item-namespace)))



;; -- Multiple item subscriptions ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-backup-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  (get-in db [:engines :engine-handler/backup-items engine-id]))

(defn export-backup-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (strings in vector) item-ids
  ;
  ; @return (namespaced maps in vector)
  [db [_ engine-id item-ids]]
  ; Az export-backup-items függvény az item-ids vektorban átadott azonosítójú elemek névtérrel
  ; ellátott változatával tér vissza.
  (let [item-namespace (r transfer.subs/get-transfer-item db engine-id :item-namespace)]
       (vector/->items item-ids #(let [backup-item (r get-backup-item db engine-id %)]
                                      (map/add-namespace backup-item item-namespace)))))
