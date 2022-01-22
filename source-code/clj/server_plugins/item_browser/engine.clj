
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.4.8
; Compatibility: x4.5.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-browser.engine
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mongo-db.api       :as mongo-db]
              [pathom.api         :as pathom]
              [x.server-core.api  :as a]
              [mid-plugins.item-browser.engine :as engine]
              [server-plugins.item-lister.api  :as item-lister]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-browser.engine
(def BROWSER-PROPS-KEYS      engine/BROWSER-PROPS-KEYS)
(def browser-uri             engine/browser-uri)
(def request-id              engine/request-id)
(def data-item-path          engine/data-item-path)
(def meta-item-path          engine/meta-item-path)
(def resolver-id             engine/resolver-id)
(def collection-name         engine/collection-name)
(def route-id                engine/route-id)
(def extended-route-id       engine/extended-route-id)
(def route-template          engine/route-template)
(def extended-route-template engine/extended-route-template)
(def load-extension-event    engine/load-extension-event)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->item-links
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (item-browser/env->item-links {...} :my-extension :my-type)
  ;
  ; @return (maps in vector)
  [env extension-id item-namespace]
  (let [collection-name (collection-name extension-id)
        items-key       (keyword/add-namespace item-namespace :items)
        item-id         (pathom/env->param env :item-id)]
       (if-let [document (mongo-db/get-document-by-id collection-name item-id)]
               (get document items-key)
               ; WARNING!
               ; Az env->item-links függvény visszatérési értéke egy vektor kell legyen!
               ; Ha az item-links értéke nil, akkor az így létrehozott filter-pattern alkalmazásával
               ; az adatbázis a kollekció összes dokumentumát kiszolgálná eredményként!
               (return []))))

(defn env->sort-pattern
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (item-browser/env->sort-pattern {...} :my-extension :my-type)
  ;
  ; @return (map)
  [env extension-id item-namespace]
  (item-lister/env->sort-pattern env extension-id item-namespace))

(defn env->search-pattern
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (item-browser/env->search-pattern {...} :my-extension :my-type)
  ;
  ; @return (map)
  ;  {:$or (maps in vector)}
  [env extension-id item-namespace]
  (item-lister/env->search-pattern env extension-id item-namespace))

(defn env->pipeline-props
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-browser/env->pipeline-props {...} :my-extension :my-type)
  ;  =>
  ;  {:max-count 20
  ;   :skip       0
  ;   :filter-pattern {:$or [{:my-type/my-key "..."} {...}]}
  ;   :search-pattern {:$or [{:my-type/name   "..."} {...}]}
  ;   :sort-pattern   {:my-type/name 1}}
  ;
  ; @return (map)
  [env extension-id item-namespace]
  ; Az item-lister plugin env->pipeline-props függvényénét kiegészíti, az kollekció elemeinek
  ; szűrésével, hogy a csak azok az elemek jelenjenek meg a item-browser böngészőben, amelyek
  ; aktuálisan böngészett elem :namespace/items vektorában fel vannak sorolva.
  (let [item-links     (env->item-links   env extension-id item-namespace)
        filter-pattern (pathom/env->param env :filter-pattern)
        env            (pathom/env<-param env :filter-pattern {:$or item-links})]
       (item-lister/env->pipeline-props env extension-id item-namespace)))

(defn env->get-pipeline
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (item-browser/env->get-pipeline {...} :my-extension :my-type)
  ;
  ; @return (maps in vector)
  [env extension-id item-namespace]
  (let [pipeline-props (env->pipeline-props env extension-id item-namespace)]
       (mongo-db/get-pipeline pipeline-props)))

(defn env->count-pipeline
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (item-browser/env->count-pipeline {...} :my-extension :my-type)
  ;
  ; @return (maps in vector)
  [env extension-id item-namespace]
  (let [pipeline-props (env->pipeline-props env extension-id item-namespace)]
       (mongo-db/count-pipeline pipeline-props)))



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
  (let [path-key (a/subscribed [:item-browser/get-meta-item extension-id item-namespace :path-key])]
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
