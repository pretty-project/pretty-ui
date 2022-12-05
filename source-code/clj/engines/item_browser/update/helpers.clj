
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.update.helpers
    (:require [keyword.api  :as keyword]
              [mongo-db.api :as mongo-db]
              [pathom.api   :as pathom]
              [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item->path
  ; @param (map) env
  ; @param (keyword) browser-id
  ; @param (namespaced map) item
  ;
  ; @example
  ; (item->path :my-browser {...})
  ; =>
  ; [{:my-type/id "parent-item"}]
  ;
  ; @return (maps in vector)
  [env browser-id item]
  (let [item-namespace @(r/subscribe [:item-browser/get-browser-prop browser-id :item-namespace])
        path-key        (pathom/env->param env :path-key)]
       (get item (keyword/add-namespace item-namespace path-key))))

(defn item->parent-link
  ; @param (map) env
  ; @param (keyword) browser-id
  ; @param (namespaced map) item
  ;
  ; @example
  ; (item->parent-link :my-browser {...})
  ; =>
  ; {:my-type/id "parent-item"}
  ;
  ; @return (namespaced map)
  [env browser-id item]
  (if-let [path (item->path env browser-id item)]
          (last path)))

(defn item->parent-id
  ; @param (map) env
  ; @param (keyword) browser-id
  ; @param (namespaced map) item
  ;
  ; @example
  ; (item->parent-id :my-browser {...})
  ; =>
  ; "parent-item"
  ;
  ; @return (string)
  [env browser-id item]
  (if-let [parent-link (item->parent-link env browser-id item)]
          (let [item-namespace @(r/subscribe [:item-browser/get-browser-prop browser-id :item-namespace])]
               (get parent-link (keyword/add-namespace item-namespace :id)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-id->path
  ; @param (map) env
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @example
  ; (item-id->path :my-browser "my-item")
  ; =>
  ; [{:my-type/id "parent-item"}]
  ;
  ; @return (namespaced maps in vector)
  [env browser-id item-id]
  (let [collection-name @(r/subscribe [:item-browser/get-browser-prop browser-id :collection-name])
        item-namespace  @(r/subscribe [:item-browser/get-browser-prop browser-id :item-namespace])
        path-key         (pathom/env->param env :path-key)]
       (if-let [item (mongo-db/get-document-by-id collection-name item-id)]
               (get item (keyword/add-namespace item-namespace path-key)))))

(defn item-id->parent-link
  ; @param (map) env
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @example
  ; (item-id->parent-link :my-browser "my-item")
  ; =>
  ; {:my-type/id "parent-item"}
  ;
  ; @return (namespaced map)
  [env browser-id item-id]
  (if-let [path (item-id->path env browser-id item-id)]
          (last path)))

(defn item-id->parent-id
  ; @param (map) env
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @example
  ; (item-id->parent-id :my-browser "my-item")
  ; =>
  ; "parent-item"
  ;
  ; @return (string)
  [env browser-id item-id]
  (if-let [parent-link (item-id->parent-link env browser-id item-id)]
          (let [item-namespace @(r/subscribe [:item-browser/get-browser-prop browser-id :item-namespace])]
               (get parent-link (keyword/add-namespace item-namespace :id)))))
