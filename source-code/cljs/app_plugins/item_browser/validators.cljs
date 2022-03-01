
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.validators
    (:require [mid-fruits.string :as string]
              [x.app-db.api      :as db]
              [app-plugins.item-browser.engine :as engine]))



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

(defn delete-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [_ [_ extension-id item-namespace server-response]]
  (let [mutation-name (engine/mutation-name extension-id item-namespace :delete)
        document-id   (get server-response (symbol mutation-name))]
       (string/nonempty? document-id)))

(defn undo-delete-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [_ [_ extension-id item-namespace server-response]]
  (let [mutation-name (engine/mutation-name extension-id item-namespace :undo-delete)
        document      (get server-response (symbol mutation-name))]
       (db/document->document-namespaced? document)))

(defn duplicate-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [_ [_ extension-id item-namespace server-response]]
  (let [mutation-name (engine/mutation-name extension-id item-namespace :duplicate)
        document      (get server-response (symbol mutation-name))]
       (db/document->document-namespaced? document)))

(defn undo-duplicate-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [_ [_ extension-id item-namespace server-response]]
  (let [mutation-name (engine/mutation-name extension-id item-namespace :undo-duplicate)
        document-id   (get server-response (symbol mutation-name))]
       (string/nonempty? document-id)))

(defn update-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [_ [_ extension-id item-namespace server-response]]
  (let [mutation-name (engine/mutation-name extension-id item-namespace :update)
        document      (get server-response (symbol mutation-name))]
       (db/document->document-namespaced? document)))
