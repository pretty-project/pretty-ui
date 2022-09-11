
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.21
; Description:
; Version: v0.2.8
; Compatibility: x4.3.4



;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-db.id-handler
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :as map]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name document-entity
;  Olyan adat, amely egyértelműen azonosít egy dokumentumot.
;  Pl. [:account/id "0ce14671-e916-43ab-b057-0939329d4c1b"]
;       [:passenger/passport-id "KI-1993-6503688-FF"]
;
; @name document-link
;  Egy dokumentumban egy vagy több másik dokumentumot úgy érdemes tárolni (hozzácsatolni),
;  hogy a csatolt dokumentumoknak csak olyan kivonatát táruljuk ami nem tartalmaz
;  elévülhető adatot.
;  Ilyen nem elévülhető adat a csatolt dokumentumok azonosítója, aminek segítségével
;  egy csatolt dokumentumokat egyértelműen azonosíthatjuk és elérhetjük az
;  eredeti tárolási helyükön.
;
; Pl. [{:account/id "0ce14671-e916-43ab-b057-0939329d4c1b"
;        :games [{:game/id "9cea3696-56ca-4be5-a5f2-e7477d9f43fb"}
;                {...}]}]



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn document-entity?
  ; @param (vector) n
  ;
  ; @example
  ;  (db/document-entity? [:directory/id "my-directory"])
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (and (vector? n)
       (= 2 (count n))
       (-> n first  keyword?)
       (-> n second string?)))

(defn document-link?
  ; @param (vector) n
  ;
  ; @example
  ;  (db/document-link? {:directory/id "my-directory"})
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (and (map? n)
       (= 1 (count n))
       (-> n keys first keyword?)
       (-> n vals first string?)))

(defn document-id->document-link
  ; @param (string) document-id
  ; @param (keyword)(opt) namespace
  ;
  ; @example
  ;  (db/document-id->document-link "my-directory")
  ;  =>
  ;  {:id "my-directory"}
  ;
  ; @example
  ;  (db/document-id->document-link "my-directory" :directory)
  ;  =>
  ;  {:directory/id "my-directory"}
  ;
  ; @return (map)
  [document-id & [namespace]]
  (if namespace {(keyword/add-namespace namespace :id) document-id}
                {(param                           :id) document-id}))

(defn item-id->document-link
  ; @param (keyword) item-id
  ; @param (keyword)(opt) namespace
  ;
  ; @example
  ;  (db/item-id->document-link :my-directory)
  ;  =>
  ;  {:id "my-directory"}
  ;
  ; @example
  ;  (db/item-id->document-link :my-directory :directory)
  ;  =>
  ;  {:directory/id "my-directory"}
  ;
  ; @return (map)
  [item-id & [namespace]]
  (let [document-id (name item-id)]
       (document-id->document-link document-id namespace)))

(defn document-link->document-id
  ; @param (map) document-link
  ;
  ; @example
  ;  (db/document-link->document-id {:id "my-directory"})
  ;  =>
  ;  "my-directory"
  ;
  ; @return (string)
  [document-link]
  (map/get-first-value document-link))

(defn document-link->item-id
  ; @param (map) document-link
  ;
  ; @example
  ;  (db/document-link->item-id {:id "my-directory"})
  ;  =>
  ;  :my-directory
  ;
  ; @return (keyword)
  [document-link]
  (let [document-id (document-link->document-id document-link)]
       (keyword document-id)))

(defn document-link->namespace
  ; @param (map) document-link
  ;
  ; @example
  ;  (db/document-link->namespace {:id "my-directory"})
  ;  =>
  ;  nil
  ;
  ; @example
  ;  (db/document-link->namespace {:directory/id "my-directory"})
  ;  =>
  ;  :directory
  ;
  ; @return (keyword)
  [document-link]
  (let [key (map/get-first-key document-link)]
       (keyword/get-namespace key)))

(defn document-link->namespace?
  ; @param (map) document-link
  ; @param (keyword) namespace
  ;
  ; @example
  ;  (db/document-link->namespace {:file/id "my-file"} :directory)
  ;  =>
  ;  false
  ;
  ; @example
  ;  (db/document-link->namespace {:directory/id "my-directory"} :directory)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [document-link namespace]
  (let [document-link-namespace (document-link->namespace document-link)]
       (= document-link-namespace namespace)))

(defn document-id->document-entity
  ; @param (string) document-id
  ; @param (keyword)(opt) namespace
  ;
  ; @example
  ;  (db/document-id->document-entity "my-directory")
  ;  =>
  ;  [:id "my-directory"]
  ;
  ; @example
  ;  (db/document-id->document-entity "my-directory" :directory)
  ;  =>
  ;  [:directory/id "my-directory"]
  ;
  ; @return (vector)
  [document-id & [namespace]]
  (if namespace [(keyword/add-namespace namespace :id) document-id]
                [(param                           :id) document-id]))

(defn item-id->document-entity
  ; @param (keyword) item-id
  ; @param (keyword)(opt) namespace
  ;
  ; @example
  ;  (db/item-id->document-entity :my-directory)
  ;  =>
  ;  [:id "my-directory"]
  ;
  ; @example
  ;  (db/item-id->document-entity :my-directory :directory)
  ;  =>
  ;  [:directory/id "my-directory"]
  ;
  ; @return (vector)
  [item-id & [namespace]]
  (let [document-id (name item-id)]
       (document-id->document-entity document-id namespace)))

(defn document-entity->document-id
  ; @param (vector) document-entity
  ;
  ; @example
  ;  (db/document-entity->document-id [:directory/id "my-directory"])
  ;  =>
  ;  "my-directory"
  ;
  ; @return (string)
  [document-entity]
  (second document-entity))

(defn document-entity->item-id
  ; @param (vector) document-entity
  ;
  ; @example
  ;  (db/document-entity->item-id [:directory/id "my-directory"])
  ;  =>
  ;  :my-directory
  ;
  ; @return (keyword)
  [document-entity]
  (let [document-id (document-entity->document-id document-entity)]
       (keyword document-id)))
