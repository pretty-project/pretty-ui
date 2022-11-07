
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.download.helpers
    (:require [mid-fruits.candy        :refer [return]]
              [mid-fruits.keyword      :as keyword]
              [mongo-db.api            :as mongo-db]
              [pathom.api              :as pathom]
              [engines.item-lister.api :as item-lister]
              [re-frame.api            :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->item-links
  ; @param (map) env
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  (env->item-links {...} :my-browser)
  ;
  ; @return (maps in vector)
  [env browser-id]
  (let [collection-name @(r/subscribe [:item-browser/get-browser-prop browser-id :collection-name])
        item-namespace  @(r/subscribe [:item-browser/get-browser-prop browser-id :item-namespace])
        items-key        (pathom/env->param env :items-key)
        item-id          (pathom/env->param env :item-id)]
       (if-let [document (mongo-db/get-document-by-id collection-name item-id)]
               (get document (keyword/add-namespace item-namespace items-key))
               ; WARNING!
               ; Az env->item-links függvény visszatérési értéke ...
               ; ... minden esetben vektor típus kell legyen!
               ; ... nem lehet NIL, különben az adatbázis a kollekció összes dokumentumát kiszolgálná eredményként!
               (return []))))

(defn env->sort-pattern
  ; @param (map) env
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  (env->sort-pattern {...} :my-browser)
  ;
  ; @return (map)
  [env browser-id]
  (item-lister/env->sort-pattern env browser-id))

(defn env->search-pattern
  ; @param (map) env
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  (env->search-pattern {...} :my-browser)
  ;
  ; @return (map)
  ;  {:$or (maps in vector)}
  [env browser-id]
  (item-lister/env->search-pattern env browser-id))

(defn env->pipeline-props
  ; @param (map) env
  ; @param (keyword) browser-id
  ;
  ; @example
  ;  (env->pipeline-props {...} :my-browser)
  ;  =>
  ;  {:max-count 20
  ;   :skip       0
  ;   :filter-pattern {:$or [{:my-type/my-key "..."} {...}]}
  ;   :search-pattern {:$or [{:my-type/name   "..."} {...}]}
  ;   :sort-pattern   {:my-type/name 1}}
  ;
  ; @return (map)
  [env browser-id]
  ; Az item-lister engine env->pipeline-props függvényét kiegészíti a kollekció elemeinek
  ; szűrésével, hogy a csak azok az elemek jelenjenek meg a item-browser böngészőben, amelyek
  ; az aktuálisan böngészett elem :namespace/items vektorában fel vannak sorolva.
  (let [item-links     (env->item-links env browser-id)
        filter-pattern (if-let [filter-pattern (pathom/env->param env :filter-pattern)]
                               {:$and [filter-pattern {:$or item-links}]}
                               {:$or item-links})
        env            (pathom/env<-param env :filter-pattern filter-pattern)]
       (item-lister/env->pipeline-props env browser-id)))

(defn env->get-pipeline
  ; @param (map) env
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  (env->get-pipeline {...} :my-browser)
  ;
  ; @return (maps in vector)
  [env browser-id]
  (let [pipeline-props (env->pipeline-props env browser-id)]
       (mongo-db/get-pipeline pipeline-props)))

(defn env->count-pipeline
  ; @param (map) env
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  (env->count-pipeline {...} :my-browser)
  ;
  ; @return (maps in vector)
  [env browser-id]
  (let [pipeline-props (env->pipeline-props env browser-id)]
       (mongo-db/count-pipeline pipeline-props)))
