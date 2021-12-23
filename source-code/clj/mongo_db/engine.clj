
(ns mongo-db.engine
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.random  :as random]
              [monger.conversion  :as mcv]
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
  (mcv/from-db-object n true))

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
  (let [namespace   (db/document->namespace document)
        id-key      (keyword/add-namespace  namespace :id)
        document-id (get                    document  id-key)]
       (-> document (assoc  :_id document-id)
                    (dissoc id-key))))

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
  (let [namespace   (db/document->namespace document)
        id-key      (keyword/add-namespace  namespace :id)
        document-id (get                    document  :_id)]
       (-> document (assoc  id-key document-id)
                    (dissoc :_id))))

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
  (let [namespace (db/document->namespace document)
        id-key    (keyword/add-namespace  namespace :id)]
       (if-let [document-id (get document id-key)]
               (return document)
               (let [document-id (random/generate-string)]
                    (assoc document id-key document-id)))))
