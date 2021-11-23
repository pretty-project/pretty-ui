
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



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name document-path
;  [(string) collection-name
;   (string) document-id
;   (keyword)(opt) item-id
;   (keyword)(opt) subitem-id
;   ...]



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn document-path->collection-name
  ; @param (document-path vector)
  ;  [(string) collection-name
  ;   (string) document-id
  ;   (keyword)(opt) item-id]
  ;
  ; @example
  ;  (db/document-path->collection-name ["my-collection" "my-document" :my-item])
  ;  => 
  ;  "my-collection"
  ;
  ; @return (string)
  [document-path]
  (first document-path))

(defn document-path->document-id
  ; @param (document-path vector)
  ;  [(string) collection-name
  ;   (string) document-id
  ;   (keyword)(opt) item-id]
  ;
  ; @example
  ;  (db/document-path->document-id ["my-collection" "my-document" :my-item])
  ;  =>
  ;  "my-document"
  ;
  ; @return (string)
  [document-path]
  (first document-path))

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
  (reduce-kv (fn [document item-key item-value]
                 (assoc document (item-key->namespaced-item-key item-key namespace)
                                 (param item-value)))
             (param {})
             (param document)))

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
  (reduce-kv (fn [document item-key item-value]
                 (assoc document (item-key->non-namespaced-item-key item-key)
                                 (param item-value)))
             (param {})
             (param document)))

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
  ;  (db/document->unidentified-document {:foo/bar "baz" :foo/id "my-document" :foo/permissions {...}})
  ;  =>
  ;  {:bar "baz"}
  ;
  ; @return (map)
  [document]
  (-> document (document->unidentified-document)
               (document->non-namespaced-document)

               ; A dokumentum változtatásához szükséges jogosultságokot leíró térképet
               ; szükségeses eltávolítani a dokumentumból, annak szerverről kliens eszközre
               ; történő elküldése előtt.
               (dissoc :permissions)))

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

(defn document->identified-document?
  ; @param (map) document
  ;
  ; @example
  ;  (db/document->identified-document? {:bar "baz" :id "my-document"})
  ;  =>
  ;  true
  ;
  ; @example
  ;  (db/document->identified-document? {:foo/bar "baz" :foo/id "my-document"})
  ;  =>
  ;  true
  ;
  ; @example
  ;  (db/document->identified-document {:bar "baz"})
  ;  =>
  ;  false
  ;
  ; @return (map)
  [document]
  (document-contains-key? document :id))

(defn document->non-identified-document?
  ; @param (map) document
  ;
  ; @example
  ;  (db/document->non-identified-document? {:bar "baz" :id "my-document"})
  ;  =>
  ;  false
  ;
  ; @example
  ;  (db/document->non-identified-document? {:foo/bar "baz" :foo/id "my-document"})
  ;  =>
  ;  false
  ;
  ; @example
  ;  (db/document->non-identified-document {:bar "baz"})
  ;  =>
  ;  true
  ;
  ; @return (map)
  [document]
  (let [document-identified? (document->identified-document? document)]
       (not document-identified?)))

(defn document->ordered-document
  ; @param (map) document
  ; @param (integer) document-dex
  ;
  ; @example
  ;  (db/document->ordered-document {:id "1"} 7)
  ;  =>
  ;  {:id "1" :order "7"}
  ;
  ; @return (map)
  ;  {:order (string)}
  [document document-dex]
  (assoc-document-value document :order (str document-dex)))

(defn document->ordered-document?
  ; @param (map) document
  ;
  ; @example
  ;  (db/document->ordered-document? {:bar "baz" :order "my-document"})
  ;  =>
  ;  true
  ;
  ; @example
  ;  (db/document->ordered-document? {:foo/bar "baz" :foo/order "my-document"})
  ;  =>
  ;  true
  ;
  ; @example
  ;  (db/document->ordered-document {:bar "baz"})
  ;  =>
  ;  false
  ;
  ; @return (map)
  [document]
  (document-contains-key? document :order))

(defn document->document-dex
  ; @param (map) document
  ;
  ; @return (integer)
  [document]
  (get-document-value document :order))

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
