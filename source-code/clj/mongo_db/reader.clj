
(ns mongo-db.reader
   ; WARNING! DEPRECATED! DO NOT USE!
   ;(:import  org.bson.types.BSONTimestamp)
   ; WARNING! DEPRECATED! DO NOT USE!
    (:require monger.joda-time
              [mid-fruits.candy     :refer [param return]]
              [mid-fruits.vector    :as vector]
              [monger.collection    :as mcl]
              [monger.core          :as mcr]
              [mongo-db.adaptation  :as adaptation]
              [mongo-db.aggregation :as aggregation]
              [mongo-db.engine      :as engine]
              [x.server-core.api    :as a]
              [x.server-db.api      :as db]))



;; -- Error handling ----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- find-maps
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map) query
  ; @param (map)(opt) projection
  ;
  ; @return (vector)
  ([collection-name query]
   (let [database (a/subscribed [:mongo-db/get-connection])]
        (try (vec (mcl/find-maps database collection-name query))
             (catch Exception e (println (str e "\n" {:collection-name collection-name :query query}))))))

  ([collection-name query projection]
   (let [database (a/subscribed [:mongo-db/get-connection])]
        (try (vec (mcl/find-maps database collection-name query projection))
             (catch Exception e (println (str e "\n" {:collection-name collection-name :query query :projection projection})))))))

(defn- find-one-as-map
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map) query
  ;
  ; @return (namespaced map)
  [collection-name query]
  (let [database (a/subscribed [:mongo-db/get-connection])]
       (try (mcl/find-one-as-map database collection-name query)
            (catch Exception e (println (str e "\n" {:collection-name collection-name :query query}))))))

(defn- find-map-by-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (org.bson.types.ObjectId object) document-id
  ;
  ; @return (namespaced map)
  [collection-name document-id]
  (let [database (a/subscribed [:mongo-db/get-connection])]
       (try (mcl/find-map-by-id database collection-name document-id)
            (catch Exception e (println (str e "\n" {:collection-name collection-name :document-id document-id}))))))

(defn- count-documents
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ;
  ; @return (integer)
  [collection-name]
  (let [database (a/subscribed [:mongo-db/get-connection])]
       (try (mcl/count database collection-name)
            (catch Exception e (println (str e "\n" {:collection-name collection-name}))))))

(defn- count-documents-by-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) query
  ;
  ; @return (integer)
  [collection-name query]
  (let [database (a/subscribed [:mongo-db/get-connection])]
       (try (mcl/count database collection-name query)
            (catch Exception e (println (str e "\n" {:collection-name collection-name :query query}))))))



;; -- Collection functions ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-collection-namespace
  ; @param (string) collection-name
  ;
  ; @usage
  ;  (mongo-db/get-collection-namespace "my-collection")
  ;
  ; @return (keyword)
  [collection-name]
  (let [all-documents  (find-maps collection-name {})
        first-document (first all-documents)]
       (db/document->namespace first-document)))

(defn get-all-document-count
  ; @param (string) collection-name
  ;
  ; @usage
  ;  (mongo-db/get-all-document-count "my-collection")
  ;
  ; @return (integer)
  [collection-name]
  (count-documents collection-name))

(defn get-document-count-by-query
  ; @param (string) collection-name
  ; @param (namespaced map) query
  ;
  ; @usage
  ;  (mongo-db/get-document-count-by-query "my-collection" {:namespace/my-keyword  :my-value}
  ;                                                         :namespace/your-string "your-value"})
  ;
  ; @return (integer)
  [collection-name query]
  (if-let [query (adaptation/query-input query)]
          (count-documents-by-query collection-name query)))

(defn get-all-documents
  ; @param (string) collection-name
  ; @param (namespaced map)(opt) projection
  ;
  ; @example
  ;  (mongo-db/get-all-documents "my-collection" {:namespace/my-keyword  0
  ;                                               :namespace/your-string 1})
  ;  =>
  ;  [{:namespace/my-keyword  :my-value
  ;    :namespace/your-string "your-value"
  ;    :namespace/id          "MyObjectId"}]
  ;
  ; @return (maps in vector)
  ;  [{:namespace/id (string)}]
  ([collection-name]
   (if-let [all-documents (find-maps collection-name {})]
           (vector/->items all-documents #(adaptation/get-document-output %))))

  ([collection-name projection]
   (if-let [projection (adaptation/projection-input projection)]
           (if-let [all-documents (find-maps collection-name {} projection)]
                   (vector/->items all-documents #(adaptation/get-document-output %))))))

(defn get-documents-by-query
  ; @param (string) collection-name
  ; @param (namespaced map) query
  ; @param (namespaced map)(opt) projection
  ;
  ; @example
  ;  (mongo-db/get-documents-by-query "my-collection" {:namespace/my-keyword :my-value}
  ;                                                   {:namespace/my-keyword  0
  ;                                                    :namespace/your-string 1})
  ;  =>
  ;  [{:namespace/my-keyword  :my-value
  ;    :namespace/your-string "your-value"
  ;    :namespace/id          "MyObjectId"}]
  ;
  ; @return (namespaced maps in vector)
  ;  [{:namespace/id (string)}]
  ([collection-name query]
   (if-let [query (adaptation/query-input query)]
           (if-let [documents (find-maps collection-name query)]
                   (vector/->items documents #(adaptation/get-document-output %)))))

  ([collection-name query projection]
   (if-let [query (adaptation/query-input query)]
           (if-let [projection (adaptation/projection-input projection)]
                   (if-let [documents (find-maps collection-name query projection)]
                           (vector/->items documents #(adaptation/get-document-output %)))))))



;; -- Document functions ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-document-by-query
  ; @param (string) collection-name
  ; @param (namespaced map) query
  ;
  ; @usage
  ;  (mongo-db/get-document-by-query "my-collection" {:namespace/my-keyword :my-value})
  ;  =>
  ;  {:namespace/my-keyword  :my-value
  ;   :namespace/your-string "your-value"
  ;   :namespace/id          "MyObjectId"}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  [collection-name query]
  (if-let [query (adaptation/query-input query)]
          (if-let [document (find-one-as-map collection-name query)]
                  (adaptation/get-document-output document))))

(defn get-document-by-id
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @example
  ;  (mongo-db/get-document-by-id "my-collection" "MyObjectId")
  ;  =>
  ;  {:namespace/my-keyword  :my-value
  ;   :namespace/your-string "your-value"
  ;   :namespace/id          "MyObjectId"}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  [collection-name document-id]
  (if-let [document-id (adaptation/document-id-input document-id)]
          (if-let [document (find-map-by-id collection-name document-id)]
                  (adaptation/get-document-output document))))

(defn document-exists?
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @usage
  ;  (mongo-db/document-exists? "my-collection" "MyObjectId")
  ;
  ; @return (boolean)
  [collection-name document-id]
  (boolean (if-let [document-id (adaptation/document-id-input document-id)]
                   (find-map-by-id collection-name document-id))))



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
  (if-let [documents (aggregation/process collection-name pipeline)]
          (vector/->items documents #(adaptation/get-document-output %))))

(defn count-documents-by-pipeline
  ; @param (string) collection-name
  ; @param (maps in vector) pipeline
  ;
  ; @usage
  ;  (mongo-db/count-documents-by-pipeline "my-collection" [...])
  ;
  ; @return (integer)
  [collection-name pipeline]
  (if-let [documents (aggregation/process collection-name pipeline)]
          (count documents)))
