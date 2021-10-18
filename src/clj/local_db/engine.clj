
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.15
; Description:
; Version: v1.1.2
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns local-db.engine
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.string :as string]
              [mid-fruits.time   :as time]
              [mid-fruits.vector :as vector]
              [server-fruits.io  :as io]
              [x.server-db.api   :as db]))



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description XXX#8075
;  Date and time objects
;  Az EDN alapú adattárolás nem teszi lehetővé az időbélyegzők objektumként
;  való tárolását, ezért minden tárolási függvény string típusúvá alakítja
;  az objektum típusú időbélyegzőket



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name {:additional-namespace? ...}
;  XXX#3209
;
; @name {:remove-namespace? true}
;  XXX#3210



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (MB)
(def MAX-FILESIZE (io/MB->B 10))

; @constant (string)
(def LOCAL-DB-PATH "db/")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- collection-id-valid?
  ; @param (*) collection-id
  ;
  ; @return (boolean)
  [collection-id]
  (string/nonempty? collection-id))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- collection-id->filepath
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-id
  ;
  ; @return (string)
  [collection-id]
  (str LOCAL-DB-PATH collection-id ".edn"))

(defn- collection-id->max-filesize-reached?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-id
  ;
  ; @return (boolean)
  [collection-id]
  (let [filepath (collection-id->filepath collection-id)]
       (io/max-filesize-reached? filepath MAX-FILESIZE)))

(defn- collection-id->collection-exists?
  ; @param (string) collection-id
  ;
  ; @return (boolean)
  [collection-id]
  (let [filepath (collection-id->filepath collection-id)]
       (io/file-exists? filepath)))

(defn- collection-id->collection-writable?
  ; @param (string) collection-id
  ;
  ; @return (boolean)
  [collection-id]
  (and (collection-id->collection-exists?         collection-id)
       (not (collection-id->max-filesize-reached? collection-id))))

(defn- collection-id->collection-readable?
  ; @param (string) collection-id
  ;
  ; @return (boolean)
  [collection-id]
  (let [filepath (collection-id->filepath collection-id)]
       (io/file-exists? filepath)))

(defn- collection-id->collection-regable?
  ; @param (string) collection-id
  ;
  ; @return (boolean)
  [collection-id]
  (and (collection-id-valid? collection-id)
       (not (collection-id->collection-exists? collection-id))))



;; -- Collection functions ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-collection
  ; @param (string) collection-id
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespaced? (boolean)(opt)}
  ;
  ; @example
  ;  (local-db/get-collection "my-collection")
  ;  => [{...} {...} {...}]
  ;
  ; @return (vector)
  ([collection-id]
   (get-collection collection-id {}))

  ([collection-id {:keys [additional-namespace remove-namespace?]}]
   (if (collection-id->collection-exists? collection-id)
       (let [filepath   (collection-id->filepath collection-id)
             collection (io/read-edn-file filepath)]
            (cond (some? additional-namespace)
                  (db/collection->namespaced-collection collection additional-namespace)
                  (boolean remove-namespace?)
                  (db/collection->non-namespaced-collection collection)
                  :else (return collection))))))

(defn set-collection!
  ; @param (string) collection-id
  ; @param (map) collection
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespaced? (boolean)(opt)}
  ;
  ; @usage
  ;  (local-db/set-collection! "my-collection" [{...} {...} {...}])
  ;
  ; @return (nil)
  ([collection-id collection]
   (set-collection! collection-id collection {}))

  ([collection-id collection {:keys [additional-namespace remove-namespace?]}]
   (if (collection-id->collection-writable? collection-id)
       (let [filepath (collection-id->filepath collection-id)]
            (cond (some? additional-namespace)
                  (let [collection (db/collection->namespaced-collection collection additional-namespace)]
                       (io/write-edn-file! filepath collection {:abc? true}))
                  (boolean remove-namespace?)
                  (let [collection (db/collection->non-namespaced-collection collection)]
                       (io/write-edn-file! filepath collection {:abc? true}))
                  :else (io/write-edn-file! filepath collection {:abc? true}))))))



;; -- Document functions ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn filter-documents
  ; @param (string) collection-id
  ; @param (function) filter-f
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespaced? (boolean)(opt)}
  ;
  ; @usage
  ;  (local-db/filter-documents "my-collection" #(= :value (:key %1)))
  ;
  ; @return (vector)
  ([collection-id filter-f]
   (filter-documents collection-id filter-f {}))

  ([collection-id filter-f context-props]
   (let [collection (get-collection collection-id context-props)]
        (db/filter-documents collection filter-f))))

(defn filter-document
  ; @param (string) collection-id
  ; @param (function) filter-f
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespaced? (boolean)(opt)}
  ;
  ; @usage
  ;  (local-db/filter-document "my-collection" #(= :value (:key %1)))
  ;
  ; @return (map)
  ([collection-id filter-f]
   (filter-document collection-id filter-f {}))

  ([collection-id filter-f context-props]
   (let [collection (get-collection collection-id context-props)]
        (db/filter-document collection filter-f))))

(defn match-documents
  ; @param (string) collection-id
  ; @param (map) pattern
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespaced? (boolean)(opt)}
  ;
  ; @example
  ;  (local-db/match-documents "my-collection" {:foo "bar"})
  ;  => [{:foo "bar" :baz "boo"}]
  ;
  ; @return (vector)
  ([collection-id pattern]
   (match-documents collection-id pattern {}))

  ([collection-id pattern context-props]
   (let [collection (get-collection collection-id)]
        (db/match-documents collection pattern context-props))))

(defn match-document
  ; @param (string) collection-id
  ; @param (keyword) pattern
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespaced? (boolean)(opt)}
  ;
  ; @example
  ;  (local-db/match-document "my-collection" {:foo "bar"})
  ;  => {:foo "bar" :baz "boo"}
  ;
  ; @return (map)
  ([collection-id pattern]
   (match-document collection-id pattern {}))

  ([collection-id pattern context-props]
   (let [collection (get-collection collection-id)]
        (db/match-document collection pattern context-props))))

(defn get-documents-kv
  ; @param (string) collection-id
  ; @param (keyword) item-key
  ; @param (*) item-value
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespaced? (boolean)(opt)}
  ;
  ; @return (maps in vector)
  ([collection-id item-key item-value]
   (get-documents-kv collection-id item-key item-value {}))

  ([collection-id item-key item-value context-props]
   (let [collection (get-collection collection-id)]
        (db/get-documents-kv collection item-key item-value context-props))))

(defn get-document-kv
  ; @param (string) collection-id
  ; @param (keyword) item-key
  ; @param (*) item-value
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespaced? (boolean)(opt)}
  ;
  ; @return (map)
  ([collection-id item-key item-value]
   (get-document-kv collection-id item-key item-value {}))

  ([collection-id item-key item-value context-props]
   (let [collection (get-collection collection-id)]
        (db/get-document-kv collection item-key item-value context-props))))

(defn get-documents
  ; @param (string) collection-id
  ; @param (strings in vector) document-ids
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespaced? (boolean)(opt)}
  ;
  ; @return (maps in vector)
  ([collection-id document-ids]
   (get-documents collection-id document-ids {}))

  ([collection-id document-ids context-props]
   (let [collection (get-collection collection-id)]
        (db/get-documents collection document-ids context-props))))

(defn get-document
  ; @param (string) collection-id
  ; @param (string) document-id
  ; @param (map)(opt) context-props
  ;  {:additional-namespace (keyword)(opt)
  ;   :remove-namespaced? (boolean)(opt)}
  ;
  ; @return (map)
  ([collection-id document-id]
   (get-document collection-id document-id {}))

  ([collection-id document-id context-props]
   (let [collection (get-collection collection-id)]
        (db/get-document collection document-id context-props))))

(defn get-document-item
  ; @param (string) collection-id
  ; @param (string) document-id
  ; @param (keyword) item-key
  ;
  ; @return (*)
  ([collection-id document-id item-key]
   (let [collection (get-collection collection-id)]
        (db/get-document-item collection document-id item-key))))

(defn add-document!
  ; @param (string) collection-id
  ; @param (map) document
  ;
  ; @return (nil)
  [collection-id document]
  (let [collection (get-collection collection-id)
        document   (time/unparse-date-time document)]
       (set-collection! (param collection-id)
                        (db/add-document collection document))))

(defn remove-documents!
  ; @param (string) collection-id
  ; @param (strings in vector) document-ids
  ;
  ; @return (nil)
  [collection-id document-ids]
  (let [collection (get-collection collection-id)]
       (set-collection! (param collection-id)
                        (db/remove-documents collection document-ids))))

(defn remove-document!
  ; @param (string) collection-id
  ; @param (string) document-id
  ;
  ; @return (nil)
  [collection-id document-id]
  (let [collection (get-collection collection-id)]
       (set-collection! (param collection-id)
                        (db/remove-document collection document-id))))

(defn set-document!
  ; @param (string) collection-id
  ; @param (string) document-id
  ; @param (map) document
  ;
  ; @return (nil)
  [collection-id document-id document]
  (let [collection (get-collection collection-id)
        document   (time/unparse-date-time document)]
       (set-collection! (param collection-id)
                        (-> (db/remove-document document-id)
                            (db/add-document    document)))))

(defn update-document!
  ; @param (string) collection-id
  ; @param (string) document-id
  ; @param (function) f
  ; @param (list of *)(opt) params
  ;
  ; @usage
  ;  (local-db/update-document! "my-collection" "my-document" assoc :foo "bar")
  ;
  ; @usage
  ;  (local-db/update-document! "my-collection" "my-document"
  ;                             (fn [document] (assoc document :foo "bar")))
  ;
  ; @return (nil)
  [collection-id document-id f & params]
  ; XXX#8075
  ; Az x4.4.1 verzióig az update-document! függvény a db/update-document függvény
  ; alkalmazásával volt megvalósítva, amely nem tette lehetővé a params listában
  ; átadott anoním függvényekben lévő dátum objektumok string típussá alakítását.
  (let [collection       (get-collection  collection-id)
        document         (db/get-document collection document-id)
        params           (cons document params)
        updated-document (apply f params)
        updated-document (time/unparse-date-time updated-document)]
       (set-collection! (param collection-id)
                        (-> (param collection)
                            (db/remove-document document-id)
                            (db/add-document    updated-document)))))

(defn update-document-item!
  ; @param (string) collection-id
  ; @param (string) document-id
  ; @param (keyword) item-key
  ; @param (function) f
  ; @param (list of *)(opt) params
  ;
  ; @usage
  ;  (local-db/update-document-item! "my-collection" "my-document" :my-item vector/remove-item "foo")
  ;
  ; @usage
  ;  (local-db/update-document! "my-collection" "my-document" :my-item
  ;                             (fn [document-item] (vector/remove-item document-item "foo")))
  ;
  ; @return (nil)
  [collection-id document-id item-key f & params]
  ; XXX#8075
  (let [collection            (get-collection       collection-id)
        document              (db/get-document      collection document-id)
        document-item         (db/get-document-item collection document-id item-key)
        params                (cons document-item params)
        updated-document-item (apply f params)
        updated-document      (assoc document item-key updated-document-item)
        updated-document (time/unparse-date-time updated-document)]
       (set-collection! (param collection-id)
                        (-> (param collection)
                            (db/remove-document document-id)
                            (db/add-document    updated-document)))))

(defn document-exists?
  ; @param (string) collection-id
  ; @param (string) document-id
  ;
  ; @return (boolean)
  [collection-id document-id]
  (let [collection (get-collection collection-id)]
       (db/document-exists? collection document-id)))
