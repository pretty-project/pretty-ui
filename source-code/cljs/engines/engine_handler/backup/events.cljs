
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.backup.events
    (:require [engines.engine-handler.core.subs :as core.subs]
              [map.api                          :as map :refer [dissoc-in]]
              [re-frame.api                     :refer [r]]))



;; -- Single item events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn backup-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ engine-id item-id]]
  (let [downloaded-item (r core.subs/get-downloaded-item db engine-id item-id)]
       (assoc-in db [:engines :engine-handler/backup-items engine-id item-id] downloaded-item)))

(defn clean-backup-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ engine-id item-id]]
  (dissoc-in db [:engines :engine-handler/backup-items engine-id item-id]))



;; -- Current item events -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn backup-current-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  (let [current-item-id (r core.subs/get-current-item-id db engine-id)]
       (r backup-item! db engine-id current-item-id)))

(defn clean-current-item-backup!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  (let [current-item-id (r core.subs/get-current-item-id db engine-id)]
       (r clean-backup-item! db engine-id current-item-id)))



;; -- Multiple item events ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clean-backup-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (strings in vector) item-ids
  ;
  ; @return (map)
  [db [_ engine-id item-ids]]
  (update-in db [:engines :engine-handler/backup-items engine-id] map/remove-keys item-ids))
