
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.update.queries
    (:require [engines.item-browser.backup.subs :as backup.subs]
              [engines.item-browser.core.subs   :as core.subs]
              [engines.item-browser.items.subs  :as items.subs]
              [engines.item-browser.update.subs :as update.subs]
              [re-frame.api                     :refer [r]]))



;; -- Delete item queries -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-delete-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (map)
  ;  {:item-id (string)}
  [db [_ browser-id item-id]]
  (r core.subs/use-query-params db browser-id {:item-id item-id}))

(defn get-delete-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (vector)
  [db [_ browser-id item-id]]
  (let [mutation-name  (r update.subs/get-mutation-name  db browser-id :delete-item!)
        mutation-props (r get-delete-item-mutation-props db browser-id item-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; -- Undo delete item queries ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-undo-delete-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (map)
  ;  {:item (namespaced map)}
  [db [_ browser-id item-id]]
  (let [backup-item (r backup.subs/export-backup-item db browser-id item-id)]
       (r core.subs/use-query-params db browser-id {:item backup-item})))

(defn get-undo-delete-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (vector)
  [db [_ browser-id item-id]]
  (let [mutation-name  (r update.subs/get-mutation-name  db browser-id :undo-delete-item!)
        mutation-props (r get-undo-delete-item-mutation-props db browser-id item-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; -- Duplicate item queries --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-duplicate-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (map)
  ;  {:item (namespaced map)}
  [db [_ browser-id item-id]]
  ; Az item-browser engine az item-editor pluginhoz hasonlóan duplikáláskor az elem azonosítója
  ; helyett az elemet küldi el a szerver számára, hogy a két engine mutation függvényei hasonló
  ; paraméterezéssel működjenek.
  ; (az item-browser engine működéséhez elegendő lenne az elem azonosítóját elküldni duplikáláskor)
  (let [exported-item (r items.subs/export-item db browser-id item-id)]
       (r core.subs/use-query-params db browser-id {:item exported-item})))

(defn get-duplicate-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (vector)
  [db [_ browser-id item-id]]
  (let [mutation-name  (r update.subs/get-mutation-name     db browser-id :duplicate-item!)
        mutation-props (r get-duplicate-item-mutation-props db browser-id item-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; -- Update item queries -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-update-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (map)
  ;  {:item (namespaced map)}
  [db [_ browser-id item-id]]
  (let [exported-item (r items.subs/export-item db browser-id item-id)]
       (r core.subs/use-query-params db browser-id {:item exported-item})))

(defn get-update-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (vector)
  [db [_ browser-id item-id]]
  (let [mutation-name  (r update.subs/get-mutation-name  db browser-id :update-item!)
        mutation-props (r get-update-item-mutation-props db browser-id item-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))
