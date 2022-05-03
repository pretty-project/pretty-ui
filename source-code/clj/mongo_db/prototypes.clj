
(ns mongo-db.prototypes
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.time    :as time]
              [server-fruits.http :as http]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->user-link
  ; @param (map) request
  ;
  ; @return (map)
  ;  {:user-account/id (string)}
  [request]
  (if-let [user-account-id (http/request->session-param request :user-account/id)]
          {:user-account/id user-account-id}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn added-document-prototype
  ; @param (map) request
  ; @param (keyword) document-namespace
  ; @param (namespaced map) updated-item
  ;
  ; @usage
  ;  (mongo-db/added-document-prototype {} :my-namespace {...})
  ;
  ; @return (namespaced map)
  ;  {:namespace/added-at (string)
  ;   :namespace/added-by (map)
  ;   :namespace/modified-at (string)
  ;   :namespace/modified-by (map)}
  [request document-namespace updated-item]
  (let [namespace (name document-namespace)
        timestamp (time/timestamp-string)
        user-link (request->user-link request)]
       (merge (param updated-item)
              {(keyword namespace "added-at")    timestamp
               (keyword namespace "added-by")    user-link
               (keyword namespace "modified-at") timestamp
               (keyword namespace "modified-by") user-link})))

(defn updated-document-prototype
  ; @param (map) request
  ; @param (keyword) document-namespace
  ; @param (namespaced map) updated-item
  ;
  ; @usage
  ;  (mongo-db/updated-document-prototype {} :my-namespace {...})
  ;
  ; @return (namespaced map)
  ;  {:namespace/added-at (string)
  ;   :namespace/added-by (map)
  ;   :namespace/modified-at (string)
  ;   :namespace/modified-by (map)}
  [request document-namespace updated-item]
  (let [namespace (name document-namespace)
        timestamp (time/timestamp-string)
        user-link (request->user-link request)]
       (merge {(keyword namespace "added-at") timestamp
               (keyword namespace "added-by") user-link}
              (param updated-item)
              {(keyword namespace "modified-at") timestamp
               (keyword namespace "modified-by") user-link})))

(defn duplicated-document-prototype
  ; @param (map) request
  ; @param (keyword) document-namespace
  ; @param (namespaced map) duplicated-item
  ;
  ; @usage
  ;  (mongo-db/updated-document-prototype {} :my-namespace {...})
  ;
  ; @return (namespaced map)
  ;  {:namespace/added-at (string)
  ;   :namespace/added-by (map)
  ;   :namespace/modified-at (string)
  ;   :namespace/modified-by (map)}
  [request document-namespace duplicated-item]
  (let [namespace (name document-namespace)
        timestamp (time/timestamp-string)
        user-link (request->user-link request)]
       (merge (param duplicated-item)
              {(keyword namespace "added-at")    timestamp
               (keyword namespace "added-by")    user-link
               (keyword namespace "modified-at") timestamp
               (keyword namespace "modified-by") user-link})))
