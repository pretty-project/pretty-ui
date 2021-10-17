
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v0.6.8
; Compatibility: x4.1.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-db.data-item-handler
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :as map]))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn data-item->namespace
  ; @param (map) data-item
  ;
  ; @example
  ;  (db/data-item->namespace {:bar "baz"})
  ;  => nil
  ;
  ; @example
  ;  (db/data-item->namespace {:foo/bar "baz"})
  ;  => :foo
  ;
  ; @return (keyword or nil)
  [data-item]
  (let [keys (map/get-keys data-item)]
       (keyword/get-namespace (first keys))))

(defn data-item->namespaced?
  ; @param (map) data-item
  ;
  ; @example
  ;  (db/data-item->namespaced? {:foo "bar"})
  ;  => false
  ;
  ; @example
  ;  (db/data-item->namespaced? {:foo/bar "baz"})
  ;  => true
  ;
  ; @return (boolean)
  [data-item]
  (let [keys (map/get-keys data-item)]
       (keyword/namespaced? (first keys))))

(defn data-item<-id
  ; @param (keyword) data-item-id
  ; @param (map) data-item
  ;
  ; @example
  ;  (db/data-item<-id :my-data-item {:bar "baz"})
  ;  => {:bar "baz" :id :my-data-item}
  ;
  ; @example
  ;  (db/data-item<-id :my-data-item {:foo/bar "baz"})
  ;  => {:foo/bar "baz" :foo/id :my-data-item}
  ;
  ; @return (map)
  [data-item-id data-item]
  (if (data-item->namespaced? data-item)
      (let [namespace (data-item->namespace data-item)]
           (assoc data-item (keyword/add-namespace namespace :id)
                            (param data-item-id)))
      (assoc data-item :id data-item-id)))
