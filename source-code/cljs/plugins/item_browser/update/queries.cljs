
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.update.queries
    (:require [plugins.item-browser.backup.subs :as backup.subs]
              [plugins.item-browser.core.subs   :as core.subs]
              [plugins.item-browser.items.subs  :as items.subs]
              [plugins.item-browser.update.subs :as update.subs]
              [x.app-core.api                   :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-delete-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  ;  {:item-id (string)}
  [db [_ extension-id item-namespace item-id]]
  (merge (r core.subs/get-meta-item db extension-id item-namespace :default-mutation-params)
         {:item-id item-id}))

(defn get-delete-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-id]]
  (let [mutation-name  (r update.subs/get-mutation-name  db extension-id item-namespace :delete)
        mutation-props (r get-delete-item-mutation-props db extension-id item-namespace item-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-undo-delete-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  ;  {:item (namespaced map)}
  [db [_ extension-id item-namespace item-id]]
  (let [backup-item (r backup.subs/export-backup-item db extension-id item-namespace item-id)]
       (merge (r core.subs/get-meta-item db extension-id item-namespace :default-mutation-params)
              {:item backup-item})))

(defn get-undo-delete-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-id]]
  (let [mutation-name  (r update.subs/get-mutation-name  db extension-id item-namespace :undo-delete)
        mutation-props (r get-undo-delete-item-mutation-props db extension-id item-namespace item-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-duplicate-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  ;  {:item (namespaced map)}
  [db [_ extension-id item-namespace item-id]]
  ; Az item-browser plugin az item-editor pluginhoz hasonlóan duplikáláskor az elem azonosítója
  ; helyett az elemet küldi el a szerver számára, hogy a két plugin mutation függvényei hasonló
  ; paraméterezéssel működjenek.
  ; (az item-browser plugin működéséhez elegendő lenne az elem azonosítóját elküldni duplikáláskor)
  (let [exported-item (r items.subs/export-item db extension-id item-namespace item-id)]
       (merge (r core.subs/get-meta-item db extension-id item-namespace :default-mutation-params)
              {:item exported-item})))

(defn get-duplicate-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-id]]
  (let [mutation-name  (r update.subs/get-mutation-name     db extension-id item-namespace :duplicate)
        mutation-props (r get-duplicate-item-mutation-props db extension-id item-namespace item-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-undo-duplicate-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) copy-id
  ;
  ; @return (map)
  ;  {:item-id (string)}
  [db [_ extension-id item-namespace copy-id]]
  (merge (r core.subs/get-meta-item db extension-id item-namespace :default-mutation-params)
         {:item-id copy-id}))

(defn get-undo-duplicate-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) copy-id
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace copy-id]]
  (let [mutation-name  (r update.subs/get-mutation-name          db extension-id item-namespace :undo-duplicate)
        mutation-props (r get-undo-duplicate-item-mutation-props db extension-id item-namespace copy-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-update-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  ;  {:item (namespaced map)}
  [db [_ extension-id item-namespace item-id]]
  (let [exported-item (r items.subs/export-item db extension-id item-namespace item-id)]
       (merge (r core.subs/get-meta-item db extension-id item-namespace :default-mutation-params)
              {:item exported-item})))

(defn get-update-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-id]]
  (let [mutation-name  (r update.subs/get-mutation-name  db extension-id item-namespace :update)
        mutation-props (r get-update-item-mutation-props db extension-id item-namespace item-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))
