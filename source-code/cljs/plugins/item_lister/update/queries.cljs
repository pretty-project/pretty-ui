
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.update.queries
    (:require [plugins.item-lister.backup.subs :as backup.subs]
              [plugins.item-lister.core.subs   :as core.subs]
              [plugins.item-lister.update.subs :as update.subs]
              [x.app-core.api                  :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-delete-items-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (map)
  ;  {:item-ids (strings in vector)}
  [db [_ extension-id item-namespace item-ids]]
  (merge (r core.subs/get-meta-item db extension-id item-namespace :default-query-params)
         {:item-ids item-ids}))

(defn get-delete-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-ids]]
  (let [mutation-name  (r update.subs/get-mutation-name   db extension-id item-namespace :delete)
        mutation-props (r get-delete-items-mutation-props db extension-id item-namespace item-ids)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-undo-delete-items-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (map)
  ;  {:items (namespaced maps in vector)}
  [db [_ extension-id item-namespace item-ids]]
  (let [exported-items (r backup.subs/export-backup-items db extension-id item-namespace item-ids)]
       (merge (r core.subs/get-meta-item db extension-id item-namespace :default-query-params)
              {:items exported-items})))

(defn get-undo-delete-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-ids]]
  (let [mutation-name  (r update.subs/get-mutation-name        db extension-id item-namespace :undo-delete)
        mutation-props (r get-undo-delete-items-mutation-props db extension-id item-namespace item-ids)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-duplicate-items-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (map)
  ;  {:item-ids (strings in vector)}
  [db [_ extension-id item-namespace item-ids]]
  (merge (r core.subs/get-meta-item db extension-id item-namespace :default-query-params)
         {:item-ids item-ids}))

(defn get-duplicate-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-ids]]
  (let [mutation-name  (r update.subs/get-mutation-name      db extension-id item-namespace :duplicate)
        mutation-props (r get-duplicate-items-mutation-props db extension-id item-namespace item-ids)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-undo-duplicate-items-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) copy-ids
  ;
  ; @return (map)
  ;  {:item-ids (strings in vector)}
  [db [_ extension-id item-namespace copy-ids]]
  (merge (r core.subs/get-meta-item db extension-id item-namespace :default-query-params)
         {:item-ids copy-ids}))

(defn get-undo-duplicate-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) copy-ids
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace copy-ids]]
  (let [mutation-name  (r update.subs/get-mutation-name           db extension-id item-namespace :undo-duplicate)
        mutation-props (r get-undo-duplicate-items-mutation-props db extension-id item-namespace copy-ids)]
       [`(~(symbol mutation-name) ~mutation-props)]))
