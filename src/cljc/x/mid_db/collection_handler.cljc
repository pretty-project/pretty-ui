
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v1.2.8
; Compatibility: x4.1.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-db.collection-handler
    (:require [mid-fruits.candy          :refer [param return]]
              [mid-fruits.keyword        :as keyword]
              [mid-fruits.loop           :refer [reduce-while]]
              [mid-fruits.map            :as map]
              [mid-fruits.vector         :as vector]
              [x.mid-db.document-handler :as document-handler]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name collection
;  [{:id "my-data" :my-prop "my-value"} {...} {...}]
;
; @name ordered-collection
;  [{:id "my-data" :my-prop "my-value" :order 7} {...} {...}]
;
; @name {:additional-namespace ...}
;  XXX#3209
;  Az {:additional-namespace ...} tulajdonság átadásával a függvény visszatérési
;  értékében levő kollekció dokumentumainak kulcsai vagy a függvény visszatérési
;  értékében levő dokumentum kulcsai, az átadott névtérrel hozzácsatolásával
;  lesznek elnevezve.
;
; @name {:remove-namespace? true}
;  XXX#3210
;  A {:remove-namespace? true} tulajdonság átadásával a függvény visszatérési
;  értékében levő kollekció dokumentumainak kulcsai vagy a függvény visszatérési
;  értékében levő dokumentum kulcsai névtér használata nélkül lesznek elnevezve.



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.document-handler
(def document->namespace                document-handler/document->namespace)
(def document->document-namespaced?     document-handler/document->document-namespaced?)
(def document->document-non-namespaced? document-handler/document->document-non-namespaced?)
(def document->namespaced-document      document-handler/document->namespaced-document)
(def document->non-namespaced-document  document-handler/document->non-namespaced-document)
(def document->document-id              document-handler/document->document-id)
(def document->identified-document      document-handler/document->identified-document)
(def document->item-key                 document-handler/document->item-key)



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remote-path->collection-id
  ; @param (document-path vector) remote-path
  ;  [(string) collection-id
  ;   (string) document-id
  ;   (keyword) item-key]
  ;
  ; @example
  ;  (db/remote-path/collection-id ["my-collection" "my-document" :my-item])
  ;  => "my-collection"
  ;
  ; @return (string)
  [[collection-id _ _]]
  (return collection-id))

(defn remote-path->document-id
  ; @param (document-path vector) remote-path
  ;  [(string) collection-id
  ;   (string) document-id
  ;   (keyword) item-key]
  ;
  ; @example
  ;  (db/remote-path/document-id ["my-collection" "my-document" :my-item])
  ;  => "my-document"
  ;
  ; @return (string)
  [[_ document-id _]]
  (return document-id))

(defn remote-path->item-key
  ; @param (document-path vector) remote-path
  ;  [(string) collection-id
  ;   (string) document-id
  ;   (keyword) item-key]
  ;
  ; @example
  ;  (db/remote-path/item-key ["my-collection" "my-document" :my-item])
  ;  => :my-item
  ;
  ; @return (string)
  [[_ _ item-key]]
  (return item-key))

(defn collection->namespace
  ; @param (maps in vector) collection
  ;
  ; @usage
  ;  (db/collection->namespaced [{...} {...} {...}])
  ;
  ; @return (keyword)
  [collection]
  (if-let [first-document (first collection)]
          (document->namespace first-document)))

(defn collection->collection-namespaced?
  ; @param (maps in vector) collection
  ;
  ; @usage
  ;  (db/collection->collection-namespaced? [{...} {...} {...}])
  ;
  ; @return (boolean)
  [collection]
  (boolean (collection->namespace collection)))

(defn collection->collection-non-namespaced?
  ; @param (maps in vector) collection
  ;
  ; @usage
  ;  (db/collection->collection-non-namespaced? [{...} {...} {...}])
  ;
  ; @return (boolean)
  [collection]
  (not (collection->collection-namespaced? collection)))

(defn collection->namespaced-collection
  ; @param (maps in vector) collection
  ; @param (keyword) namespace
  ;
  ; @example
  ;  (db/collection->namespaced-collection [{:foo "bar"} {:foo "boo"}] :baz)
  ;  => [{:baz/foo "bar"} {:baz/foo "boo"}]
  ;
  ; @return (maps in  vector)
  [collection namespace]
  (reduce (fn [collection document]
              (let [namespaced-document (document->namespaced-document document namespace)]
                   (vector/conj-item collection namespaced-document)))
          (param [])
          (param collection)))

(defn collection->non-namespaced-collection
  ; @param (maps in vector) collection
  ;
  ; @example
  ;  (db/collection->namespaced-collection [{:baz/foo "bar"} {:baz/foo "boo"}])
  ;  => [{:foo "bar"} {:foo "boo"}]
  ;
  ; @return (maps in  vector)
  [collection]
  (reduce (fn [collection document]
              (let [non-namespaced-document (document->non-namespaced-document document)]
                   (vector/conj-item collection non-namespaced-document)))
          (param [])
          (param collection)))

(defn collection->collection-ordered?
  ; @param (maps in vector) collection
  ;
  ; @example
  ;  (db/collection->collection-ordered? [{:id "1"} {:id "2"}])
  ;  => false
  ;
  ; @example
  ;  (db/collection->collection-ordered? [{:id "1" :order 1} {:id "2" :order 0}])
  ;  => true
  ;
  ; @example
  ;  (db/collection->collection-ordered? [{:foo/id "1" :foo/order 1} {:foo/id "2" :foo/order 0}])
  ;  => true
  ;
  ; @return (boolean)
  [collection]
  (boolean (if-let [first-document (first collection)]
                   (let [order-key (document->item-key first-document :order)]
                        (map/contains-key? first-document order-key)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn trim-collection
  ; @param (maps in vector) collection
  ; @param (integer) max-count
  ; @param (integer)(opt) skip
  ;  Default: 0
  ;
  ; @example
  ;  (db/trim-collection [{:id "1"} {:id "2"} {:id "3"} {:id "4"} {:id "5"}] 2)
  ;  => [{:id "1"} {:id "2"}]
  ;
  ; @example
  ;  (db/trim-collection [{:id "1"} {:id "2"} {:id "3"} {:id "4"} {:id "5"}] 2 1)
  ;  => [{:id "2"} {:id "3"} {:id "4"}]
  ;
  ; @example
  ;  (db/trim-collection [{:id "1"} {:id "2"} {:id "3"} {:id "4"} {:id "5"}] 2 4)
  ;  => [{:id "5"}]
  ;
  ; @return (maps in vector)
  ([collection max-count]
   (trim-collection collection max-count 0))

  ([collection max-count skip]
   (take max-count (drop skip collection))))

(defn sort-collection
  ; @param (maps in vector) collection
  ; @param (keyword) sort-key
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @example
  ;  (db/sort-collection [{:name "avocado"} {:name "apple"} {:name "banana"}] :name)
  ;  => [{:name "apple"} {:name "avocado"} {:name "banana"}]
  ;
  ; @example
  ;  (db/sort-collection [{:foo/name "avocado"} {:foo/name "apple"} {:foo/name "banana"}] :name)
  ;  => [{:foo/name "apple"} {:foo/name "avocado"} {:foo/name "banana"}]
  ;
  ; @return (maps in vector)
  ([collection sort-key]
   (sort-collection collection sort-key {}))

  ([collection sort-key {:keys [additional-namespace remove-namespace?]}]
   (if-let [first-document (first collection)]
           (let [sort-key (document->item-key first-document sort-key)
                 sorted-collection (vector/order-items-by collection sort-key)]
                (cond (some? additional-namespace)
                      (collection->namespaced-collection sorted-collection additional-namespace)
                      (boolean remove-namespace?)
                      (collection->non-namespaced-collection sorted-collection)
                      :else (return sorted-collection)))
           (param []))))

(defn filter-documents
  ; @param (maps in vector) collection
  ; @param (function) filter-f
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @usage
  ;  (db/filter-documents [{...} {...} {...}] #(= :value (:key %1)))
  ;
  ; @return (maps in vector)
  ([collection filter-f]
   (filter-documents collection filter-f {}))

  ([collection filter-f {:keys [additional-namespace remove-namespace?]}]
   (reduce #(cond (and (filter-f %2)
                       (some?    additional-namespace))
                  (let [document (document->namespaced-document %2 additional-namespace)]
                       (vector/conj-item %1 document))
                  (and (filter-f %2)
                       (boolean remove-namespace?))
                  (let [document (document->non-namespaced-document %2)]
                       (vector/conj-item %1 document))
                  (filter-f %2)
                  (vector/conj-item %1 %2)
                  :else (return %1))
           (param [])
           (param collection))))

(defn filter-document
  ; @param (maps in vector) collection
  ; @param (function) filter-f
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @usage
  ;  (db/filter-document [{...} {...} {...}] #(= :value (:key %1)))
  ;
  ; @return (map)
  ([collection filter-f]
   (filter-document collection filter-f {}))

  ([collection filter-f {:keys [additional-namespace remove-namespace?]}]
   (reduce-while #(cond (and (filter-f %2)
                             (some?   additional-namespace))
                        (document->namespaced-document %2 additional-namespace)
                        (and (filter-f %2)
                             (boolean remove-namespace?))
                        (document->non-namespaced-document %2)
                        (filter-f %2)
                        (return %2))
                  (param nil)
                  (param collection)
                  (fn [%1 _] (map? %1)))))

(defn match-documents
  ; Get documents by pattern
  ;
  ; @param (maps in vector) collection
  ; @param (map) pattern
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @example
  ;  (db/match-documents [{:foo "bar"} {...} {:foo "bar"}] {:foo "bar"})
  ;  => [{:foo "bar"} {:foo "bar"}]
  ;
  ; @return (maps in vector)
  ([collection pattern]
   (match-documents collection pattern {}))

  ([collection pattern {:keys [additional-namespace remove-namespace?]}]
   (reduce #(cond (and (map/match-pattern? %2 pattern)
                       (some? additional-namespace))
                  (let [document (document->namespaced-document %2 additional-namespace)]
                       (vector/conj-item %1 document))
                  (and (map/match-pattern? %2 pattern)
                       (boolean remove-namespace?))
                  (let [document (document->non-namespaced-document %2)]
                       (vector/conj-item %1 document))
                  (map/match-pattern? %2 pattern)
                  (vector/conj-item %1 %2)
                  :else (return %1))
           (param [])
           (param collection))))

(defn match-document
  ; Get first document by pattern
  ;
  ; @param (maps in vector) collection
  ; @param (map) pattern
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @example
  ;  (db/match-document [{:foo "bar"} {...} {:foo "bar"}] {:foo "bar"})
  ;  => {:foo "bar"}
  ;
  ; @return (map)
  ([collection pattern]
   (match-document collection pattern {}))

  ([collection pattern {:keys [additional-namespace remove-namespace?]}]
   (reduce-while #(cond (and (map/match-pattern? %2 pattern)
                             (some? additional-namespace))
                        (document->namespaced-document %2 additional-namespace)
                        (and (map/match-pattern? %2 pattern)
                             (boolean remove-namespace?))
                        (document->non-namespaced-document %2)
                        (map/match-pattern? %2 pattern)
                        (return %2))
                  (param nil)
                  (param collection)
                  (fn [%1 _] (map? %1)))))

(defn get-documents-kv
  ; Get documents by value
  ;
  ; @param (maps in vector) collection
  ; @param (keyword) item-key
  ; @param (*) item-value
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @example
  ;  (db/get-documents-kv [{:foo "bar"} {...} {:foo "bar"}] :foo "bar")
  ;  => [{:foo "bar"} {:foo "bar"}]
  ;
  ; @return (maps in vector)
  ([collection item-key item-value]
   (get-documents-kv collection item-key item-value {}))

  ([collection item-key item-value {:keys [additional-namespace remove-namespace?]}]
   (reduce #(cond (and (= (param item-value)
                          (get %2 item-key))
                       (some? additional-namespace))
                  (let [document (document->namespaced-document %2 additional-namespace)]
                       (vector/conj-item %1 document))
                  (and (= (param item-value)
                          (get %2 item-key))
                       (boolean remove-namespace?))
                  (let [document (document->non-namespaced-document %2)]
                       (vector/conj-item %1 document))
                  (= (param item-value)
                     (get %2 item-key))
                  (vector/conj-item %1 %2)
                  :else (return %1))
            (param nil)
            (param collection))))

(defn get-document-kv
  ; Get first document by value
  ;
  ; @param (maps in vector) collection
  ; @param (keyword) item-key
  ; @param (*) item-value
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @example
  ;  (db/get-document-kv [{...} {...} {:foo "bar"}] :foo "bar")
  ;  => {:foo "bar"}
  ;
  ; @return (map)
  ([collection item-key item-value]
   (get-document-kv collection item-key item-value {}))

  ([collection item-key item-value {:keys [additional-namespace remove-namespace?]}]
   (reduce-while #(cond (and (= (param item-value)
                                (get %2 item-key))
                             (some? additional-namespace))
                        (document->namespaced-document %2 additional-namespace)
                        (and (= (param item-value)
                                (get %2 item-key))
                             (boolean remove-namespace?))
                        (document->non-namespaced-document %2)
                        (= (param item-value)
                           (get %2 item-key))
                        (return %2))
                  (param nil)
                  (param collection)
                  (fn [%1 _] (map? %1)))))

(defn get-document
  ; Get (first) document by id
  ;
  ; @param (maps in vector) collection
  ; @param (string) document-id
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @usage
  ;  (db/get-document [{...} {...} {...}] "my-document")
  ;
  ; @return (map)
  ([collection document-id]
   (get-document collection document-id {}))

  ([collection document-id context-props]
   (if (collection->collection-namespaced? collection)
       (let [namespace (collection->namespace collection)]
            (get-document-kv collection (keyword/add-namespace namespace :id)
                             document-id context-props))
       (get-document-kv collection :id document-id context-props))))

(defn get-document-item
  ; @param (maps in vector) collection
  ; @param (string) document-id
  ; @param (keyword) item-key
  ;
  ; @example
  ;  (db/get-document-item [{:id "my-document" :label "My document"} {...} {...}]
  ;                        "my-document" :label)
  ;  => "My document"
  ;
  ; @return (*)
  [collection document-id item-key]
  (let [document (get-document collection document-id)]
       (get document item-key)))

(defn get-documents
  ; @param (maps in vector) collection
  ; @param (strings in vector) document-ids
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @return (maps in vector)
  ([collection document-ids]
   (get-documents collection document-ids {}))

  ([collection document-ids {:keys [additional-namespace remove-namespace?]}]
   (reduce #(cond (and (vector/contains-item? document-ids (:id %2))
                       (some? additional-namespace))
                  (let [document (document->namespaced-document %2 additional-namespace)]
                       (vector/conj-item collection document))
                  (and (vector/contains-item? document-ids (:id %2))
                       (boolean remove-namespace?))
                  (let [document (document->non-namespaced-document %2)]
                       (vector/conj-item collection document))
                  (vector/contains-item? document-ids (:id %2))
                  (vector/conj-item %1 %2)
                  :else (return %1))
            (param [])
            (param collection))))

(defn add-document
  ; @param (maps in vector) collection
  ; @param (keyword) document
  ;
  ; @example
  ;  (db/add-document [{:foo "bar"}] {:baz "boo"})
  ;  => [{:foo "bar"} {:baz "boo"}]
  ;
  ; @example
  ;  (db/add-document [{:foo "bar"}] {:bam/baz "boo"})
  ;  => [{:foo "bar"} {:baz "boo"}]
  ;
  ; @example
  ;  (db/add-document [{:bam/foo "bar"}] {:baz "boo"})
  ;  => [{:bam/foo "bar"} {:bam/baz "boo"}]
  ;
  ; @return (maps in vector)
  [collection document]
  (let [document (document->identified-document document)]
       (cond (collection->collection-namespaced? collection)
             (let [namespace (collection->namespace         collection)
                   document  (document->namespaced-document document namespace)]
                  (vector/conj-item collection document))
             (collection->collection-non-namespaced? collection)
             (let [document (document->non-namespaced-document document)]
                  (vector/conj-item collection document)))))

(defn remove-document
  ; @param (maps in vector) collection
  ; @param (string) document-id
  ;
  ; @example
  ;  (db/remove-document [{:id "1"} {:id "2"}] "2")
  ;  => [{:id "1"}]
  ;
  ; @return (maps in vector)
  [collection document-id]
  (reduce #(if (= document-id (document->document-id %2))
               (return %1)
               (vector/conj-item %1 %2))
           (param [])
           (param collection)))

(defn remove-documents
  ; @param (maps in vector) collection
  ; @param (strings in vector) document-ids
  ;
  ; @example
  ;  (db/remove-documents [{:id "1"} {:id "2"} {:id "3"}] ["1" "3"])
  ;  => [{:id "2"}]
  ;
  ; @return (maps in vector)
  [collection document-ids]
  (reduce #(if (vector/contains-item? document-ids (document->document-id %2))
               (return %1)
               (vector/conj-item %1 %2))
           (param [])
           (param collection)))

(defn update-document
  ; @param (maps in vector) collection
  ; @param (string) document-id
  ; @param (function) f
  ; @param (list of *)(opt) params
  ;
  ; @usage
  ;  (db/update-document [{:id "my-document" :label "My document"}]
  ;                      "my-document" assoc :foo "bar")
  ;
  ; @usage
  ;  (db/update-document [{:id "my-document" :label "My document"}]
  ;                      "my-document" (fn [document] (assoc document :foo "bar")))
  ;
  ; @return (maps in vector)
  [collection document-id f & params]
  (let [document         (get-document collection document-id)
        params           (cons document params)
        updated-document (apply f params)]
       (-> (param collection)
           (remove-document document-id)
           (add-document    updated-document))))

(defn update-document-item
  ; @param (maps in vector) collection
  ; @param (string) document-id
  ; @param (keyword) item-key
  ; @param (function) f
  ; @param (list of *)(opt) params
  ;
  ; @usage
  ;  (db/update-document-item [{:id "my-document" :my-item ["foo" "bar"]}]
  ;                           "my-document" :my-item vector/remove-item "foo")
  ;
  ; @usage
  ;  (db/update-document-item [{:id "my-document" :my-item ["foo" "bar"]}]
  ;                           "my-document" :my-item
  ;                           (fn [document-item] (vector/remove-item document-item "foo")))
  ;
  ; @return (maps in vector)
  [collection document-id item-key f & params]
  (let [document              (get-document      collection document-id)
        document-item         (get-document-item collection document-id item-key)
        params                (cons document-item params)
        updated-document-item (apply f params)
        updated-document      (assoc document item-key updated-document-item)]
       (-> (param collection)
           (remove-document document-id)
           (add-document    updated-document))))

(defn document-exists?
  ; @param (maps in vector) collection
  ; @param (string) document-id
  ;
  ; @return (boolean)
  [collection document-id]
  (some? (get-document collection document-id)))

(defn explode-collection
  ; @param (maps in vector) collection
  ;
  ; @example
  ;  (db/explode-collection [{:foo/id 1} {:foo/id 2}
  ;                          {:bar/id 1} {:bar/id 2}
  ;                          {:id     1} {:id     2}])
  ;  => {:foo [{:foo/id 1} {:foo/id 2}]
  ;      :bar [{:bar/id 1} {:bar/id 2}]
  ;      nil  [{:id     1} {:id     2}]}
  ;
  ; @return (map)
  [collection]
  (reduce (fn [result document]
              (let [namespace (document->namespace document)]
                   (update result namespace vector/conj-item document)))
          (param {})
          (param collection)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-collection!
  ; @param (item-path vector) collection-path
  ; @param (maps in vector) collection
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespace? (boolean)(opt)}
  ;
  ; @usage
  ;  (r db/store-collection! db [:my-collection] [{...} {...}])
  ;
  ; @return (map)
  [db [_ collection-path collection {:keys [additional-namespace remove-namespace?]}]]
  (cond (some? additional-namespace)
        (let [collection (collection->namespaced-collection collection additional-namespace)]
             (assoc-in db collection-path collection))
        (boolean remove-namespace?)
        (let [collection (collection->non-namespaced-collection collection)]
             (assoc-in db collection-path collection))
        :else (assoc-in db collection-path collection)))
