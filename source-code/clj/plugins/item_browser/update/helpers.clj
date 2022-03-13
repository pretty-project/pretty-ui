
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.update.helpers
    (:require [mid-fruits.keyword                :as keyword]
              [mongo-db.api                      :as mongo-db]
              [plugins.item-browser.core.helpers :as core.helpers]
              [x.server-core.api                 :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item->path
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (namespaced map) item
  ;
  ; @usage
  ;  (item-browser/item->path :my-extension :my-type {...})
  ;
  ; @return (maps in vector)
  [extension-id item-namespace item]
  (let [path-key @(a/subscribe [:item-browser/get-meta-item extension-id item-namespace :path-key])]
       (get item (keyword/add-namespace item-namespace path-key))))

(defn item->parent-link
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (namespaced map) item
  ;
  ; @usage
  ;  (item-browser/item->parent-link :my-extension :my-type {...})
  ;
  ; @return (namespaced map)
  [extension-id item-namespace item]
  (if-let [path (item->path extension-id item-namespace item)]
          (last path)))

(defn item->parent-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (namespaced map) item
  ;
  ; @usage
  ;  (item-browser/item->parent-id :my-extension :my-type {...})
  ;
  ; @return (string)
  [extension-id item-namespace item]
  (if-let [parent-link (item->parent-link extension-id item-namespace item)]
          (get parent-link (keyword/add-namespace item-namespace :id))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-id->path
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  (item-browser/item-id->path :my-extension :my-type "my-item")
  ;
  ; @return (maps in vector)
  [extension-id item-namespace item-id]
  (let [collection-name (core.helpers/collection-name extension-id item-namespace)
        path-key       @(a/subscribe [:item-browser/get-meta-item extension-id item-namespace :path-key])]
       (if-let [item (mongo-db/get-document-by-id collection-name item-id)]
               (get item (keyword/add-namespace item-namespace path-key)))))

(defn item-id->parent-link
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  (item-browser/item-id->parent-link :my-extension :my-type "my-item")
  ;
  ; @return (namespaced map)
  [extension-id item-namespace item-id]
  (if-let [path (item-id->path extension-id item-namespace item-id)]
          (last path)))

(defn item-id->parent-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  (item-browser/item-id->parent-id :my-extension :my-type "my-item")
  ;
  ; @return (string)
  [extension-id item-namespace item-id]
  (if-let [parent-link (item-id->parent-link extension-id item-namespace item-id)]
          (get parent-link (keyword/add-namespace item-namespace :id))))
