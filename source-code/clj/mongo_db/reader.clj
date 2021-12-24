
(ns mongo-db.reader
    (:import org.bson.types.BSONTimestamp)
    (:require [mid-fruits.candy    :refer [param return]]
              [mid-fruits.json     :as json]
              [mid-fruits.keyword  :as keyword]
              [mid-fruits.random   :as random]
              [mid-fruits.time     :as time]
              [monger.collection   :as mcl]
              [monger.conversion   :as mcv]
              [monger.core         :as mcr]
              [mongo-db.connection :refer [DB]]
              [mongo-db.engine     :as engine]
              [x.server-db.api     :as db]
              monger.joda-time))



;; -- Aggregation functions ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-from-aggregation
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DBObject) n
  ;
  ; @return (maps in vector)
  [n]
  (-> n engine/DBObject->edn (get-in [:cursor :firstBatch])
        time/unparse-date-time engine/_ids->ids))

(defn- aggregation
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (maps in vector) pipeline
  ; @param (map)(opt) options
  ;  {:locale (string)(opt)
  ;    Default: mongo-db.engine/DEFAULT-LOCALE}
  ;
  ; @return (maps in vector)
  ([collection-name pipeline]
   (aggregation collection-name pipeline {}))

  ([collection-name pipeline {:keys [locale]}]
   (let [locale      (or locale engine/DEFAULT-LOCALE)
         aggregation (mcr/command @DB {:aggregate collection-name
                                       :pipeline  pipeline
                                       :collation {:locale locale :numericOrdering true}
                                       :cursor    {}})]
        (get-from-aggregation aggregation))))



;; -- Reading documents functions ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn find-documents-by-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) query
  ; @param (namespaced map)(opt) projection
  ;
  ; @example
  ;  (read/find-documents-by-query "my-collection" {"my-namespace/my-keyword"  "*:my-value"
  ;                                                 "my-namespace/your-string" "your-value"}
  ;                                                {"my-namespace/my-keyword"  1
  ;                                                 "my-namespace/your-string" 1})
  ;  =>
  ;  {:my-namespace/my-keyword  "*:my-value"
  ;   :my-namespace/your-string "your-value"
  ;   :_id                      "my-document"}
  ;
  ; @return (namespaced maps in vector)
  ;  [{:_id (string)
  ;    :namespace/key (*)}]
  [collection-name query & [projection]]
  (if (some? projection)
      (vec (mcl/find-maps @DB collection-name query projection))
      (vec (mcl/find-maps @DB collection-name query))))

(defn find-all-documents
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map)(opt) projection
  ;
  ; @example
  ;  (read/find-all-documents "my-collection" {"my-namespace/my-keyword"  1
  ;                                            "my-namespace/your-string" 1})
  ;  =>
  ;  [{:my-namespace/my-keyword  "*:my-value"
  ;    :my-namespace/your-string "your-value"
  ;    :_id                      "my-document"}]
  ;
  ; @return (namespaced maps in vector)
  ;  [{:_id (string)
  ;    :namespace/key (*)}]
  [collection-name & [projection]]
  (find-documents-by-query collection-name {} projection))

(defn find-document-by-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) query
  ;
  ; @example
  ;  (read/find-document-by-query "my-collection" {"my-namespace/my-keyword" "*:my-value"})
  ;  =>
  ;  {:my-namespace/my-keyword  "*:my-value"
  ;   :my-namespace/your-string "your-value"
  ;   :_id                      "my-document"}
  ;
  ; @example
  ;  (read/find-document-by-query "my-collection" {"my-namespace/my-keyword"  "*:my-value"
  ;                                                "my-namespace/your-string" "your-value"})
  ;  =>
  ;  {:my-namespace/my-keyword  "*:my-value"
  ;   :my-namespace/your-string "your-value"
  ;   :_id                      "my-document"}
  ;
  ; @return (namespaced map)
  ;  {:_id (string)
  ;   :namespace/key (*)}
  [collection-name query]
  (mcl/find-one-as-map @DB collection-name query))

(defn find-document-by-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @example
  ;  (read/find-document-by-id "my-collection" "my-document")
  ;  =>
  ;  {:my-namespace/my-keyword  "*:my-value"
  ;   :my-namespace/your-string "your-value"
  ;   :_id                      "my-document"}
  ;
  ; @return (namespaced map)
  ;  {:_id (string)
  ;   :namespace/key (*)}
  [collection-name document-id]
  (mcl/find-map-by-id @DB collection-name document-id))



;; -- Collection functions ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-collection-namespace
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ;
  ; @usage
  ;  (read/get-collection-namespace "my-collection")
  ;
  ; @return (keyword)
  [collection-name]
  (let [all-documents  (find-all-documents collection-name)
        first-document (first all-documents)]
       (db/document->namespace first-document)))

(defn get-all-document-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ;
  ; @return (integer)
  [collection-name]
  (mcl/count @DB collection-name))

(defn get-document-count-by-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) query
  ;
  ; @return (integer)
  [collection-name query]
  (mcl/count @DB collection-name query))



;; -- Finding documents functions ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-all-documents
  ; @param (string) collection-name
  ; @param (namespaced map)(opt) projection
  ;
  ; @example
  ;  (mongo-db/get-all-documents "my-collection" {:my-namespace/my-keyword  0
  ;                                               :my-namespace/your-string 1})
  ;  =>
  ;  {:my-namespace/my-keyword  :my-value
  ;   :my-namespace/your-string "your-value"
  ;   :my-namespace/id          "my-document"}
  ;
  ; @return (maps in vector)
  ;  [{:namespace/id (string)}]
  [collection-name & [projection]]
  (vec (reduce (fn [result document]
                   (let [document (-> document (engine/_id->id)
                                               (json/keywordize-values)
                                               (time/unparse-date-time))]
                        (conj result document)))
               (param [])
               (find-all-documents collection-name projection))))

(defn get-documents-by-query
  ; @param (string) collection-name
  ; @param (namespaced map) query
  ; @param (namespaced map)(opt) projection
  ;
  ; @example
  ;  (mongo-db/get-documents-by-query "my-collection" {:my-namespace/my-keyword :my-value}
  ;                                                   {:my-namespace/my-keyword  0
  ;                                                    :my-namespace/your-string 1})
  ;  =>
  ;  {:my-namespace/my-keyword  :my-value
  ;   :my-namespace/your-string "your-value"
  ;   :my-namespace/id          "my-document"}
  ;
  ; @return (namespaced maps in vector)
  ;  [{:namespace/id (string)}]
  [collection-name query & [projection]]
  (let [query      (-> query json/unkeywordize-keys json/unkeywordize-values)
        projection (json/unkeywordize-keys  projection)]
       (vec (reduce (fn [result document]
                        (let [document (-> document (engine/_id->id)
                                                    (json/keywordize-values)
                                                    (time/unparse-date-time))]
                             (conj result document)))
                    (param [])
                    (find-documents-by-query collection-name query projection)))))

(defn get-document-by-query
  ; @param (string) collection-name
  ; @param (namespaced map) query
  ;
  ; @usage
  ;  (mongo-db/get-document-by-query "my-collection" {:my-namespace/my-keyword :my-value})
  ;  =>
  ;  {:my-namespace/my-keyword  :my-value
  ;   :my-namespace/your-string "your-value"
  ;   :my-namespace/id          "my-document"}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  [collection-name query]
  (let [query    (-> query json/unkeywordize-keys json/unkeywordize-values)
        document (find-document-by-query collection-name query)]
       (-> document engine/_id->id json/keywordize-values time/unparse-date-time)))

(defn get-document-by-id
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @example
  ;  (mongo-db/get-document-by-id "my-collection" "my-document")
  ;  =>
  ;  {:my-namespace/my-keyword  :my-value
  ;   :my-namespace/your-string "your-value"
  ;   :my-namespace/id          "my-document"}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  [collection-name document-id]
  (if-let [document (find-document-by-id collection-name document-id)]
          (-> document engine/_id->id json/keywordize-values time/unparse-date-time)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn document-exists?
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @return (boolean)
  [collection-name document-id]
  (let [document (find-document-by-id collection-name document-id)]
       (some? document)))



;; -- Advanced DB functions ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-documents-by-pipeline
  ; @param (string) collection-name
  ; @param (maps in vector) pipeline
  ;
  ; @usage
  ;  (mongo-db/get-documents-by-pipeline "my-collection" [...])
  ;
  ; @return (maps in vector)
  [collection-name pipeline]
  (-> (aggregation collection-name pipeline nil)
      json/keywordize-values time/unparse-date-time))

(defn count-documents-by-pipeline
  ; @param (string) collection-name
  ; @param (maps in vector) pipeline
  ;
  ; @usage
  ;  (mongo-db/count-documents-by-pipeline "my-collection" [...])
  ;
  ; @return (integer)
  [collection-name pipeline]
  (count (aggregation collection-name pipeline nil)))
