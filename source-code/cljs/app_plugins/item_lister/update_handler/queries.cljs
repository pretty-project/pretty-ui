
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.update-handler.queries
    (:require [app-plugins.item-lister.backup-handler.subs :as backup-handler.subs]
              [app-plugins.item-lister.update-handler.subs :as update-handler.subs]
              [x.app-core.api                              :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-delete-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-ids]]
  (let [mutation-name (r update-handler.subs/get-mutation-name db extension-id item-namespace :delete)]
       [:debug `(~(symbol mutation-name) ~{:item-ids item-ids})]))

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
  (let [exported-items (r backup-handler.subs/export-backup-items db extension-id item-namespace item-ids)]
       {:items exported-items}))

(defn get-undo-delete-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-ids]]
  (let [mutation-name  (r update-handler.subs/get-mutation-name db extension-id item-namespace :undo-delete)
        mutation-props (r get-undo-delete-items-mutation-props  db extension-id item-namespace item-ids)]
       [:debug `(~(symbol mutation-name) ~mutation-props)]))

(defn get-duplicate-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-ids]]
  (let [mutation-name (r update-handler.subs/get-mutation-name db extension-id item-namespace :duplicate)]
       [:debug `(~(symbol mutation-name) ~{:item-ids item-ids})]))

(defn get-undo-duplicate-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) copy-ids
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace copy-ids]]
  (let [mutation-name (r update-handler.subs/get-mutation-name db extension-id item-namespace :undo-duplicate)]
       [:debug `(~(symbol mutation-name) ~{:item-ids copy-ids})]))
