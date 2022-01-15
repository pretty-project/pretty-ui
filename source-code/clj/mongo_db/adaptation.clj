
(ns mongo-db.adaptation
    (:import  org.bson.types.ObjectId)
    (:require [mid-fruits.candy :refer [param return]]
              [mid-fruits.json  :as json]
              [mid-fruits.map   :as map]
              [mid-fruits.time  :as time]
              [mongo-db.engine  :as engine]
              [mongo-db.errors  :as errors]
              [x.server-db.api  :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn document-id-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) document-id
  ;
  ; @example
  ;  (adaptation/document-id-input "MyObjectId")
  ;  =>
  ;  #<ObjectId MyObjectId>
  ;
  ; @return (org.bson.types.ObjectId object)
  [document-id]
  (try (ObjectId. document-id)
       (catch Exception e (println (str e "\n" {:document-id document-id})))))

(defn document-id-output
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (org.bson.types.ObjectId object) document-id
  ;
  ; @example
  ;  (adaptation/document-id-output #<ObjectId MyObjectId>)
  ;  =>
  ;  "MyObjectId"
  ;
  ; @return (string)
  [document-id]
  (if document-id (str document-id)))



;; -- Aggregating documents ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn aggregation-output
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DBObject) n
  ;
  ; @return (maps in vector)
  [n]
  (try (-> n engine/DBObject->edn (get-in [:cursor :firstBatch]))
       (catch Exception e (println (str e "\n" {:aggregation-output n})))))



;; -- Get document ------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-document-output
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) document
  ;
  ; @return (namespaced map)
  [document]
  ; 1. A dokumentumban használt string típusra alakított értékek átalakítása kulcsszó típusra
  ; 2. A dokumentumban objektum típusként tárolt dátumok és idők átalakítása string típusra
  ; 3. A dokumentum :_id tulajdonságának átnevezése :namespace/id tulajdonságra
  (try (-> document json/keywordize-values time/unparse-date-time engine/_id->id)
       (catch Exception e (println (str e "\n" {:document document})))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn find-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) query
  ;
  ; @return (namespaced map)
  [query]
  (try (-> query json/unkeywordize-keys json/unkeywordize-values)
       (catch Exception e (println (str e "\n" {:query query})))))

(defn find-projection
  ; @param (namespaced map) projection
  ;
  ; @return (namespaced map)
  [projection]
  (try (-> projection json/unkeywordize-keys)
       (catch Exception e (println (str e "\n" {:projection projection})))))



;; -- Inserting document ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn insert-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) document
  ;
  ; @example
  ;  (adaptation/insert-input {:namespace/id            "MyObjectId"
  ;                            :namespace/my-keyword    :my-value
  ;                            :namespace/your-string   "your-value"
  ;                            :namespace/our-timestamp "2020-04-20T16:20:00.000Z"})
  ;  =>
  ;  {"_id"                     #<ObjectId MyObjectId>
  ;   "namespace/my-keyword"    "*:my-value"
  ;   "namespace/your-string"   "your-value"
  ;   "namespace/our-timestamp" #<DateTime 2020-04-20T16:20:00.123Z>}
  ;
  ; @return (namespaced map)
  [document]
  ; 1. A dokumentum :namespace/id tulajdonságának átnevezése :_id tulajdonságra
  ;    A dokumentum string típusú azonosítójának átalakítása objektum típusra
  ; 2. A dokumentumban használt kulcsszó típusú kulcsok és értékek átalakítása string típusra
  ; 3. A dokumentumban string típusként tárolt dátumok és idők átalakítása objektum típusra
  (try (-> document engine/id->_id json/unkeywordize-keys json/unkeywordize-values time/parse-date-time)
       (catch Exception e (println (str e "\n" {:document document})))))

(defn insert-output
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) document
  ;
  ; @example
  ;  (adaptation/insert-output {"_id"                     #<ObjectId MyObjectId>
  ;                             "namespace/my-keyword"    "*:my-value"
  ;                             "namespace/your-string"   "your-value"
  ;                             "namespace/our-timestamp" #<DateTime 2020-04-20T16:20:00.123Z>})
  ;  =>
  ;  {:namespace/id            "MyObjectId"
  ;   :namespace/my-keyword    :my-value
  ;   :namespace/your-string   "your-value"
  ;   :namespace/our-timestamp "2020-04-20T16:20:00.000Z"}
  ;
  ; @return (namespaced map)
  [document]
  ; 1. A dokumentumban használt string típusra alakított kulcsok és értékek átalakítása kulcsszó típusra
  ; 2. A dokumentumban objektum típusként tárolt dátumok és idők átalakítása string típusra
  ; 3. A dokumentum :_id tulajdonságának átnevezése :namespace/id tulajdonságra
  ;    A dokumentum objektum típusú azonosítójának átalakítása string típusra
  (try (-> document json/keywordize-keys json/keywordize-values time/unparse-date-time engine/_id->id)
       (catch Exception e (println (str e "\n" {:document document})))))



;; -- Saving document ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) document
  ;
  ; @example
  ;  (adaptation/save-input {:namespace/id            "MyObjectId"
  ;                          :namespace/my-keyword    :my-value
  ;                          :namespace/your-string   "your-value"
  ;                          :namespace/our-timestamp "2020-04-20T16:20:00.000Z"})
  ;  =>
  ;  {"_id"                     #<ObjectId MyObjectId>
  ;   "namespace/my-keyword"    "*:my-value"
  ;   "namespace/your-string"   "your-value"
  ;   "namespace/our-timestamp" #<DateTime 2020-04-20T16:20:00.123Z>}
  ;
  ; @return (namespaced map)
  [document]
  ; 1. A dokumentum :namespace/id tulajdonságának átnevezése :_id tulajdonságra
  ;    A dokumentum string típusú azonosítójának átalakítása objektum típusra
  ; 2. A dokumentumban használt kulcsszó típusú kulcsok és értékek átalakítása string típusra
  ; 3. A dokumentumban string típusként tárolt dátumok és idők átalakítása objektum típusra
  (try (-> document engine/id->_id json/unkeywordize-keys json/unkeywordize-values time/parse-date-time)
       (catch Exception e (println (str e "\n" {:document document})))))

(defn save-output
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) document
  ;
  ; @example
  ;  (adaptation/save-output {"_id"                     #<ObjectId MyObjectId>
  ;                           "namespace/my-keyword"    "*:my-value"
  ;                           "namespace/your-string"   "your-value"
  ;                           "namespace/our-timestamp" #<DateTime 2020-04-20T16:20:00.123Z>})
  ;  =>
  ;  {:namespace/id            "MyObjectId"
  ;   :namespace/my-keyword    :my-value
  ;   :namespace/your-string   "your-value"
  ;   :namespace/our-timestamp "2020-04-20T16:20:00.000Z"}
  ;
  ; @return (namespaced map)
  [document]
  ; 1. A dokumentumban használt string típusra alakított kulcsok és értékek átalakítása kulcsszó típusra
  ; 2. A dokumentumban objektum típusként tárolt dátumok és idők átalakítása string típusra
  ; 3. A dokumentum :_id tulajdonságának átnevezése :namespace/id tulajdonságra
  ;    A dokumentum objektum típusú azonosítójának átalakítása string típusra
  (try (-> document json/keywordize-keys json/keywordize-values time/unparse-date-time engine/_id->id)
       (catch Exception e (println (str e "\n" {:document document})))))



;; -- Updating document -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) document
  ;
  ; @example
  ;  (adaptation/update-input {:namespace/my-keyword    :my-value
  ;                            :namespace/your-string   "your-value"
  ;                            :namespace/our-timestamp "2020-04-20T16:20:00.000Z"})
  ;  =>
  ;  {"namespace/my-keyword"    "*:my-value"
  ;   "namespace/your-string"   "your-value"
  ;   "namespace/our-timestamp" #<DateTime 2020-04-20T16:20:00.123Z>}
  ;
  ; @return (namespaced map)
  [document]
  ; 1. A dokumentumban használt kulcsszó típusú kulcsok és értékek átalakítása string típusra
  ; 2. A dokumentumban string típusként tárolt dátumok és idők átalakítása objektum típusra
  (try (-> document json/unkeywordize-keys json/unkeywordize-values time/parse-date-time)
       (catch Exception e (println (str e "\n" {:document document})))))

(defn update-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) document
  ;
  ; @example
  ;  (adaptation/update-query {:namespace/id            "MyObjectId"
  ;                            :namespace/my-keyword    :my-value
  ;                            :namespace/your-string   "your-value"
  ;                            :namespace/our-timestamp "2020-04-20T16:20:00.000Z"})
  ;  =>
  ;  {"_id"                     #<ObjectId MyObjectId>
  ;   "namespace/my-keyword"    "*:my-value"
  ;   "namespace/your-string"   "your-value"
  ;   "namespace/our-timestamp" #<DateTime 2020-04-20T16:20:00.123Z>}
  ;
  ; @return (namespaced map)
  [query]
  (if (map/nonempty? query)
      ; 1. A dokumentum :namespace/id tulajdonságának átnevezése :_id tulajdonságra
      ;    A dokumentum string típusú azonosítójának átalakítása objektum típusra
      ; 2. A dokumentumban használt kulcsszó típusú kulcsok és értékek átalakítása string típusra
      ; 3. A dokumentumban string típusként tárolt dátumok és idők átalakítása objektum típusra
      (try (-> query engine/id->_id json/unkeywordize-keys json/unkeywordize-values time/parse-date-time)
           (catch Exception e (println (str e "\n" {:query query}))))
      ; A query paraméterként lehetséges üres térképet is átadni.
      (return query)))



;; -- Upserting document ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn upsert-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) document
  ;
  ; @example
  ;  (adaptation/upsert-input {:namespace/my-keyword    :my-value
  ;                            :namespace/your-string   "your-value"
  ;                            :namespace/our-timestamp "2020-04-20T16:20:00.000Z"})
  ;  =>
  ;  {"namespace/my-keyword"    "*:my-value"
  ;   "namespace/your-string"   "your-value"
  ;   "namespace/our-timestamp" #<DateTime 2020-04-20T16:20:00.123Z>}
  ;
  ; @return (namespaced map)
  [document]
  ; 1. A dokumentumban használt kulcsszó típusú kulcsok és értékek átalakítása string típusra
  ; 2. A dokumentumban string típusként tárolt dátumok és idők átalakítása objektum típusra
  (try (-> document json/unkeywordize-keys json/unkeywordize-values time/parse-date-time)
       (catch Exception e (println (str e "\n" {:document document})))))

(defn upsert-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) document
  ;
  ; @example
  ;  (adaptation/upsert-query {:namespace/id            "MyObjectId"
  ;                            :namespace/my-keyword    :my-value
  ;                            :namespace/your-string   "your-value"
  ;                            :namespace/our-timestamp "2020-04-20T16:20:00.000Z"})
  ;  =>
  ;  {"_id"                     #<ObjectId MyObjectId>
  ;   "namespace/my-keyword"    "*:my-value"
  ;   "namespace/your-string"   "your-value"
  ;   "namespace/our-timestamp" #<DateTime 2020-04-20T16:20:00.123Z>}
  ;
  ; @return (namespaced map)
  [query]
  (if (map/nonempty? query)
      ; 1. A dokumentum :namespace/id tulajdonságának átnevezése :_id tulajdonságra
      ;    A dokumentum string típusú azonosítójának átalakítása objektum típusra
      ; 2. A dokumentumban használt kulcsszó típusú kulcsok és értékek átalakítása string típusra
      ; 3. A dokumentumban string típusként tárolt dátumok és idők átalakítása objektum típusra
      (try (-> query engine/id->_id json/unkeywordize-keys json/unkeywordize-values time/parse-date-time)
           (catch Exception e (println (str e "\n" {:query query}))))
      ; A query paraméterként lehetséges üres térképet is átadni.
      (return query)))



;; -- Applying document -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) document
  ;
  ; @example
  ;  (adaptation/apply-input {:namespace/id            "MyObjectId"
  ;                           :namespace/my-keyword    :my-value
  ;                           :namespace/your-string   "your-value"
  ;                           :namespace/our-timestamp "2020-04-20T16:20:00.000Z"})
  ;  =>
  ;  {"namespace/my-keyword"    "*:my-value"
  ;   "namespace/your-string"   "your-value"
  ;   "namespace/our-timestamp" #<DateTime 2020-04-20T16:20:00.123Z>}
  ;
  ; @return (namespaced map)
  [document]
  ; 1. A dokumentum :namespace/id tulajdonságának eltávolítása
  ; 2. A dokumentumban használt kulcsszó típusú kulcsok és értékek átalakítása string típusra
  ; 3. A dokumentumban string típusként tárolt dátumok és idők átalakítása objektum típusra
  (try (-> document engine/dissoc-id json/unkeywordize-keys json/unkeywordize-values time/parse-date-time)
       (catch Exception e (println (str e "\n" {:document document})))))

(defn apply-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) document-id
  ;
  ; @example
  ;  (adaptation/apply-query "MyObjectId")
  ;  =>
  ;  {"_id" #<ObjectId MyObjectId>}
  ;
  ; @return (map)
  [document-id]
  (if-let [document-id (document-id-input document-id)]
          {"_id" document-id}))



;; -- Duplicating document ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicate-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) document
  ;
  ; @example
  ;  (adaptation/duplicate-input {:namespace/id            "MyObjectId"
  ;                               :namespace/my-keyword    :my-value
  ;                               :namespace/your-string   "your-value"
  ;                               :namespace/our-timestamp "2020-04-20T16:20:00.000Z"})
  ;  =>
  ;  {"namespace/my-keyword"    "*:my-value"
  ;   "namespace/your-string"   "your-value"
  ;   "namespace/our-timestamp" #<DateTime 2020-04-20T16:20:00.123Z>}
  ;
  ; @return (namespaced map)
  [document]
  ; 1. A dokumentum :namespace/id tulajdonságának eltávolítása
  ; 2. A dokumentumban használt kulcsszó típusú kulcsok és értékek átalakítása string típusra
  ; 3. A dokumentumban string típusként tárolt dátumok és idők átalakítása objektum típusra
  (try (-> document engine/dissoc-id json/unkeywordize-keys json/unkeywordize-values time/parse-date-time)
       (catch Exception e (println (str e "\n" {:document document})))))

(defn duplicate-output
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) document
  ;
  ; @example
  ;  (adaptation/insert-output {"_id"                     #<ObjectId MyObjectId>
  ;                             "namespace/my-keyword"    "*:my-value"
  ;                             "namespace/your-string"   "your-value"
  ;                             "namespace/our-timestamp" #<DateTime 2020-04-20T16:20:00.123Z>})
  ;  =>
  ;  {:namespace/id            "MyObjectId"
  ;   :namespace/my-keyword    :my-value
  ;   :namespace/your-string   "your-value"
  ;   :namespace/our-timestamp "2020-04-20T16:20:00.000Z"}
  ;
  ; @return (namespaced map)
  [document]
  ; 1. A dokumentumban használt string típusra alakított kulcsok és értékek átalakítása kulcsszó típusra
  ; 2. A dokumentumban objektum típusként tárolt dátumok és idők átalakítása string típusra
  ; 3. A dokumentum :_id tulajdonságának átnevezése :namespace/id tulajdonságra
  ;    A dokumentum objektum típusú azonosítójának átalakítása string típusra
  (try (-> document json/keywordize-keys json/keywordize-values time/unparse-date-time engine/_id->id)
       (catch Exception e (println (str e "\n" {:document document})))))



;; -- Filtering documents -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn operator-input
  ; @param (keyword) operator
  ;
  ; @example
  ;  (adaptation/operator-input :and)
  ;  =>
  ;  "$and"
  ;
  ; @return (string)
  [operator]
  (when (keyword? operator)
        (str "$" (name operator))))

(defn pipeline-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) document
  ;
  ; @example
  ;  (adaptation/pipeline-query {:namespace/id            "MyObjectId"
  ;                              :namespace/my-keyword    :my-value
  ;                              :namespace/your-string   "your-value"
  ;                              :namespace/our-timestamp "2020-04-20T16:20:00.000Z"})
  ;  =>
  ;  {"_id"                     #<ObjectId MyObjectId>
  ;   "namespace/my-keyword"    "*:my-value"
  ;   "namespace/your-string"   "your-value"
  ;   "namespace/our-timestamp" #<DateTime 2020-04-20T16:20:00.123Z>}
  ;
  ; @return (namespaced map)
  [query]
  (if (map/nonempty? query)
      ; 1. A dokumentum :namespace/id tulajdonságának átnevezése :_id tulajdonságra
      ;    A dokumentum string típusú azonosítójának átalakítása objektum típusra
      ; 2. A dokumentumban használt kulcsszó típusú kulcsok és értékek átalakítása string típusra
      ; 3. A dokumentumban string típusként tárolt dátumok és idők átalakítása objektum típusra
      (try (-> query engine/id->_id json/unkeywordize-keys json/unkeywordize-values time/parse-date-time)
           (catch Exception e (println (str e "\n" {:query query}))))
      ; A query paraméterként lehetséges üres térképet is átadni.
      (return query)))