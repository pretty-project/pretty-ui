
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.6.4
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-lister.engine
    (:require [mid-fruits.keyword :as keyword]
              [mongo-db.api       :as mongo-db]
              [pathom.api         :as pathom]
              [mid-plugins.item-lister.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-lister.engine
(def request-id         engine/request-id)
(def mutation-name      engine/mutation-name)
(def resolver-id        engine/resolver-id)
(def new-item-uri       engine/new-item-uri)
(def add-new-item-event engine/add-new-item-event)
(def route-id           engine/route-id)
(def route-template     engine/route-template)
(def render-event       engine/render-event)
(def dialog-id          engine/dialog-id)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn search-props->search-pipeline
  ; @param (map) search-props
  ;  {:filter-pattern (map)
  ;   :max-count (integer)
  ;   :search-pattern (map)
  ;   :skip (integer)
  ;   :sort-pattern (vectors in vector)}
  ;
  ; @usage
  ;  (item-lister/search-props->search-pipeline {...})
  ;
  ; @return (maps in vector)
  [{:keys [filter-pattern max-count search-pattern skip sort-pattern]}]
  (let [filter-query (mongo-db/filter-pattern->filter-query filter-pattern)
        search-query (mongo-db/search-pattern->search-query search-pattern)
        sort         (mongo-db/sort-pattern->sort-query     sort-pattern)]
       [{"$match" {"$and" [filter-query search-query]}}
        {"$sort"  sort}
        {"$skip"  skip}
        {"$limit" max-count}]))

(defn search-props->count-pipeline
  ; @param (map) search-props
  ;  {:filter-pattern (map)
  ;   :search-pattern (map)}
  ;
  ; @usage
  ;  (item-lister/search-props->count-pipeline {...})
  ;
  ; @return (maps in vector)
  [{:keys [filter-pattern search-pattern] :as search-props}]
  (let [filter-query (mongo-db/filter-pattern->filter-query filter-pattern)
        search-query (mongo-db/search-pattern->search-query search-pattern)]
       [{"$match" {"$and" [filter-query search-query]}}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->filter-pattern
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-lister/env->filter-pattern {...} :my-extension :my-type)
  ;  =>
  ;  {:and [[:my-type/archived? true]]}
  ;
  ; @return (map)
  ;  {:and (vectors in vector)
  ;   :or (vectors in vector)}
  [env _ item-namespace]
  (let [resolver-props (pathom/env->params env)
        filter-id      (get resolver-props :filter)]
       (case filter-id :archived-items {:and [[(keyword/add-namespace item-namespace :archived?) true]]}
                       :favorite-items {:and [[(keyword/add-namespace item-namespace :favorite?) true]]
                                        :or  [[(keyword/add-namespace item-namespace :archived?) false]
                                              [(keyword/add-namespace item-namespace :archived?) nil]]}
                       ; If no filter selected ...
                       {:or [[(keyword/add-namespace item-namespace :archived?) false]
                             [(keyword/add-namespace item-namespace :archived?) nil]]})))

(defn env->sort-pattern
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-lister/env->sort-pattern {...} :my-extension :my-type)
  ;  =>
  ;  [[:my-type/name 1]]
  ;
  ; @return (vectors in vector)
  [env _ item-namespace]
  (let [resolver-props (pathom/env->params env)
        order-by       (get resolver-props :order-by)]
       (case order-by :by-name-ascending  [[(keyword/add-namespace item-namespace :name)         1]]
                      :by-name-descending [[(keyword/add-namespace item-namespace :name)        -1]]
                      :by-date-ascending  [[(keyword/add-namespace item-namespace :modified-at)  1]]
                      :by-date-descending [[(keyword/add-namespace item-namespace :modified-at) -1]])))

(defn env->search-pattern
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-lister/env->search-pattern {...} :my-extension :my-type)
  ;  =>
  ;  {:or [[:my-type/name "..."] [...]]}
  ;
  ; @return (map)
  ;  {:or (vectors in vector)}
  [env _ item-namespace]
  (let [resolver-props (pathom/env->params env)
        search-keys    (get resolver-props :search-keys)
        search-term    (get resolver-props :search-term)]
       {:or (vec (reduce #(conj %1 [(keyword/add-namespace item-namespace %2) search-term]) [] search-keys))}))

(defn env->search-props
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-lister/env->search-props {...} :my-extension :my-type)
  ;  =>
  ;  {:max-count 20
  ;   :skip 0
  ;   :filter-pattern {:and [[:my-type/archived? true]]}
  ;   :search-pattern {:or [[:my-type/name "..."] [...]]}
  ;   :sort-pattern   [[:my-type/name 1]]}
  ;
  ; @return (map)
  ;  {:filter-pattern (map)
  ;   :max-count (integer)
  ;   :search-pattern (map)
  ;   :skip (integer)
  ;   :sort-pattern (vectors in vector)}
  [env extension-id item-namespace]
  (let [resolver-props (pathom/env->params env)]
       {:max-count      (get resolver-props :download-limit)
        :skip           (get resolver-props :downloaded-item-count)
        :filter-pattern (env->filter-pattern env extension-id item-namespace)
        :search-pattern (env->search-pattern env extension-id item-namespace)
        :sort-pattern   (env->sort-pattern   env extension-id item-namespace)}))

(defn env->search-pipeline
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (item-lister/env->search-pipeline {...} :my-extension :my-type)
  ;
  ; @return (maps in vector)
  [env extension-id item-namespace]
  (let [search-props (env->search-props env extension-id item-namespace)]
       (search-props->search-pipeline search-props)))

(defn env->count-pipeline
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (item-lister/env->count-pipeline {...} :my-extension :my-type)
  ;
  ; @return (maps in vector)
  [env extension-id item-namespace]
  (let [search-props (env->search-props env extension-id item-namespace)]
       (search-props->count-pipeline search-props)))
