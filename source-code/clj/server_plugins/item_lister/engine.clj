
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.7.8
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-lister.engine
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.math    :as math]
              [mid-fruits.vector  :as vector]
              [mongo-db.api       :as mongo-db]
              [pathom.api         :as pathom]
              [mid-plugins.item-lister.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-lister.engine
(def request-id           engine/request-id)
(def data-item-path       engine/data-item-path)
(def meta-item-path       engine/meta-item-path)
(def mutation-name        engine/mutation-name)
(def resolver-id          engine/resolver-id)
(def collection-name      engine/collection-name)
(def new-item-uri         engine/new-item-uri)
(def add-new-item-event   engine/add-new-item-event)
(def route-id             engine/route-id)
(def route-template       engine/route-template)
(def dialog-id            engine/dialog-id)
(def load-extension-event engine/load-extension-event)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- env->max-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;
  ; @return (integer)
  [env]
  (let [download-limit (pathom/env->param env :download-limit)]
       (if-let [reload-items? (pathom/env->param env :reload-items?)]
               (let [downloaded-item-count (pathom/env->param env :downloaded-item-count)
                     ; A downloaded-item-count értéke nem lehet 0, mert akkor matematikai
                     ; szempontból nem tartozna az első tartományba (1-20 elem) és a math/domain-ceil
                     ; függvény visszatérési értéke is 0 lenne!
                     downloaded-item-count (max downloaded-item-count 1)]
                    (math/domain-ceil downloaded-item-count download-limit))
               (return download-limit))))

(defn- env->skip
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;
  ; @return (integer)
  [env]
  (if-let [reload-items? (pathom/env->param env :reload-items?)]
          (return 0)
          (pathom/env->param env :downloaded-item-count)))



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
  ;  {:my-type/name 1}
  ;
  ; @return (map)
  [env _ item-namespace]
  (let [order-by (pathom/env->param env :order-by)]
       (case order-by :by-name-ascending  {(keyword/add-namespace item-namespace :name)         1}
                      :by-name-descending {(keyword/add-namespace item-namespace :name)        -1}
                      :by-date-ascending  {(keyword/add-namespace item-namespace :modified-at)  1}
                      :by-date-descending {(keyword/add-namespace item-namespace :modified-at) -1}
                      ; Default
                      {(keyword/add-namespace item-namespace :name) 1})))

(defn env->search-pattern
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-lister/env->search-pattern {...} :my-extension :my-type)
  ;  =>
  ;  {:$or [{:my-type/name "..."} {...}]}
  ;
  ; @return (map)
  ;  {:$or (maps in vector)}
  [env _ item-namespace]
  (let [search-keys (pathom/env->param env :search-keys)
        search-term (pathom/env->param env :search-term)]
       {:$or (vector/->items search-keys #(return {(keyword/add-namespace item-namespace %) search-term}))}))

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
  ;   :filter-pattern {:$or [{:my-type/my-key "..."} {...}]}
  ;   :search-pattern {:$or [{:my-type/name   "..."} {...}]}
  ;   :sort-pattern   {:my-type/name 1}}
  ;
  ; @return (map)
  ;  {:field-pattern (map)
  ;   :filter-pattern (map)
  ;   :max-count (integer)
  ;   :search-pattern (map)
  ;   :skip (integer)
  ;   :sort-pattern (map)}
  [env extension-id item-namespace]
  {:max-count      (env->max-count      env)
   :skip           (env->skip           env)
   :field-pattern  (pathom/env->param   env :field-pattern)
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
