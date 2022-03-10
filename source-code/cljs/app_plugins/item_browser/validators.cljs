
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.validators
    (:require [app-plugins.item-browser.engine :as engine]
              [app-plugins.item-browser.subs   :as subs]
              [mid-fruits.string               :as string]
              [x.app-core.api                  :as a :refer [r]]
              [x.app-db.api                    :as db]))



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
  [db [_ extension-id item-namespace server-response]]
  (let [resolver-id (r subs/get-resolver-id db extension-id item-namespace :get)
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
  [db [_ extension-id item-namespace server-response]]
  (let [mutation-name (r subs/get-mutation-name db extension-id item-namespace :delete)
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
  [db [_ extension-id item-namespace server-response]]
  (let [mutation-name (r subs/get-mutation-name db extension-id item-namespace :undo-delete)
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
  [db [_ extension-id item-namespace server-response]]
  (let [mutation-name (r subs/get-mutation-name db extension-id item-namespace :duplicate)
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
  [db [_ extension-id item-namespace server-response]]
  (let [mutation-name (r subs/get-mutation-name db extension-id item-namespace :undo-duplicate)
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
  [db [_ extension-id item-namespace server-response]]
  (let [mutation-name (r subs/get-mutation-name db extension-id item-namespace :update)
        document      (get server-response (symbol mutation-name))]
       (db/document->document-namespaced? document)))
