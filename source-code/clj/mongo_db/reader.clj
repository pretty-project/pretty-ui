
(ns mongo-db.reader
    (:require monger.joda-time
              [mid-fruits.candy     :refer [param return]]
              [mid-fruits.vector    :as vector]
              [monger.collection    :as mcl]
              [monger.core          :as mcr]
              [monger.db            :as mdb]
              [mongo-db.adaptation  :as adaptation]
              [mongo-db.aggregation :as aggregation]
              [mongo-db.checking    :as checking]
              [mongo-db.engine      :as engine]
              [x.server-core.api    :as a]
              [x.server-db.api      :as db]))



;; -- Error handling ----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn find-maps
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map) query
  ; @param (map)(opt) projection
  ;
  ; @return (namespaced maps in vector)
  ([collection-name query]
   (let [database @(a/subscribe [:mongo-db/get-connection])]
        (try (vec (mcl/find-maps database collection-name query))
             (catch Exception e (println (str e "\n" {:collection-name collection-name :query query}))))))

  ([collection-name query projection]
   (let [database @(a/subscribe [:mongo-db/get-connection])]
        (try (vec (mcl/find-maps database collection-name query projection))
             (catch Exception e (println (str e "\n" {:collection-name collection-name :query query :projection projection})))))))

(defn find-one-as-map
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map) query
  ;
  ; @return (namespaced map)
  [collection-name query]
  (let [database @(a/subscribe [:mongo-db/get-connection])]
       (try (mcl/find-one-as-map database collection-name query)
            (catch Exception e (println (str e "\n" {:collection-name collection-name :query query}))))))

(defn find-map-by-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (org.bson.types.ObjectId object) document-id
  ;
  ; @return (namespaced map)
  [collection-name document-id]
  (let [database @(a/subscribe [:mongo-db/get-connection])]
       (try (mcl/find-map-by-id database collection-name document-id)
            (catch Exception e (println (str e "\n" {:collection-name collection-name :document-id document-id}))))))

(defn count-documents
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ;
  ; @return (integer)
  [collection-name]
  (let [database @(a/subscribe [:mongo-db/get-connection])]
       (try (mcl/count database collection-name)
            (catch Exception e (println (str e "\n" {:collection-name collection-name}))))))

(defn count-documents-by-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) query
  ;
  ; @return (integer)
  [collection-name query]
  (let [database @(a/subscribe [:mongo-db/get-connection])]
       (try (mcl/count database collection-name query)
            (catch Exception e (println (str e "\n" {:collection-name collection-name :query query}))))))



;; -- Collection functions ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-collection-names
  ; @usage
  ;  (mongo-db/get-collection-names)
  ;
  ; @return (strings in vector)
  []
  (let [database @(a/subscribe [:mongo-db/get-connection])]
       (-> database mdb/get-collection-names vec)))

(defn get-collection-namespace
  ; @param (string) collection-name
  ;
  ; @usage
  ;  (mongo-db/get-collection-namespace "my_collection")
  ;
  ; @return (keyword)
  [collection-name]
  (let [all-documents  (find-maps collection-name {})
        first-document (first     all-documents)]
       (db/document->namespace first-document)))

(defn get-all-document-count
  ; @param (string) collection-name
  ;
  ; @usage
  ;  (mongo-db/get-all-document-count "my_collection")
  ;
  ; @return (integer)
  [collection-name]
  (count-documents collection-name))

(defn collection-empty?
  ; @param (string) collection-name
  ;
  ; @usage
  ;  (mongo-db/collection-empty? "my_collection")
  ;
  ; @return (boolean)
  [collection-name]
  (= 0 (count-documents collection-name)))

(defn get-document-count-by-query
  ; @param (string) collection-name
  ; @param (namespaced map) query
  ;
  ; @usage
  ;  (mongo-db/get-document-count-by-query "my_collection" {:namespace/my-keyword :my-value})
  ;
  ; @usage
  ;  (mongo-db/get-document-count-by-query "my_collection" {:$or [{...} {...}]})
  ;
  ; @usage
  ;  (mongo-db/get-document-count-by-query "my_collection" {:namespace/my-keyword  :my-value}
  ;                                                         :namespace/your-string "Your value"})
  ;
  ; @return (integer)
  [collection-name query]
  (if-let [query (-> query checking/find-query adaptation/find-query)]
          (count-documents-by-query collection-name query)))

(defn get-all-documents
  ; @param (string) collection-name
  ; @param (namespaced map)(opt) projection
  ;
  ; @example
  ;  (mongo-db/get-all-documents "my_collection" {:namespace/my-keyword  0
  ;                                               :namespace/your-string 1})
  ;  =>
  ;  [{:namespace/my-keyword  :my-value
  ;    :namespace/your-string "Your value"
  ;    :namespace/id          "MyObjectId"}]
  ;
  ; @return (maps in vector)
  ;  [{:namespace/id (string)}]
  ([collection-name]
   (if-let [all-documents (find-maps collection-name {})]
           (vector/->items all-documents #(adaptation/find-output %))))

  ([collection-name projection]
   (if-let [projection (adaptation/find-projection projection)]
           (if-let [all-documents (find-maps collection-name {} projection)]
                   (vector/->items all-documents #(adaptation/find-output %))))))

(defn get-documents-by-query
  ; @param (string) collection-name
  ; @param (namespaced map) query
  ; @param (namespaced map)(opt) projection
  ;
  ; @usage
  ;  (mongo-db/get-documents-by-query "my_collection" {:namespace/my-keyword :my-value})
  ;
  ; @usage
  ;  (mongo-db/get-documents-by-query "my_collection" {:$or [{...} {...}]})
  ;
  ; @example
  ;  (mongo-db/get-documents-by-query "my_collection" {:namespace/my-keyword :my-value}
  ;                                                   {:namespace/my-keyword  0
  ;                                                    :namespace/your-string 1})
  ;  =>
  ;  [{:namespace/your-string "Your value"
  ;    :namespace/id          "MyObjectId"}]
  ;
  ; @return (namespaced maps in vector)
  ;  [{:namespace/id (string)}]
  ([collection-name query]
   (if-let [query (-> query checking/find-query adaptation/find-query)]
           (if-let [documents (find-maps collection-name query)]
                   (vector/->items documents #(adaptation/find-output %)))))

  ([collection-name query projection]
   (if-let [query (-> query checking/find-query adaptation/find-query)]
           (if-let [projection (adaptation/find-projection projection)]
                   (if-let [documents (find-maps collection-name query projection)]
                           (vector/->items documents #(adaptation/find-output %)))))))



;; -- Document functions ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-document-by-query
  ; @param (string) collection-name
  ; @param (namespaced map) query
  ;
  ; @usage
  ;  (mongo-db/get-document-by-query "my_collection" {:namespace/my-keyword :my-value})
  ;
  ; @usage
  ;  (mongo-db/get-document-by-query "my_collection" {:$or [{...} {...}]})
  ;
  ; @example
  ;  (mongo-db/get-document-by-query "my_collection" {:namespace/my-keyword :my-value})
  ;  =>
  ;  {:namespace/my-keyword  :my-value
  ;   :namespace/your-string "Your value"
  ;   :namespace/id          "MyObjectId"}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  [collection-name query]
  (if-let [query (-> query checking/find-query adaptation/find-query)]
          (if-let [document (find-one-as-map collection-name query)]
                  (adaptation/find-output document))))

(defn get-document-by-id
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @example
  ;  (mongo-db/get-document-by-id "my_collection" "MyObjectId")
  ;  =>
  ;  {:namespace/my-keyword  :my-value
  ;   :namespace/your-string "Your value"
  ;   :namespace/id          "MyObjectId"}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  [collection-name document-id]
  (if-let [document-id (adaptation/document-id-input document-id)]
          (if-let [document (find-map-by-id collection-name document-id)]
                  (adaptation/find-output document))))

(defn document-exists?
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @usage
  ;  (mongo-db/document-exists? "my_collection" "MyObjectId")
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
  ;  (mongo-db/get-documents-by-pipeline "my_collection" [...])
  ;
  ; @usage
  ;  (mongo-db/get-documents-by-pipeline "my_collection" (mongo-db/get-pipeline {...}))
  ;
  ; @return (namespaced maps in vector)
  [collection-name pipeline]
  (if-let [documents (aggregation/process collection-name pipeline)]
          (vector/->items documents #(adaptation/find-output %))
          (return [])))

(defn count-documents-by-pipeline
  ; @param (string) collection-name
  ; @param (maps in vector) pipeline
  ;
  ; @usage
  ;  (mongo-db/count-documents-by-pipeline "my_collection" [...])
  ;
  ; @usage
  ;  (mongo-db/count-documents-by-pipeline "my_collection" (mongo-db/count-pipeline {...}))
  ;
  ; @return (integer)
  [collection-name pipeline]
  (if-let [documents (aggregation/process collection-name pipeline)]
          (count  documents)
          (return 0)))
