
(ns prototypes.engine
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.time   :as time]
              [x.server-user.api :as user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn updated-document-prototype
  ; @param (map) request
  ; @param (keyword) document-namespace
  ; @param (namespaced map) updated-item
  ;
  ; @usage
  ;  (prototypes/updated-document-prototype {} :my-namespace {...})
  ;
  ; @return (namespaced map)
  ;  {:namespace/added-at (object)
  ;   :namespace/added-by (map)
  ;   :namespace/modified-at (object)
  ;   :namespace/modified-by (map)}
  [request document-namespace updated-item]
  (let [namespace (name document-namespace)
        timestamp (time/timestamp-object)
        user-link (user/request->user-link request)]
       (merge {(keyword namespace "added-at") timestamp
               (keyword namespace "added-by") user-link}
              (param updated-item)
              {(keyword namespace "modified-at") timestamp
               (keyword namespace "modified-by") user-link})))

(defn duplicated-document-prototype
  ; @param (map) request
  ; @param (keyword) document-namespace
  ; @param (namespaced map) duplicated-item
  ;  {:namespace/id (string)}
  ;
  ; @usage
  ;  (prototypes/updated-document-prototype {} :my-namespace {...})
  ;
  ; @return (namespaced map)
  ;  {:namespace/added-at (object)
  ;   :namespace/added-by (map)
  ;   :namespace/modified-at (object)
  ;   :namespace/modified-by (map)}
  [request document-namespace duplicated-item]
  (let [namespace (name document-namespace)
        timestamp (time/timestamp-object)
        user-link (user/request->user-link request)]
       (merge (dissoc duplicated-item (keyword namespace "id"))
              {(keyword namespace "added-at")    timestamp
               (keyword namespace "added-by")    user-link
               (keyword namespace "modified-at") timestamp
               (keyword namespace "modified-by") user-link})))
