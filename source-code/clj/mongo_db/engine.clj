
(ns mongo-db.engine
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.json    :as json]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.random  :as random]
              [mid-fruits.time    :as time]
              [monger.conversion  :as mcv]
              [x.server-db.api    :as db]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DEFAULT-LOCALE "hu")

; @constant (string)
(def MISSING-NAMESPACE-ERROR "Document must be a namespaced map with keyword type keys!")



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn DBObject->edn
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DBObject) n
  ;
  ; @return (map)
  [n]
  (try (mcv/from-db-object n true)
       (catch Exception e (println e))))

(defn id->_id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) document
  ;  {:namespace/id (string)}
  ;
  ; @example
  ;  (engine/id->_id {:my-namespace/id :my-id :my-namespace/my-key "my-value"})
  ;  =>
  ;  {:_id :my-id :my-namespace/my-key "my-value"}
  ;
  ; @return (map)
  ;  {:_id (string)}
  [document]
  (if-let [namespace (db/document->namespace document)]
          (let [id-key      (keyword/add-namespace  namespace :id)
                document-id (get                    document  id-key)]
               (-> document (assoc  :_id document-id)
                            (dissoc id-key)))
          (throw (Exception. MISSING-NAMESPACE-ERROR))))

(defn _id->id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) document
  ;  {:_id (string)}
  ;
  ; @example
  ;  (engine/_id->id {:_id :my-id :my-namespace/key :my-value})
  ;  =>
  ;  {:my-namespace/id :my-id :my-namespace/my-key "my-value"}
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  [document]
  (if-let [namespace (db/document->namespace document)]
          (let [id-key      (keyword/add-namespace  namespace :id)
                document-id (get                    document  :_id)]
               (-> document (assoc  id-key document-id)
                            (dissoc :_id)))
          (throw (Exception. MISSING-NAMESPACE-ERROR))))

(defn _ids->ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (maps in vector) documents
  ;
  ; @example
  ;  (engine/_ids->ids [{:_id "my-document" :my-namespace/my-key "my-value"}])
  ;  =>
  ;  [{:my-namespace/id "my-document" :my-namespace/my-key "my-value"}]
  ;
  ; @return (maps in vector)
  [documents]
  (mapv _id->id documents))

(defn document<-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) document
  ;  {:namespace/id (string)(opt)}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  [document]
  (if-let [namespace (db/document->namespace document)]
          (let [id-key (keyword/add-namespace  namespace :id)]
               (if-let [document-id (get document id-key)]
                       (return document)
                       (let [document-id (random/generate-string)]
                            (assoc document id-key document-id))))
          (throw (Exception. MISSING-NAMESPACE-ERROR))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adapt-input
  ; @param (namespaced map) document
  ;
  ; @example
  ;  (engine/adapt-input {:namespace/my-keyword    :my-value
  ;                       :namespace/your-string   "your-value"
  ;                       :namespace/our-timestamp "2020-04-20T16:20:00.000Z"})
  ;  =>
  ;  {"_id"                     "6e9973ec-d982-48ca-8aff-f0efe4b721bb"
  ;   "namespace/my-keyword"    "*:my-value"
  ;   "namespace/your-string"   "your-value"
  ;   "namespace/our-timestamp" #<DateTime 2020-04-20T16:20:00.123Z>}
  ;
  ; @return (namespaced map)
  [document]
  ; - Ha a dokumentum nem rendelkezne string típusú :_id kulcssal, akkor a MongoDB BSON objektum
  ;   típusú :_id kulcsot társítana hozzá!
  ; - A dokumentum :namespace/id tulajdonságának átnevezése :_id tulajdonságra
  ; - A dokumentumban string típusként tárolt dátumok és idők átalakítása objektum típusra
  (try (-> document document<-id id->_id json/unkeywordize-keys json/unkeywordize-values time/parse-date-time)
       (catch Exception e (println (str e "\n" document)))))

(defn adapt-output
  ; @param (namespaced map) document
  ;
  ; @example
  ;  (engine/adapt-output {"_id"                     "6e9973ec-d982-48ca-8aff-f0efe4b721bb"
  ;                        "namespace/my-keyword"    "*:my-value"
  ;                        "namespace/your-string"   "your-value"
  ;                        "namespace/our-timestamp" #<DateTime 2020-04-20T16:20:00.123Z>})
  ;  =>
  ;  {:namespace/id            "6e9973ec-d982-48ca-8aff-f0efe4b721bb"
  ;   :namespace/my-keyword    :my-value
  ;   :namespace/your-string   "your-value"
  ;   :namespace/our-timestamp "2020-04-20T16:20:00.000Z"}
  ;
  ; @return (namespaced map)
  [document]
  (try (-> document json/keywordize-keys json/keywordize-values time/unparse-date-time _id->id)
       (catch Exception e (println (str e "\n" document)))))



;; -- Document order ----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn document->order
  ; @param (namespaced map) document
  ;
  ; @return (integer)
  [document]
  (if-let [namespace (db/document->namespace document)]
          (get document (keyword/add-namespace namespace :order))
          (throw (Exception. MISSING-NAMESPACE-ERROR))))

(defn document<-order
  ; @param (namespaced map) document
  ; @param (integer) order
  ;
  ;
  ; @return (namespaced map)
  ;  {:namespace/order (integer)}
  [document order]
  (if-let [namespace (db/document->namespace document)]
          (assoc document (keyword/add-namespace namespace :order) order)
          (throw (Exception. MISSING-NAMESPACE-ERROR))))
