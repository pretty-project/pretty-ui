
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v0.5.2
; Compatibility: x4.1.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-db.document-handler
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.map     :as map]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.random  :as random]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-key->non-namespaced-item-key
  ; @param (keyword) item-key
  ;
  ; @example
  ;  (db/item-key->non-namespaced-item-key :bar)
  ;  =>
  ;  :bar
  ;
  ; @example
  ;  (db/item-key->non-namespaced-item-key :foo/bar)
  ;  =>
  ;  :bar
  ;
  ; @return (keyword)
  [item-key]
  (keyword/get-name item-key))

(defn item-key->namespaced-item-key
  ; @param (keyword) item-key
  ; @param (keyword) namespace
  ;
  ; @example
  ;  (db/item-key->namespaced-item-key :foo :bar)
  ;  =>
  ;  :bar/foo
  ;
  ; @example
  ;  (db/item-key->namespaced-item-key :bar/foo :bar)
  ;  =>
  ;  :bar/foo
  ;
  ; @return (keyword)
  [item-key namespace]
  (keyword/add-namespace namespace item-key))

(defn document->namespace
  ; @param (map) document
  ;
  ; @example
  ;  (db/document->namespace {:bar "baz"})
  ;  =>
  ;  nil
  ;
  ; @example
  ;  (db/document->namespace {:foo/bar "baz"})
  ;  =>
  ;  :foo
  ;
  ; @example
  ;  (db/document->namespace {:foo     "bar"
  ;                           :baz     "boo"
  ;                           :bam/box "bok"
  ;                           :kop/lok "map"})
  ;  =>
  ;  :bam
  ;
  ; @return (keyword or nil)
  [document]
  ; A MongoDB adatbázisban tárolt dokumentumoknál előfordulhat,
  ; hogy valamelyik elem az :_id kulcs, ami nem rendelkezik névtérrel!
  ; {:_id "..." :directory/id "..." :directory/alias "..."}
  ; Szükséges több kulcson is vizsgálni a névteret.
  (some #(keyword/get-namespace %)
         (map/get-keys document)))

(defn document->namespace?
  ; @param (map) document
  ; @param (keyword) namespace
  ;
  ; @example
  ;  (db/document->namespace? {:bar "baz"} :foo)
  ;  =>
  ;  false
  ;
  ; @example
  ;  (db/document->namespace {:foo/bar "baz"} :foo)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [document namespace]
  (= (param namespace)
     (document->namespace document)))

(defn document->document-namespaced?
  ; @param (map) document
  ;
  ; @example
  ;  (db/document->document-namespaced? {:foo "bar"})
  ;  =>
  ;  false
  ;
  ; @example
  ;  (db/document->document-namespaced? {:foo/bar "baz"})
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [document]
  (let [document-namespace (document->namespace document)]
       (some? document-namespace)))

(defn document->document-non-namespaced?
  ; @param (map) document
  ;
  ; @example
  ;  (db/document->document-non-namespaced? {:foo "bar"})
  ;  =>
  ;  true
  ;
  ; @example
  ;  (db/document->document-non-namespaced? {:foo/bar "baz"})
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [document]
  (let [document-namespaced? (document->document-namespaced? document)]
       (not document-namespaced?)))

(defn document->namespaced-document
  ; @param (map) document
  ; @param (keyword) namespace
  ;
  ; @example
  ;  (db/document->namespaced-document {:foo "bar"} :baz)
  ;  =>
  ;  {:baz/foo "bar"}
  ;
  ; @return (map)
  [document namespace]
  (letfn [(f [document item-key item-value]
             (assoc document (item-key->namespaced-item-key item-key namespace)
                             (param item-value)))]
         (reduce-kv f {} document)))

(defn document->non-namespaced-document
  ; @param (map) document
  ;
  ; @example
  ;  (db/document->non-namespaced-document {:baz/foo "bar"})
  ;  =>
  ;  {:foo "bar"}
  ;
  ; @return (map)
  [document]
  (letfn [(f [document item-key item-value]
             (assoc document (item-key->non-namespaced-item-key item-key)
                             (param item-value)))]
         (reduce-kv f {} document)))

(defn document-contains-key?
  ; @param (map) document
  ;
  ; @example
  ;  (db/document-contains-key? {:bar "baz"} :bar)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (db/document-contains-key? {:foo/bar "baz"} :bar)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (db/document-contains-key {:bar "baz"} :foo)
  ;  =>
  ;  false
  ;
  ; @return (map)
  [document key]
  (if-let [namespace (document->namespace document)]
          (map/contains-key? document (keyword/add-namespace namespace :key))
          (map/contains-key? document (param :key))))

(defn assoc-document-value
  ; @param (map) document
  ; @param (keyword) k
  ; @param (*) v
  ;
  ; @example
  ;  (db/assoc-document-value {:bar "baz"} :boo "bam")
  ;  =>
  ;  {:bar "baz" :boo "bam"}
  ;
  ; @example
  ;  (db/assoc-document-value {:foo/bar "baz"} :boo "bam")
  ;  =>
  ;  {:foo/bar "baz" :foo/boo "bam"}
  ;
  ; @return (map)
  [document k v]
  (if-let [namespace (document->namespace document)]
          (assoc document (keyword/add-namespace namespace k)
                          (param v))
          (assoc document k v)))

(defn dissoc-document-value
  ; @param (map) document
  ; @param (keyword) k
  ;
  ; @example
  ;  (db/assoc-document-value {:bar "baz"} :bar)
  ;  =>
  ;  {}
  ;
  ; @example
  ;  (db/assoc-document-value {:foo/bar "baz"} :bar)
  ;  =>
  ;  {}
  ;
  ; @return (map)
  [document k]
  (if-let [namespace (document->namespace document)]
          (dissoc document (keyword/add-namespace namespace k))
          (dissoc document (param k))))

(defn get-document-value
  ; @param (map) document
  ; @param (keyword) k
  ;
  ; @example
  ;  (db/get-document-value {:bar "baz"} :bar)
  ;  =>
  ;  "baz"
  ;
  ; @example
  ;  (db/get-document-value {:foo/bar "baz"} :bar)
  ;  =>
  ;  "baz"
  ;
  ; @return (map)
  [document k]
  (if-let [namespace (document->namespace document)]
          (get document (keyword/add-namespace namespace k))
          (get document (param k))))

(defn document->document-id
  ; @param (map) document
  ;  {:namespace/id (string)(opt)
  ;   :id (string)(opt)}
  ;
  ; @example
  ;  (db/document->document-id {:bar "baz"})
  ;  =>
  ;  "i5o28977-d2310-k5432-lk98u784k819"
  ;
  ; @example
  ;  (db/document->document-id {:bar "baz" :id "my-document"})
  ;  =>
  ;  "my-document"
  ;
  ; @example
  ;  (db/document->document-id {:foo/bar "baz" :foo/id "my-document"})
  ;  =>
  ;  "my-document"
  ;
  ; @return (string)
  [document]
  (if-let [document-id (get-document-value document :id)]
          (return document-id)
          (random/generate-string)))

(defn document->unidentified-document
  ; @param (map) document
  ;
  ; @example
  ;  (db/document->unidentified-document {:foo/bar "baz" :foo/id "my-document"})
  ;  =>
  ;  {:foo/bar "baz"}
  ;
  ; @return (map)
  [document]
  (if (document->document-namespaced? document)
      (let [namespace (document->namespace document)]
           (dissoc document (keyword/add-namespace namespace :id)))
      (dissoc document :id)))

(defn document->pure-document
  ; @param (map) document
  ;
  ; @example
  ;  (db/document->unidentified-document {:foo/bar "baz" :foo/id "my-document"})
  ;  =>
  ;  {:bar "baz"}
  ;
  ; @return (map)
  [document]
  (-> document document->unidentified-document document->non-namespaced-document))

(defn document->identified-document
  ; @param (map) document
  ;
  ; @example
  ;  (db/document->identified-document {:bar "baz" :id "my-document"})
  ;  =>
  ;  {:bar "baz" :id "my-document"}
  ;
  ; @example
  ;  (db/document->identified-document {:foo/bar "baz" :foo/id "my-document"})
  ;  =>
  ;  {:foo/bar "baz" :foo/id "my-document"}
  ;
  ; @example
  ;  (db/document->identified-document {:bar "baz"})
  ;  =>
  ;  {:bar "baz" :id "0ce14671-e916-43ab-b057-0939329d4c1b"}
  ;
  ; @example
  ;  (db/document->identified-document {:foo/bar "baz"})
  ;  =>
  ;  {:foo/bar "baz" :foo/id "0ce14671-e916-43ab-b057-0939329d4c1b"}
  ;
  ; @return (map)
  [document]
  (if (document->document-namespaced? document)
      (let [namespace (document->namespace document)]
           (if (some? (get document (keyword/add-namespace namespace :id)))
               (return document)
               (assoc document (keyword/add-namespace namespace :id)
                               (random/generate-string))))
      (if (some? (get document :id))
          (return document)
          (assoc document :id (random/generate-string)))))

(defn document->item-value
  ; @param (map) document
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (db/document->item-value {:foo/bar "baz"} :bar)
  ;  =>
  ;  "baz"
  ;
  ; @usage
  ;  (db/document->item-value {:foo/bar "baz"} :foo/bar)
  ;  =>
  ;  "baz"
  ;
  ; @return (*)
  [document item-key]
  (if-let [namespace (document->namespace document)]
          (get document (item-key->namespaced-item-key     item-key namespace))
          (get document (item-key->non-namespaced-item-key item-key))))

(defn document->item-key
  ; @param (map) document
  ; @param (keyword) item-key
  ;
  ; @example
  ;  (db/document->item-key {:id 1} :id)
  ;  =>
  ;  :id
  ;
  ; @example
  ;  (db/document->item-key {:id 1} :foo/id)
  ;  =>
  ;  :id
  ;
  ; @example
  ;  (db/document->item-key {:foo/id 1} :id)
  ;  =>
  ;  :foo/id
  ;
  ; @return (keyword)
  ;  A dokumentum névterével látja el a megadott item-key paraméter értékét
  [document item-key]
  (if (document->document-namespaced? document)
      (let [namespace (document->namespace document)]
           (keyword/add-namespace namespace item-key))
      (keyword/get-name item-key)))
