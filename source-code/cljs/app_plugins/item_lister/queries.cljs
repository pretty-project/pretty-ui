
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.6.8
; Compatibility: x4.5.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.queries
    (:require [mid-fruits.candy  :refer [param return]]
              [x.app-core.api    :as a :refer [r]]
              [app-plugins.item-lister.engine :as engine]
              [app-plugins.item-lister.subs   :as subs]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:downloaded-item-count (integer)
  ;   :download-limit (integer)
  ;   :filter-pattern (map)
  ;   :order-by (keyword)
  ;   :search-keys (keywords in vector)
  ;   :search-term (string)}
  [db [_ extension-id item-namespace]]
  {:downloaded-item-count (r subs/get-downloaded-item-count db extension-id)
   :download-limit        (r subs/get-meta-value            db extension-id item-namespace :download-limit)
   :filter-pattern        (r subs/get-meta-value            db extension-id item-namespace :filter-pattern)
   :order-by              (r subs/get-meta-value            db extension-id item-namespace :order-by)
   :search-keys           (r subs/get-meta-value            db extension-id item-namespace :search-keys)
   :search-term           (r subs/get-search-term           db extension-id item-namespace)})

(defn get-delete-selected-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (let [mutation-name (engine/mutation-name            extension-id item-namespace :delete)
        item-ids      (r subs/get-selected-item-ids db extension-id item-namespace)]
       [`(~(symbol mutation-name) ~{:item-ids item-ids})]))

(defn get-undo-delete-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-ids]]
  (let [mutation-name  (engine/mutation-name          extension-id item-namespace :undo-delete)
        exported-items (r subs/export-backup-items db extension-id item-namespace item-ids)]
       [`(~(symbol mutation-name) ~{:items exported-items})]))

(defn get-mark-selected-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) mark-props
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace mark-props]]
  (let [mutation-name  (engine/mutation-name          extension-id item-namespace :merge)
        exported-items (r subs/export-marked-items db extension-id item-namespace mark-props)]
       [`(~(symbol mutation-name) ~{:items exported-items})]))

(defn get-undo-mark-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) mark-props
  ; @param (strings in vector) item-ids
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace mark-props item-ids]]
  (let [mutation-name  (engine/mutation-name            extension-id item-namespace :merge)
        exported-items (r subs/export-unmarked-items db extension-id item-namespace mark-props item-ids)]
       [`(~(symbol mutation-name) ~{:items exported-items})]))

(defn get-request-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (let [resolver-id    (engine/resolver-id      extension-id item-namespace)
        resolver-props (r get-resolver-props db extension-id item-namespace)]
       [`(~resolver-id ~resolver-props)]))