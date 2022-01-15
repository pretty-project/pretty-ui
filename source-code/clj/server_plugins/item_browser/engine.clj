
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.3.6
; Compatibility: x4.5.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-browser.engine
    (:require [mid-fruits.keyword :as keyword]
              [mongo-db.api       :as mongo-db]
              [pathom.api         :as pathom]
              [mid-plugins.item-browser.engine :as engine]
              [server-plugins.item-lister.api  :as item-lister]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-browser.engine
(def request-id              engine/request-id)
(def resolver-id             engine/resolver-id)
(def collection-name         engine/collection-name)
(def route-id                engine/route-id)
(def extended-route-id       engine/extended-route-id)
(def route-template          engine/route-template)
(def extended-route-template engine/extended-route-template)
(def go-up-event             engine/go-up-event)
(def go-home-event           engine/go-home-event)
(def load-extension-event    engine/load-extension-event)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->sort-pattern
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (item-browser/env->sort-pattern {...} :my-extension :my-type)
  ;
  ; @return (vectors in vector)
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
  ;  {:or (vectors in vector)}
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
  ;   :filter-pattern {:or [{:my-type/my-key "..."} {...}]}
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
  (let [item-id (pathom/env->param env :item-id)
        collection-name (collection-name extension-id)
        item (mongo-db/get-document-by-id collection-name item-id)
        items-key (keyword/add-namespace item-namespace :items)
        item-links (get item items-key)
        ;_ (println "tie:" (str item-links))
        filter-pattern (pathom/env->param env :filter-pattern)
        ;filter-pattern {:and {}}
        filter-pattern {:and [{:or item-links}]}
        filter-pattern {:and [{:or [{:a "b"} {:a "b"} {:a "b"}]}]}]
       ;(println (str (mid-fruits.map/->>values {:a "A" :b "B" :c [:x "y" {:d "D"}]} keyword)))
       (merge (item-lister/env->pipeline-props env extension-id item-namespace))))
              ;{:filter-pattern filter-pattern})))

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
