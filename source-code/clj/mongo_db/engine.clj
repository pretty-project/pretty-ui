
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mongo-db.engine
    (:import  org.bson.types.ObjectId)
    (:require [mid-fruits.candy   :refer [return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :as map]
              [monger.conversion  :as mcv]
              [mongo-db.errors    :as errors]
              [x.server-db.api    :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn operator?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (*) n
  ;
  ; @example
  ;  (engine/operator? :$or)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (and (keyword? n)
       (->       n second str (= "$"))))

(defn document?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (*) n
  ;
  ; @example
  ;  (engine/document? {:namespace/my-key "..."})
  ;  =>
  ;  false
  ;
  ; @example
  ;  (engine/document? {:namespace/my-key "..."
  ;                     :namespace/id     "..."})
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (and (-> n map?)
       (boolean (if-let [namespace (db/document->namespace n)]
                        (get n (keyword/add-namespace namespace :id))))))

(defn DBObject->edn
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DBObject) n
  ;
  ; @return (map)
  [n]
  (try (mcv/from-db-object n true)
       (catch Exception e (println e))))



;; -- Document ID -------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn generate-id
  ; @usage
  ;  (mongo-db/generate-id)
  ;
  ; @return (string)
  []
  (str (ObjectId.)))

(defn assoc-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) n
  ;
  ; @example
  ;  (engine/assoc-id {:namespace/my-key "my-value"})
  ;  =>
  ;  {:namespace/id "MyObjectId" :namespace/my-key "my-value"}
  ;
  ; @return (map)
  [n]
  (if-let [namespace (db/document->namespace n)]
          (let [document-id (generate-id)]
               (assoc n (keyword/add-namespace namespace :id) document-id))
          (return n)))

(defn dissoc-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) n
  ;  {:namespace/id (string)(opt)}
  ;
  ; @example
  ;  (engine/dissoc-id {:namespace/id "MyObjectId" :namespace/my-key "my-value"})
  ;  =>
  ;  {:namespace/my-key "my-value"}
  ;
  ; @return (map)
  [n]
  (if-let [namespace (db/document->namespace n)]
          (dissoc n (keyword/add-namespace namespace :id))
          (return n)))

(defn id->_id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) n
  ;  {:namespace/id (string)(opt)}
  ;
  ; @example
  ;  (engine/id->_id {:namespace/id "MyObjectId"})
  ;  =>
  ;  {:_id #<ObjectId MyObjectId>}
  ;
  ; @return (map)
  ;  {:_id (org.bson.types.ObjectId object)}
  [n]
  ; A paraméterként átadott térkép NEM szükséges, hogy rendelkezzen {:namespace/id "..."}
  ; tulajdonsággal (pl.: query paraméterként átadott térképek, ...)
  (if-let [namespace (db/document->namespace n)]
          (let [id-key (keyword/add-namespace namespace :id)]
               (if-let [document-id (get n id-key)]
                       (let [object-id (ObjectId. document-id)]
                            (-> n (assoc  :_id object-id)
                                  (dissoc id-key)))
                       (return n)))
          (return n)))

(defn _id->id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) n
  ;  {:_id (string)}
  ;
  ; @example
  ;  (engine/_id->id {:_id #<ObjectId MyObjectId>})
  ;  =>
  ;  {:namespace/id "MyObjectId"}
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  [n]
  ; A paraméterként átadott térkép NEM szükséges, hogy rendelkezzen {:_id "..."}
  ; tulajdonsággal (hasonlóan az id->_id függvényhez)
  (if-let [namespace (db/document->namespace n)]
          (let [id-key (keyword/add-namespace namespace :id)]
               (if-let [object-id (get n :_id)]
                       (let [document-id (str object-id)]
                            (-> n (assoc  id-key document-id)
                                  (dissoc :_id)))
                       (return n)))
          (return n)))

(defn id->>_id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (*) n
  ;  {:namespace/id (string)(opt)}
  ;
  ; @example
  ;  (engine/id->>_id {:$or [{...} {:namespace/id "MyObjectId"}]})
  ;  =>
  ;  {:$or [{...} {:_id #<ObjectId MyObjectId>}]}
  ;
  ; @return (map)
  ;  {:_id (org.bson.types.ObjectId object)}
  [n]
  (cond (map?    n) (reduce-kv #(assoc %1 %2 (id->>_id %3)) {} (id->_id n))
        (vector? n) (reduce    #(conj  %1    (id->>_id %2)) []          n)
        :else    n))



;; -- Document order ----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn document->order
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) document
  ;
  ; @example
  ;  (engine/document->order {:namespace/order 3})
  ;  =>
  ;  3
  ;
  ; @return (integer)
  [document]
  (if-let [namespace (db/document->namespace document)]
          (get document (keyword/add-namespace namespace :order))
          (throw (Exception. errors/MISSING-NAMESPACE-ERROR))))
