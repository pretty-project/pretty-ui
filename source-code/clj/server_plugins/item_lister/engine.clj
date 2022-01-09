
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.7.8
; Compatibility: x4.5.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-lister.engine
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.vector  :as vector]
              [mongo-db.api       :as mongo-db]
              [pathom.api         :as pathom]
              [mid-plugins.item-lister.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-lister.engine
(def request-id           engine/request-id)
(def mutation-name        engine/mutation-name)
(def resolver-id          engine/resolver-id)
(def new-item-uri         engine/new-item-uri)
(def add-new-item-event   engine/add-new-item-event)
(def route-id             engine/route-id)
(def route-template       engine/route-template)
(def dialog-id            engine/dialog-id)
(def load-extension-event engine/load-extension-event)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  (let [order-by (pathom/env->param env :order-by)]
       (case order-by :by-name-ascending  [[(keyword/add-namespace item-namespace :name)         1]]
                      :by-name-descending [[(keyword/add-namespace item-namespace :name)        -1]]
                      :by-date-ascending  [[(keyword/add-namespace item-namespace :modified-at)  1]]
                      :by-date-descending [[(keyword/add-namespace item-namespace :modified-at) -1]]
                      ; Default
                      [[(keyword/add-namespace item-namespace :name) 1]])))

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
  (let [search-keys (pathom/env->param env :search-keys)
        search-term (pathom/env->param env :search-term)]
       {:or (vector/->items search-keys #(return [(keyword/add-namespace item-namespace %) search-term]))}))

(defn env->pipeline-props
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-lister/env->pipeline-props {...} :my-extension :my-type)
  ;  =>
  ;  {:max-count 20
  ;   :skip       0
  ;   :filter-pattern {:or [[:my-type/my-key "..."] [...]]}
  ;   :search-pattern {:or [[:my-type/name   "..."] [...]]}
  ;   :sort-pattern   [[:my-type/name 1]]}
  ;
  ; @return (map)
  ;  {:filter-pattern (map)
  ;   :max-count (integer)
  ;   :search-pattern (map)
  ;   :skip (integer)
  ;   :sort-pattern (vectors in vector)}
  [env extension-id item-namespace]
  {:max-count      (pathom/env->param   env :download-limit)
   :skip           (pathom/env->param   env :downloaded-item-count)
   :filter-pattern (pathom/env->param   env :filter-pattern)
   :search-pattern (env->search-pattern env extension-id item-namespace)
   :sort-pattern   (env->sort-pattern   env extension-id item-namespace)})

(defn env->get-pipeline
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (item-lister/env->get-pipeline {...} :my-extension :my-type)
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
  ;  (item-lister/env->count-pipeline {...} :my-extension :my-type)
  ;
  ; @return (maps in vector)
  [env extension-id item-namespace]
  (let [pipeline-props (env->pipeline-props env extension-id item-namespace)]
       (mongo-db/count-pipeline pipeline-props)))
