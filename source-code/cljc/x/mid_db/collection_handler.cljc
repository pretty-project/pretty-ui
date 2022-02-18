
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
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :as map]
              [mid-fruits.vector  :as vector]
              [x.mid-db.document-handler :as document-handler]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name collection
;  [{:id "my-data" :my-prop "my-value"} {...} {...}]
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
(def document->namespaced-document      document-handler/document->namespaced-document)
(def document->non-namespaced-document  document-handler/document->non-namespaced-document)
(def get-document-value                 document-handler/get-document-value)
(def document->document-id              document-handler/document->document-id)
(def document->identified-document      document-handler/document->identified-document)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

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

(defn collection->namespaced-collection
  ; @param (maps in vector) collection
  ; @param (keyword) namespace
  ;
  ; @example
  ;  (db/collection->namespaced-collection [{:foo "bar"} {:foo "boo"}] :baz)
  ;  =>
  ;  [{:baz/foo "bar"} {:baz/foo "boo"}]
  ;
  ; @return (maps in  vector)
  [collection namespace]
  (vector/->items collection #(document->namespaced-document % namespace)))

(defn collection->non-namespaced-collection
  ; @param (maps in vector) collection
  ;
  ; @example
  ;  (db/collection->namespaced-collection [{:baz/foo "bar"} {:baz/foo "boo"}])
  ;  =>
  ;  [{:foo "bar"} {:foo "boo"}]
  ;
  ; @return (maps in  vector)
  [collection]
  (vector/->items collection #(document->non-namespaced-document %)))



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
  ;  =>
  ;  [{:id "1"} {:id "2"}]
  ;
  ; @example
  ;  (db/trim-collection [{:id "1"} {:id "2"} {:id "3"} {:id "4"} {:id "5"}] 2 1)
  ;  =>
  ;  [{:id "2"} {:id "3"} {:id "4"}]
  ;
  ; @example
  ;  (db/trim-collection [{:id "1"} {:id "2"} {:id "3"} {:id "4"} {:id "5"}] 2 4)
  ;  =>
  ;  [{:id "5"}]
  ;
  ; @return (maps in vector)
  ([collection max-count]
   (trim-collection collection max-count 0))

  ([collection max-count skip]
   (take max-count (drop skip collection))))

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
   (letfn [(f [result document]
              (cond ; If document is NOT matches ...
                    (-> document filter-f not)
                    (return result)
                    ; If document is matches and add namespace ...
                    (some? additional-namespace)
                    (conj result (document->namespaced-document document additional-namespace))
                    ; If document is matches and remove namespace ...
                    (boolean remove-namespace?)
                    (conj result (document->non-namespaced-document document))
                    ; If document is matches ...
                    :else (conj result document)))]
          (reduce f [] collection))))

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
   (if-let [document (vector/first-filtered collection filter-f)]
           (cond (some?   additional-namespace) (document->namespaced-document     document additional-namespace)
                 (boolean remove-namespace?)    (document->non-namespaced-document document)
                 :else                          (return                            document)))))

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
  ;  =>
  ;  [{:foo "bar"} {:foo "bar"}]
  ;
  ; @return (maps in vector)
  ([collection pattern]
   (match-documents collection pattern {}))

  ([collection pattern {:keys [additional-namespace remove-namespace?]}]
   (letfn [(f [result document]
              (cond ; If document is NOT matches ...
                    (-> document (map/match-pattern? pattern) not)
                    (return result)
                    ; If document is matches and add namespace ...
                    (some? additional-namespace)
                    (conj result (document->namespaced-document document additional-namespace))
                    ; If document is matches and remove namespace ...
                    (boolean remove-namespace?)
                    (conj result (document->non-namespaced-document document))
                    ; If document is matches ...
                    :else (conj result document)))]
          (reduce f [] collection))))

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
  ;  =>
  ;  {:foo "bar"}
  ;
  ; @return (map)
  ([collection pattern]
   (match-document collection pattern {}))

  ([collection pattern {:keys [additional-namespace remove-namespace?]}]
   (if-let [document (vector/first-filtered collection #(map/match-pattern? % pattern))]
           (cond (some?   additional-namespace) (document->namespaced-document     document additional-namespace)
                 (boolean remove-namespace?)    (document->non-namespaced-document document)
                 :else                          (return                            document)))))

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
  ;  =>
  ;  [{:foo "bar"} {:foo "bar"}]
  ;
  ; @return (maps in vector)
  ([collection item-key item-value]
   (get-documents-kv collection item-key item-value {}))

  ([collection item-key item-value {:keys [additional-namespace remove-namespace?]}]
   (letfn [(f [result document]
              (cond ; If document is NOT matches ...
                    (not= item-value (get document item-key))
                    (return result)
                    ; If document is matches and add namespace ...
                    (some? additional-namespace)
                    (conj result (document->namespaced-document document additional-namespace))
                    ; If document is matches and remove namespace ...
                    (boolean remove-namespace?)
                    (conj result (document->non-namespaced-document document))
                    ; If document is matches ...
                    :else (conj result document)))]
          (reduce f [] collection))))

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
  ;  =>
  ;  {:foo "bar"}
  ;
  ; @return (map)
  ([collection item-key item-value]
   (get-document-kv collection item-key item-value {}))

  ([collection item-key item-value {:keys [additional-namespace remove-namespace?]}]
   (if-let [document (vector/first-filtered collection #(= item-value (get % item-key)))]
           (cond (some?   additional-namespace) (document->namespaced-document     document additional-namespace)
                 (boolean remove-namespace?)    (document->non-namespaced-document document)
                 :else                          (return                            document)))))

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
   (if-let [namespace (collection->namespace collection)]
           (get-document-kv collection (keyword/add-namespace namespace :id)
                            document-id context-props)
           (get-document-kv collection :id document-id context-props))))

(defn get-document-item
  ; @param (maps in vector) collection
  ; @param (string) document-id
  ; @param (keyword) item-key
  ;
  ; @example
  ;  (db/get-document-item [{:id "my-document" :label "My document"} {...} {...}]
  ;                        "my-document" :label)
  ;  =>
  ;  "My document"
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
   (letfn [(f [result document]
              (cond ; If result is NOT matches ...
                    (not (vector/contains-item? document-ids (:id document)))
                    (return result)
                    ; If document is matches and add namespace ...
                    (some? additional-namespace)
                    (conj result (document->namespaced-document document additional-namespace))
                    ; If document is matches and remove namespace ...
                    (boolean remove-namespace?)
                    (conj result (document->non-namespaced-document document))
                    ; If document is matches ...
                    :else (conj result document)))]
          (reduce f [] collection))))

(defn add-document
  ; @param (maps in vector) collection
  ; @param (keyword) document
  ;
  ; @example
  ;  (db/add-document [{:foo "bar"}] {:baz "boo"})
  ;  =>
  ;  [{:foo "bar"} {:baz "boo"}]
  ;
  ; @example
  ;  (db/add-document [{:foo "bar"}] {:bam/baz "boo"})
  ;  =>
  ;  [{:foo "bar"} {:baz "boo"}]
  ;
  ; @example
  ;  (db/add-document [{:bam/foo "bar"}] {:baz "boo"})
  ;  =>
  ;  [{:bam/foo "bar"} {:bam/baz "boo"}]
  ;
  ; @return (maps in vector)
  [collection document]
  (let [document (document->identified-document document)]
       (if-let [namespace (collection->namespace collection)]
               (let [document (document->namespaced-document document namespace)]
                    (vector/conj-item collection document))
               (let [document (document->non-namespaced-document document)]
                    (vector/conj-item collection document)))))

(defn remove-document
  ; @param (maps in vector) collection
  ; @param (string) document-id
  ;
  ; @example
  ;  (db/remove-document [{:id "1"} {:id "2"}] "2")
  ;  =>
  ;  [{:id "1"}]
  ;
  ; @return (maps in vector)
  [collection document-id]
  (letfn [(f [result document]
             (if (= document-id (document->document-id document))
                 (return result)
                 (conj   result document)))]
         (reduce f [] collection)))

(defn remove-documents
  ; @param (maps in vector) collection
  ; @param (strings in vector) document-ids
  ;
  ; @example
  ;  (db/remove-documents [{:id "1"} {:id "2"} {:id "3"}] ["1" "3"])
  ;  =>
  ;  [{:id "2"}]
  ;
  ; @return (maps in vector)
  [collection document-ids]
  (letfn [(f [result document]
             (if (vector/contains-item? document-ids (document->document-id document))
                 (return result)
                 (conj   result document)))]
         (reduce f [] collection)))

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
       (-> collection (remove-document document-id)
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
       (-> collection (remove-document document-id)
                      (add-document    updated-document))))

(defn document-exists?
  ; @param (maps in vector) collection
  ; @param (string) document-id
  ;
  ; @return (boolean)
  [collection document-id]
  (boolean (get-document collection document-id)))

(defn explode-collection
  ; @param (maps in vector) collection
  ;
  ; @example
  ;  (db/explode-collection [{:foo/id 1} {:foo/id 2}
  ;                          {:bar/id 1} {:bar/id 2}
  ;                          {:id     1} {:id     2}])
  ;  =>
  ;  {:foo [{:foo/id 1} {:foo/id 2}]
  ;   :bar [{:bar/id 1} {:bar/id 2}]
  ;   nil  [{:id     1} {:id     2}]}
  ;
  ; @return (map)
  [collection]
  (letfn [(f [result document]
             (let [namespace (document->namespace document)]
                  (update result namespace conj document)))]
         (reduce f {} collection)))

(defn get-specified-values
  ; @param (maps in vector) collection
  ; @param (keywords in vector) specified-keys
  ; @param (function)(opt) test-f
  ;  Default: some?
  ;
  ; @example
  ;  (db/get-specified-values [{...} {...}] [:my-key :your-key] string?)
  ;  =>
  ;  {:my-key   ["..." "..."]
  ;   :your-key ["..." "..."]}
  ;
  ; @return (map)
  ([collection specified-keys]
   (get-specified-values collection specified-keys some?))

  ([collection specified-keys test-f]
   (letfn [(f [result document]
              (letfn [(f [result k]
                         (let [v (get document k)]
                              (if (test-f v)
                                  (update result k vector/conj-item-once v)
                                  (return result))))]
                     (reduce f result specified-keys)))]
          (reduce f {} collection))))
