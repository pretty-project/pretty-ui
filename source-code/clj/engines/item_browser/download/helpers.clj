
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.download.helpers
    (:require [candy.api                            :refer [return]]
              [engines.item-lister.download.helpers :as download.helpers]
              [keyword.api                          :as keyword]
              [mongo-db.api                         :as mongo-db]
              [pathom.api                           :as pathom]
              [re-frame.api                         :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.item-lister.download.helpers
(def env->pipeline-options download.helpers/env->pipeline-options)
(def env->sort-pattern     download.helpers/env->sort-pattern)
(def env->search-pattern   download.helpers/env->search-pattern)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->item-links
  ; @param (map) env
  ; @param (keyword) browser-id
  ;
  ; @usage
  ; (env->item-links {...} :my-browser)
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

(defn env->pipeline-props
  ; @param (map) env
  ; @param (keyword) browser-id
  ;
  ; @example
  ; (env->pipeline-props {...} :my-browser)
  ; =>
  ; {:max-count 20
  ;  :skip       0
  ;  :filter-pattern {:$or [{:my-type/my-key "..."} {...}]}
  ;  :search-pattern {:$or [{:my-type/name   "..."} {...}]}
  ;  :sort-pattern   {:my-type/name 1}}
  ;
  ; @return (map)
  [env browser-id]
  ; Az item-lister engine env->pipeline-props függvényét kiegészíti a kollekció elemeinek
  ; szűrésével, hogy a csak azok az elemek jelenjenek meg a item-browser böngészőben, amelyek
  ; az aktuálisan böngészett elem items-key kulcshoz tartozó vektorában fel vannak sorolva.
  (let [item-links     (env->item-links env browser-id)
        filter-pattern (if-let [filter-pattern (pathom/env->param env :filter-pattern)]
                               {:$and [filter-pattern {:$or item-links}]}
                               {:$or item-links})
        env            (pathom/env<-param env :filter-pattern filter-pattern)]
       (download.helpers/env->pipeline-props env browser-id)))

(defn env->get-pipeline
  ; @param (map) env
  ; @param (keyword) browser-id
  ;
  ; @usage
  ; (env->get-pipeline {...} :my-browser)
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
  ; (env->count-pipeline {...} :my-browser)
  ;
  ; @return (maps in vector)
  [env browser-id]
  (let [pipeline-props (env->pipeline-props env browser-id)]
       (mongo-db/count-pipeline pipeline-props)))
