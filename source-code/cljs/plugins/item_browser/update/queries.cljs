
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.update.queries
    (:require [plugins.item-browser.backup.subs :as backup.subs]
              [plugins.item-browser.items.subs  :as items.subs]
              [plugins.item-browser.update.subs :as update.subs]
              [x.app-core.api                   :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-delete-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-id]]
  (let [mutation-name (r update.subs/get-mutation-name db extension-id item-namespace :delete)]
       [:debug `(~(symbol mutation-name) ~{:item-id item-id})]))

(defn get-undo-delete-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-id]]
  (let [mutation-name (r update.subs/get-mutation-name  db extension-id item-namespace :undo-delete)
        backup-item   (r backup.subs/export-backup-item db extension-id item-namespace item-id)]
       [:debug `(~(symbol mutation-name) ~{:item backup-item})]))

(defn get-duplicate-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-id]]
  ; Az item-browser plugin az item-editor pluginhoz hasonlóan duplikáláskor az elem azonosítója
  ; helyett az elemet küldi el a szerver számára, hogy a két plugin mutation függvényei hasonló
  ; paraméterezéssel működjenek.
  ; (az item-browser plugin működéséhez elegendő lenne az elem azonosítóját elküldni duplikáláskor)
  (let [mutation-name (r update.subs/get-mutation-name db extension-id item-namespace :duplicate)
        exported-item (r items.subs/export-item        db extension-id item-namespace item-id)]
       [:debug `(~(symbol mutation-name) ~{:item exported-item})]))

(defn get-undo-duplicate-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) copy-id
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace copy-id]]
  (let [mutation-name (r update.subs/get-mutation-name db extension-id item-namespace :undo-duplicate)]
       [:debug `(~(symbol mutation-name) ~{:item-id copy-id})]))

(defn get-update-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-id]]
  (let [mutation-name (r update.subs/get-mutation-name db extension-id item-namespace :update)
        exported-item (r items.subs/export-item        db extension-id item-namespace item-id)]
       [:debug `(~(symbol mutation-name) ~{:item exported-item})]))