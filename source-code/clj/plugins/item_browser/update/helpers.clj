
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.update.helpers
    (:require [mid-fruits.keyword :as keyword]
              [mongo-db.api       :as mongo-db]
              [pathom.api         :as pathom]
              [x.server-core.api  :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item->path
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (namespaced map) item
  ;
  ; @example
  ;  (item-browser/item->path :my-extension :my-type {...})
  ;  =>
  ;  [{:my-type/id "parent-item"}]
  ;
  ; @return (maps in vector)
  [env extension-id item-namespace item]
  (let [path-key (pathom/env->param env :path-key)]
       (get item (keyword/add-namespace item-namespace path-key))))

(defn item->parent-link
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (namespaced map) item
  ;
  ; @example
  ;  (item-browser/item->parent-link :my-extension :my-type {...})
  ;  =>
  ;  {:my-type/id "parent-item"}
  ;
  ; @return (namespaced map)
  [env extension-id item-namespace item]
  (if-let [path (item->path extension-id item-namespace item)]
          (last path)))

(defn item->parent-id
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (namespaced map) item
  ;
  ; @example
  ;  (item-browser/item->parent-id :my-extension :my-type {...})
  ;  =>
  ;  "parent-item"
  ;
  ; @return (string)
  [env extension-id item-namespace item]
  (if-let [parent-link (item->parent-link extension-id item-namespace item)]
          (get parent-link (keyword/add-namespace item-namespace :id))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-id->path
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @example
  ;  (item-browser/item-id->path :my-extension :my-type "my-item")
  ;  =>
  ;  [{:my-type/id "parent-item"}]
  ;
  ; @return (namespaced maps in vector)
  [env extension-id item-namespace item-id]
  (let [collection-name @(a/subscribe [:item-browser/get-browser-prop extension-id item-namespace :collection-name])
        path-key         (pathom/env->param env :path-key)]
       (if-let [item (mongo-db/get-document-by-id collection-name item-id)]
               (get item (keyword/add-namespace item-namespace path-key)))))

(defn item-id->parent-link
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @example
  ;  (item-browser/item-id->parent-link :my-extension :my-type "my-item")
  ;  =>
  ;  {:my-type/id "parent-item"}
  ;
  ; @return (namespaced map)
  [env extension-id item-namespace item-id]
  (if-let [path (item-id->path extension-id item-namespace item-id)]
          (last path)))

(defn item-id->parent-id
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @example
  ;  (item-browser/item-id->parent-id :my-extension :my-type "my-item")
  ;  =>
  ;  "parent-item"
  ;
  ; @return (string)
  [env extension-id item-namespace item-id]
  (if-let [parent-link (item-id->parent-link extension-id item-namespace item-id)]
          (get parent-link (keyword/add-namespace item-namespace :id))))
