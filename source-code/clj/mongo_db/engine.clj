
(ns mongo-db.engine
    (:import  org.bson.types.ObjectId)
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [monger.conversion  :as mcv]
              [mongo-db.errors    :as errors]
              [x.server-db.api    :as db]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DEFAULT-LOCALE "hu")



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

(defn dissoc-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) document
  ;  {:namespace/id (string)(opt)}
  ;
  ; @example
  ;  (engine/id->_id {:namespace/id "MyObjectId" :namespace/my-key "my-value"})
  ;  =>
  ;  {:namespace/my-key "my-value"}
  ;
  ; @return (namespaced map)
  [document]
  (if-let [namespace (db/document->namespace document)]
          (let [id-key (keyword/add-namespace namespace :id)]
               (dissoc document id-key))
          (throw (Exception. errors/MISSING-NAMESPACE-ERROR))))

(defn id->_id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) document
  ;  {:namespace/id (string)(opt)}
  ;
  ; @example
  ;  (engine/id->_id {:namespace/id "MyObjectId" :namespace/my-key "my-value"})
  ;  =>
  ;  {:_id #<ObjectId MyObjectId> :namespace/my-key "my-value"}
  ;
  ; @return (namespaced map)
  ;  {:_id (org.bson.types.ObjectId object)}
  [document]
  (if-let [namespace (db/document->namespace document)]
          (let [id-key (keyword/add-namespace namespace :id)]
               ; A paraméterként átadott dokumentum NEM szükséges, hogy rendelkezzen {:namespace/id ...}
               ; tulajdonsággal (pl.: conditions paraméterként átadott térképek, ...)
               (if-let [document-id (get document id-key)]
                       (let [object-id (ObjectId. document-id)]
                            (-> document (assoc  :_id object-id)
                                         (dissoc id-key)))
                       (return document)))
          (throw (Exception. errors/MISSING-NAMESPACE-ERROR))))

(defn _id->id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) document
  ;  {:_id (string)}
  ;
  ; @example
  ;  (engine/_id->id {:_id #<ObjectId MyObjectId> :namespace/key :my-value})
  ;  =>
  ;  {:namespace/id "MyObjectId" :namespace/my-key "my-value"}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  [document]
  (if-let [namespace (db/document->namespace document)]
          (let [id-key      (keyword/add-namespace namespace :id)
                object-id   (get document :_id)
                document-id (str object-id)]
               (-> document (assoc  id-key document-id)
                            (dissoc :_id)))
          (throw (Exception. errors/MISSING-NAMESPACE-ERROR))))



;; -- Document order ----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn document->order
  ; @param (namespaced map) document
  ;
  ; @return (integer)
  [document]
  (if-let [namespace (db/document->namespace document)]
          (get document (keyword/add-namespace namespace :order))
          (throw (Exception. errors/MISSING-NAMESPACE-ERROR))))
