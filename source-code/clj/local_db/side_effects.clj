
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns local-db.side-effects
    (:require [local-db.config   :as config]
              [local-db.helpers  :as helpers]
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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn collection-name->max-filesize-reached?
  ; @param (string) collection-name
  ;
  ; @return (boolean)
  [collection-name]
  (let [filepath (helpers/collection-name->filepath collection-name)]
       (io/max-filesize-reached? filepath config/MAX-FILESIZE)))

(defn collection-name->collection-exists?
  ; @param (string) collection-name
  ;
  ; @return (boolean)
  [collection-name]
  (let [filepath (helpers/collection-name->filepath collection-name)]
       (io/file-exists? filepath)))

(defn collection-name->collection-writable?
  ; @param (string) collection-name
  ;
  ; @return (boolean)
  [collection-name]
  (and (-> collection-name collection-name->collection-exists?)
       (-> collection-name collection-name->max-filesize-reached? not)))

(defn collection-name->collection-readable?
  ; @param (string) collection-name
  ;
  ; @return (boolean)
  [collection-name]
  (let [filepath (helpers/collection-name->filepath collection-name)]
       (io/file-exists? filepath)))



;; -- Collection functions ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-collection
  ; @param (string) collection-name
  ;
  ; @example
  ;  (local-db/get-collection "my-collection")
  ;  =>
  ;  [{...} {...} {...}]
  ;
  ; @return (vector)
  [collection-name]
  (if (collection-name->collection-exists? collection-name)
      (-> collection-name helpers/collection-name->filepath io/read-edn-file)))

(defn set-collection!
  ; @param (string) collection-name
  ; @param (maps in vector) collection
  ;
  ; @usage
  ;  (local-db/set-collection! "my-collection" [{...} {...} {...}])
  ;
  ; @return (nil)
  [collection-name collection]
  (if (collection-name->collection-writable? collection-name)
      (let [filepath (helpers/collection-name->filepath collection-name)]
           (io/write-edn-file! filepath collection {:abc? true}))))



;; -- Document functions ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn filter-documents
  ; @param (string) collection-name
  ; @param (function) filter-f
  ;
  ; @usage
  ;  (local-db/filter-documents "my-collection" #(= :value (:key %1)))
  ;
  ; @return (maps in vector)
  [collection-name filter-f]
  (let [collection (get-collection collection-name)]
       (db/filter-documents collection filter-f)))

(defn filter-document
  ; @param (string) collection-name
  ; @param (function) filter-f
  ;
  ; @usage
  ;  (local-db/filter-document "my-collection" #(= :value (:key %1)))
  ;
  ; @return (map)
  [collection-name filter-f]
  (let [collection (get-collection collection-name)]
       (db/filter-document collection filter-f)))

(defn match-documents
  ; @param (string) collection-name
  ; @param (map) pattern
  ;
  ; @example
  ;  (local-db/match-documents "my-collection" {:foo "bar"})
  ;  =>
  ;  [{:foo "bar" :baz "boo"}]
  ;
  ; @return (maps in vector)
  [collection-name pattern]
  (let [collection (get-collection collection-name)]
       (db/match-documents collection pattern)))

(defn match-document
  ; @param (string) collection-name
  ; @param (keyword) pattern
  ;
  ; @example
  ;  (local-db/match-document "my-collection" {:foo "bar"})
  ;  =>
  ;  {:foo "bar" :baz "boo"}
  ;
  ; @return (map)
  [collection-name pattern]
  (let [collection (get-collection collection-name)]
       (db/match-document collection pattern)))

(defn get-documents-kv
  ; @param (string) collection-name
  ; @param (keyword) item-key
  ; @param (*) item-value
  ;
  ; @return (maps in vector)
  [collection-name item-key item-value]
  (let [collection (get-collection collection-name)]
       (db/get-documents-kv collection item-key item-value)))

(defn get-document-kv
  ; @param (string) collection-name
  ; @param (keyword) item-key
  ; @param (*) item-value
  ;
  ; @return (map)
  [collection-name item-key item-value]
  (let [collection (get-collection collection-name)]
       (db/get-document-kv collection item-key item-value)))

(defn get-documents
  ; @param (string) collection-name
  ; @param (strings in vector) document-ids
  ;
  ; @return (maps in vector)
  [collection-name document-ids]
  (let [collection (get-collection collection-name)]
       (db/get-documents collection document-ids)))

(defn get-document
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @return (map)
  [collection-name document-id]
  (let [collection (get-collection collection-name)]
       (db/get-document collection document-id)))

(defn get-document-item
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (keyword) item-key
  ;
  ; @return (*)
  ([collection-name document-id item-key]
   (let [collection (get-collection collection-name)]
        (db/get-document-item collection document-id item-key))))

(defn add-document!
  ; @param (string) collection-name
  ; @param (map) document
  ;
  ; @return (nil)
  [collection-name document]
  (let [collection (get-collection collection-name)
        document   (time/unparse-date-time document)]
       (set-collection! collection-name (db/add-document collection document))))

(defn remove-documents!
  ; @param (string) collection-name
  ; @param (strings in vector) document-ids
  ;
  ; @return (nil)
  [collection-name document-ids]
  (let [collection (get-collection collection-name)]
       (set-collection! collection-name (db/remove-documents collection document-ids))))

(defn remove-document!
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @return (nil)
  [collection-name document-id]
  (let [collection (get-collection collection-name)]
       (set-collection! collection-name (db/remove-document collection document-id))))

(defn set-document!
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map) document
  ;
  ; @return (nil)
  [collection-name document-id document]
  (let [collection (get-collection collection-name)
        document   (time/unparse-date-time document)]
       (set-collection! collection-name (-> collection (db/remove-document document-id)
                                                       (db/add-document    document)))))

(defn apply-document!
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (function) f
  ; @param (list of *)(opt) params
  ;
  ; @usage
  ;  (local-db/apply-document! "my-collection" "my-document" assoc :foo "bar")
  ;
  ; @usage
  ;  (local-db/apply-document! "my-collection" "my-document"
  ;                            (fn [document] (assoc document :foo "bar")))
  ;
  ; @return (nil)
  [collection-name document-id f & params]
  ; XXX#8075
  ; Az x4.4.1 verzióig az apply-document! függvény a db/apply-document függvény
  ; alkalmazásával volt megvalósítva, amely nem tette lehetővé a params listában
  ; átadott anoním függvényekben lévő dátum objektumok string típussá alakítását.
  (let [collection       (get-collection  collection-name)
        document         (db/get-document collection document-id)
        params           (cons document params)
        updated-document (apply f params)
        updated-document (time/unparse-date-time updated-document)]
       (set-collection! collection-name (-> collection (db/remove-document document-id)
                                                       (db/add-document    updated-document)))))

(defn document-exists?
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @return (boolean)
  [collection-name document-id]
  (let [collection (get-collection collection-name)]
       (db/document-exists? collection document-id)))
