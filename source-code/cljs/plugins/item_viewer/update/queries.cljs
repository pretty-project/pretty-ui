
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.update.queries
    (:require [plugins.item-viewer.backup.subs :as backup.subs]
              [plugins.item-viewer.core.subs   :as core.subs]
              [plugins.item-viewer.update.subs :as update.subs]
              [x.app-core.api                  :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-delete-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (map)
  ;  {:item-id (string)}
  [db [_ viewer-id]]
  (let [current-item-id (r core.subs/get-current-item-id db viewer-id)]
       (merge (r core.subs/get-meta-item db viewer-id :default-query-params)
              {:item-id current-item-id})))

(defn get-delete-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (vector)
  [db [_ viewer-id]]
  (let [mutation-name  (r update.subs/get-mutation-name  db viewer-id :delete-item!)
        mutation-props (r get-delete-item-mutation-props db viewer-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-undo-delete-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (string) item-id
  ;
  ; @return (map)
  ;  {:item (namespaced map)}
  [db [_ viewer-id item-id]]
  (let [backup-item (r backup.subs/export-backup-item db viewer-id item-id)]
       (merge (r core.subs/get-meta-item db viewer-id :default-query-params)
              {:item backup-item})))

(defn get-undo-delete-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (string) item-id
  ;
  ; @return (vector)
  [db [_ viewer-id item-id]]
  ; A törlés művelet visszavonásához szükséges a szerver számára elküldeni az elem másolatát,
  ; mivel feltételezhető, hogy az elem már nem elérhető a szerveren.
  (let [mutation-name  (r update.subs/get-mutation-name       db viewer-id :undo-delete-item!)
        mutation-props (r get-undo-delete-item-mutation-props db viewer-id item-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-duplicate-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (map)
  ;  {:item-id (string)}
  [db [_ viewer-id]]
  (let [current-item-id (r core.subs/get-current-item-id db viewer-id)]
       (merge (r core.subs/get-meta-item db viewer-id :default-query-params)
              {:item-id current-item-id})))

(defn get-duplicate-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (vector)
  [db [_ viewer-id]]
  (let [mutation-name  (r update.subs/get-mutation-name     db viewer-id :duplicate-item!)
        mutation-props (r get-duplicate-item-mutation-props db viewer-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))