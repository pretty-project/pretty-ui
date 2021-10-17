
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v0.7.0
; Compatibility: x4.1.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-db.transfer-handler
    (:require [mid-fruits.candy            :refer [param return]]
              [mid-fruits.keyword          :as keyword]
              [mid-fruits.logical          :refer [nonfalse?]]
              [mid-fruits.loop             :refer [reduce-indexed]]
              [mid-fruits.map              :as map]
              [mid-fruits.vector           :as vector]
              [x.mid-core.api              :refer [r]]
              [x.mid-db.collection-handler :as collection-handler]
              [x.mid-db.data-item-handler  :as data-item-handler]
              [x.mid-db.document-handler   :as document-handler]
              [x.mid-db.partition-handler  :as partition-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.collection-handler
(def collection->collection-ordered? collection-handler/collection->collection-ordered?)
(def sort-collection                 collection-handler/sort-collection)

; x.mid-db.data-item-handler
(def data-item<-id data-item-handler/data-item<-id)

; x.mid-db.document-handler
(def item-key->namespaced-item-key     document-handler/item-key->namespaced-item-key)
(def item-key->non-namespaced-item-key document-handler/item-key->non-namespaced-item-key)
(def document->document-id             document-handler/document->document-id)
(def document->unidentified-document   document-handler/document->unidentified-document)
(def document->ordered-document        document-handler/document->ordered-document)
(def document->item-key                document-handler/document->item-key)

; x.mid-db.partition-handler
(def partition->data-items         partition-handler/partition->data-items)
(def partition->data-item          partition-handler/partition->data-item)
(def partition->data-order         partition-handler/partition->data-order)
(def partition->partition-ordered? partition-handler/partition->partition-ordered?)
(def get-partition                 partition-handler/get-partition)
(def partition-exists?             partition-handler/partition-exists?)
(def reg-partition!                partition-handler/reg-partition!)
(def add-data-items!               partition-handler/add-data-items!)



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn document-id->data-item-id
  ; @param (string) n
  ;
  ; @example
  ;  (db/document-id->data-item-id "my-id")
  ;  => :my-id
  ;
  ; @return (keyword)
  [n]
  (keyword n))

(defn data-item-value->document-item-value
  ; @param (*) n
  ;
  ; @example
  ;  (db/data-item-value->document-item-value "my-value")
  ;  => "my-value"
  ;
  ; @example
  ;  (db/data-item-value->document-item-value :my-value)
  ;  => "my-value"
  ;
  ; @return (*)
  [n]
  (cond (keyword? n)
        (keyword/to-string n)
        :else (return n)))

(defn document-item-value->data-item-value
  ; @param (*) n
  ;
  ; @example
  ;  (db/document-item-value->data-item-value "my-value")
  ;  => "my-value"
  ;
  ; @return (*)
  [n]
  (return n))

(defn data-item->document
  ; @param (keyword) data-item-id
  ; @param (map) data-item
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @example
  ;  (db/data-item->document :my-data-item {:foo/bar :baz})
  ;  => {:foo/bar "baz"
  ;      :foo/id "my-data-item"}
  ;
  ; @return (map)
  [data-item-id data-item {:keys [additional-namespace remove-namespace?]}]
  (reduce-kv (fn [document item-key item-value]
                 (cond (some? additional-namespace)
                       (let [item-key   (item-key->namespaced-item-key item-key additional-namespace)
                             item-value (data-item-value->document-item-value item-value)]
                            (assoc document item-key item-value))
                       (boolean remove-namespace?)
                       (let [item-key   (item-key->non-namespaced-item-key    item-key)
                             item-value (data-item-value->document-item-value item-value)]
                            (assoc document item-key item-value))
                       :else
                       (let [item-value (data-item-value->document-item-value item-value)]
                            (assoc document item-key item-value))))
             (param {})
             (data-item<-id data-item-id data-item)))

(defn document->data-item
  ; @param (map) document
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @example
  ;  (db/document->data-item {:foo/bar "baz" :foo/id "my-document"})
  ;  => {:foo/bar "baz" :foo/id "my-document"}
  ;
  ; @example
  ;  (db/document->data-item {:foo/bar "baz" :foo/id "my-document"} {:remove-namespace? true})
  ;  => {:bar "baz"}
  ;
  ; @return (map)
  [document {:keys [additional-namespace remove-namespace?]}]
  (reduce-kv (fn [data-item item-key item-value]
                 (cond (some? additional-namespace)
                       (let [item-key   (item-key->namespaced-item-key item-key additional-namespace)
                             item-value (document-item-value->data-item-value item-value)]
                            (assoc data-item item-key item-value))
                       (boolean remove-namespace?)
                       (let [item-key   (item-key->non-namespaced-item-key    item-key)
                             item-value (document-item-value->data-item-value item-value)]
                            (assoc data-item item-key item-value))
                       :else
                       (let [item-value (document-item-value->data-item-value item-value)]
                            (assoc document item-key item-value))))
             (param {})

            ; A kollekciók partícióvá alakítása során a dokumentumok ne veszítsék el
            ; az azonosítóikat! @paul
            ;
            ;(document->unidentified-document document)

             (param document)))

(defn unordered-partition->collection
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) partition
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @example
  ;  (db/unordered-partition->collection {:data-items {:a {} :b {} :c {}}})
  ;  => [{:id "a"} {:id "b"} {:id "c"}]
  ;
  ;
  ; @return (maps in vector)
  [partition context-props]
  (let [data-items (partition->data-items partition)]
       (reduce-kv (fn [collection data-item-id data-item]
                      (let [document (data-item->document data-item-id data-item context-props)]
                           (vector/conj-item collection document)))
                  (param [])
                  (param data-items))))

(defn ordered-partition->collection
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) partition
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @example
  ;  (db/ordered-partition->collection {:data-items {:a {} :b {} :c {}}
  ;                                     :data-order [:a :b :c]})
  ;  => [{:id "a" :order "0"} {:id "b" :order "1"} {:id "c" :order "2"}]
  ;
  ; @return (maps in vector)
  [partition context-props]
  (let [data-order (partition->data-order partition)]
       (reduce-indexed (fn [collection data-item-id data-order]
                           (let [data-item        (partition->data-item partition data-item-id)
                                 document         (data-item->document data-item-id data-item context-props)
                                 ordered-document (document->ordered-document document data-order)]
                                (vector/conj-item collection ordered-document)))
                       (param [])
                       (param data-order))))

(defn partition->collection
  ; @param (map) partition
  ;  {:data-items (map)}
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @return (maps in vector)
  [partition context-props]
  (if (partition->partition-ordered? partition)
      (ordered-partition->collection   partition context-props)
      (unordered-partition->collection partition context-props)))

(defn collection->data-items
  ; @param (maps in vector) collection
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :keywordize? (boolean)(opt)
  ;    Default: true
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @example
  ;  (db/collection->data-items [{:foo/bar "baz" :foo/id "az0323"}])
  ;  => {:az0323 {:foo/bar "baz" :foo/id "az0323"}}}
  ;
  ; @return (map)
  ([collection]
   (collection->data-items collection {}))

  ([collection {:keys [keywordize?] :as context-props}]
   (reduce (fn [data-items document]
               (let [document-id  (document->document-id     document)
                     data-item-id (document-id->data-item-id document-id)
                     data-item    (document->data-item       document context-props)]
                    (if (nonfalse? keywordize?)
                        (assoc data-items data-item-id data-item)
                        (assoc data-items document-id  data-item))))
           (param {})
           (param collection))))

(defn unordered-collection->data-order
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (maps in vector) collection
  ; @param (map) context-props
  ;  {:keywordize? (boolean)(opt)
  ;    Default: true}
  ;
  ; @example
  ;  (db/unordered-collection->data-order [{:foo/bar "baz" :foo/id "az0323"}])
  ;  => [:az0323]
  ;
  ; @return (vector)
  [collection {:keys [keywordize?]}]
  (reduce (fn [data-order document]
              (let [document-id  (document->document-id     document)
                    data-item-id (document-id->data-item-id document-id)]
                   (if (nonfalse? keywordize?)
                       (vector/conj-item data-order data-item-id)
                       (vector/conj-item data-order document-id))))
          (param [])
          (param collection)))

(defn ordered-collection->data-order
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (maps in vector) collection
  ; @param (map) context-props
  ;  {:keywordize? (boolean)(opt)
  ;    Default: true}
  ;
  ; @example
  ;  (db/ordered-collection->data-order [{:foo/bar "baz #2" :foo/id "az0323" :foo/order 2}
  ;                                      {:foo/bar "baz #1" :foo/id "xs0301" :foo/order 1}])
  ;  => [:xs0301 :az0323]
  ;
  ; @return (vector)
  [collection {:keys [keywordize?]}]
  (reduce (fn [data-order document]
              (let [document-id  (document->document-id     document)
                    data-item-id (document-id->data-item-id document-id)]
                   (if (nonfalse? keywordize?)
                       (vector/conj-item data-order data-item-id)
                       (vector/conj-item data-order document-id))))
          (param [])
          (sort-collection collection :order)))

(defn collection->data-order
  ; @param (maps in vector) collection
  ; @param (map)(opt) context-props
  ;  {:keep-document-order? (boolean)
  ;   :keywordize? (boolean)(opt)
  ;    Default: true}
  ;
  ; @example
  ;  (db/collection->data-order [{:foo/bar "baz" :foo/id "az0323"}])
  ;  => [:az0323]
  ;
  ; @return (vector)
  ([collection]
   (collection->data-order collection {}))

  ([collection {:keys [keep-document-order?] :as context-props}]
   (if (and (boolean keep-document-order?)
            (collection->collection-ordered? collection))
       (ordered-collection->data-order   collection context-props)
       (unordered-collection->data-order collection context-props))))


(defn collection->partition
  ; @param (maps in vector) collection
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :keywordize? (boolean)(opt)
  ;    Default: true
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @example
  ;  (db/collection->partition [{:foo/bar "baz" :foo/id "az0323"}])
  ;  => {:data-items {:az0323 {:foo/bar "baz" :foo/id "az0323"}}
  ;      :data-order [:az0323]}
  ;
  ; @return (map)
  ;  {:data-items (map)
  ;   :data-order (vector)}
  ([collection]
   (collection->partition collection {}))

  ([collection context-props]
   {:data-items (collection->data-items collection context-props)
    :data-order (collection->data-order collection context-props)}))

(defn collection->map
  ; @param (maps in vector) collection
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :keywordize? (boolean)(opt)
  ;    Default: true
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @example
  ;  (db/collection->map [{:foo/bar "baz" :foo/id "az0323"}])
  ;  => {:az0323 {:foo/bar "baz" :foo/id "az0323"}}
  ;
  ; @return (map)
  ([collection]
   (collection->map collection {}))

  ([collection context-props]
   (collection->data-items collection context-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-partition
  ; Export partition to collection
  ;
  ; @param (namespaced keyword) partition-id
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @return (maps in vector)
  [db [_ partition-id context-props]]
  (let [partition (r get-partition db partition-id)]
       (partition->collection partition context-props)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-collection!
  ; Import collection to partition
  ;
  ; @param (namespaced keyword) partition-id
  ; @param (maps in vector) collection
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :keep-document-order? (boolean)(opt)
  ;    Ha a kollekció rendezett, akkor a kollekció rendezésének sorrendje
  ;    legyen a partíció rendezésének sorrendje is a dokumentumok {:order ...}
  ;    tulajdonsága alapján.
  ;    Default: false
  ;   :keywordize? (boolean)(opt)
  ;    Default: true
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @usage
  ;  (r db/import-collection! db ::my-partition [{...} {...}])
  ;
  ; @return (map)
  [db [_ partition-id collection context-props]]
  (if (r partition-exists? db partition-id)
      ; Import collection to existsing partition ...
      (let [data-items (collection->data-items collection context-props)
            data-order (collection->data-order collection context-props)]
           (r add-data-items! db partition-id data-items data-order))
      ; Import collection to new partition ...
      (let [partition (collection->partition collection context-props)]
           (r reg-partition! db partition-id partition))))
