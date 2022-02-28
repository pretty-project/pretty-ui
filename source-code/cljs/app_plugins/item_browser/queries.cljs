
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.queries
    (:require [x.app-core.api :as a :refer [r]]
              [x.app-db.api   :as db]
              [app-plugins.item-browser.engine :as engine]
              [app-plugins.item-browser.subs   :as subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (let [resolver-id     (engine/resolver-id extension-id item-namespace :get)
        current-item-id (r subs/get-current-item-id db extension-id item-namespace)]
       [:debug `(~resolver-id ~{:item-id current-item-id})]))

(defn get-delete-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-id]]
  (let [mutation-name (engine/mutation-name extension-id item-namespace :delete)]
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
  (let [mutation-name (engine/mutation-name         extension-id item-namespace :undo-delete)
        backup-item   (r subs/export-backup-item db extension-id item-namespace item-id)]
       [:debug `(~(symbol mutation-name) ~{:item backup-item})]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [_ [_ extension-id item-namespace server-response]]
  (let [resolver-id (engine/resolver-id extension-id item-namespace :get)
        document    (get server-response resolver-id)]
       (db/document->document-namespaced? document)))
