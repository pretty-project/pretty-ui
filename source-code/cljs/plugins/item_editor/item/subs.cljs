
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.item.subs
    (:require [x.app-core.api :refer [r]]
              [x.app-db.api   :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-current-item-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (r item-editor/get-current-item-id db :my-extension :my-type)
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (r core.subs/get-meta-item db extension-id item-namespace :item-id))

(defn get-current-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (get-in db [extension-id :item-editor/data-items]))

(defn export-current-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (namespaced map)
  [db [_ extension-id item-namespace]]
  (let [current-item (r get-current-item db extension-id item-namespace)]
       (db/document->namespaced-document current-item item-namespace)))

(defn export-copy-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (namespaced map)
  [db [_ extension-id item-namespace]]
  (let [current-item (r get-current-item db extension-id item-namespace)]
       (db/document->namespaced-document current-item item-namespace)))
