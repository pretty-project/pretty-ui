
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.backup.subs
    (:require [engines.engine-handler.core.subs     :as core.subs]
              [engines.engine-handler.transfer.subs :as transfer.subs]
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
  ; XXX#3907 (source-code/cljs/engines/engine_handler/README.md)
  (let [item-namespace (r transfer.subs/get-transfer-item db engine-id :item-namespace)
        backup-item    (r get-backup-item                 db engine-id item-id)]
       (map/add-namespace backup-item item-namespace)))

(defn item-backed-up?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (boolean)
  [db [_ engine-id item-id]]
  (let [backup-item (r get-backup-item db engine-id item-id)]
       (some? backup-item)))



;; -- Current item subscriptions ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn current-item-backed-up?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (let [current-item-id (r core.subs/get-current-item-id db engine-id)]
       (r item-backed-up? db engine-id current-item-id)))



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
  ; XXX#3907 (source-code/cljs/engines/engine_handler/README.md)
  (vector/->items item-ids (fn [%] (r export-backup-item db engine-id %))))
