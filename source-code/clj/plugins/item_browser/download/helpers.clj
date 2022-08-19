
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.download.helpers
    (:require [mid-fruits.candy        :refer [return]]
              [mid-fruits.keyword      :as keyword]
              [mongo-db.api            :as mongo-db]
              [pathom.api              :as pathom]
              [plugins.item-lister.api :as item-lister]
              [x.server-core.api       :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->item-links
  ; @param (map) env
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  (item-browser/env->item-links {...} :my-browser)
  ;
  ; @return (maps in vector)
  [env browser-id]
  (let [collection-name @(a/subscribe [:item-browser/get-browser-prop browser-id :collection-name])
        item-namespace  @(a/subscribe [:item-browser/get-browser-prop browser-id :item-namespace])
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
  ;  (item-browser/env->sort-pattern {...} :my-browser)
  ;
  ; @return (map)
  [env browser-id]
  (item-lister/env->sort-pattern env browser-id))

(defn env->search-pattern
  ; @param (map) env
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  (item-browser/env->search-pattern {...} :my-browser)
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
  ;  (item-browser/env->pipeline-props {...} :my-browser)
  ;  =>
  ;  {:max-count 20
  ;   :skip       0
  ;   :filter-pattern {:$or [{:my-type/my-key "..."} {...}]}
  ;   :search-pattern {:$or [{:my-type/name   "..."} {...}]}
  ;   :sort-pattern   {:my-type/name 1}}
  ;
  ; @return (map)
  [env browser-id]
  ; Az item-lister plugin env->pipeline-props függvényét kiegészíti a kollekció elemeinek
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
  ;  (item-browser/env->get-pipeline {...} :my-browser)
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
  ;  (item-browser/env->count-pipeline {...} :my-browser)
  ;
  ; @return (maps in vector)
  [env browser-id]
  (let [pipeline-props (env->pipeline-props env browser-id)]
       (mongo-db/count-pipeline pipeline-props)))
