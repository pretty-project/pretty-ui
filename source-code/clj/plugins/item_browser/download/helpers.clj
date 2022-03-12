
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.download.helpers
    (:require [mid-fruits.candy                    :refer [param return]]
              [mid-fruits.keyword                  :as keyword]
              [mongo-db.api                        :as mongo-db]
              [pathom.api                          :as pathom]
              [plugins.item-browser.engine.helpers :as engine.helpers]
              [plugins.item-lister.api             :as item-lister]
              [x.server-core.api                   :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->item-links
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (env->item-links {...} :my-extension :my-type)
  ;
  ; @return (maps in vector)
  [env extension-id item-namespace]
  (let [collection-name (engine.helpers/collection-name extension-id item-namespace)
        items-key       (keyword/add-namespace item-namespace :items)
        item-id         (pathom/env->param     env            :item-id)]
       (if-let [document (mongo-db/get-document-by-id collection-name item-id)]
               (get document items-key)
               ; WARNING!
               ; - Az env->item-links függvény visszatérési értéke egy vektor kell legyen!
               ; - Ha az item-links értéke nil, akkor az adatbázis a kollekció összes dokumentumát
               ;   kiszolgálná eredményként!
               (return []))))

(defn env->sort-pattern
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (env->sort-pattern {...} :my-extension :my-type)
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
  ;  (env->search-pattern {...} :my-extension :my-type)
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
  ;  (env->pipeline-props {...} :my-extension :my-type)
  ;  =>
  ;  {:max-count 20
  ;   :skip       0
  ;   :filter-pattern {:$or [{:my-type/my-key "..."} {...}]}
  ;   :search-pattern {:$or [{:my-type/name   "..."} {...}]}
  ;   :sort-pattern   {:my-type/name 1}}
  ;
  ; @return (map)
  [env extension-id item-namespace]
  ; Az item-lister plugin env->pipeline-props függvényét kiegészíti a kollekció elemeinek
  ; szűrésével, hogy a csak azok az elemek jelenjenek meg a item-browser böngészőben, amelyek
  ; az aktuálisan böngészett elem :namespace/items vektorában fel vannak sorolva.
  (let [item-links     (env->item-links env extension-id item-namespace)
        filter-pattern (if-let [filter-pattern (pathom/env->param env :filter-pattern)]
                               {:$and [filter-pattern {:$or item-links}]}
                               {:$or item-links})
        env            (pathom/env<-param env :filter-pattern filter-pattern)]
       (item-lister/env->pipeline-props env extension-id item-namespace)))

(defn env->get-pipeline
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (env->get-pipeline {...} :my-extension :my-type)
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
  ;  (env->count-pipeline {...} :my-extension :my-type)
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
  ;  (item->path :my-extension :my-type {...})
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
  ;  (item->parent-link :my-extension :my-type {...})
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
  ;  (item->parent-id :my-extension :my-type {...})
  ;
  ; @return (string)
  [extension-id item-namespace item]
  (if-let [parent-link (item->parent-link extension-id item-namespace item)]
          (get parent-link (keyword/add-namespace item-namespace :id))))
