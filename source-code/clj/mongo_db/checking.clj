
(ns mongo-db.checking
    (:require [mid-fruits.candy :refer [return]]
              [mongo-db.errors  :as errors]
              [x.server-db.api  :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn find-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (*) query
  ;
  ; @usage
  ;  (checking/find-query {:namespace/my-string "my-value"
  ;                        :$or [{:namespace/id "MyObjectId"}]})
  ;
  ; @return (*)
  [query]
  (try (if (map?   query)
           (return query)
           (throw (Exception. errors/QUERY-MUST-BE-MAP-ERROR)))
       (catch Exception e (println (str e "\n" {:query query})))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn insert-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (*) document
  ;
  ; @return (*)
  [document]
  (try (if-let [namespace (db/document->namespace document)]
               (return document)
               (throw (Exception. errors/MISSING-NAMESPACE-ERROR)))
       (catch Exception e (println (str e "\n" {:document document})))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (*) document
  ;
  ; @return (*)
  [document]
  (try (if-let [namespace (db/document->namespace document)]
               (return document)
               (throw (Exception. errors/MISSING-NAMESPACE-ERROR)))
       (catch Exception e (println (str e "\n" {:document document})))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (*) document
  ;
  ; @return (*)
  [document]
  (try (if-let [namespace (db/document->namespace document)]
               (return document)
               (throw (Exception. errors/MISSING-NAMESPACE-ERROR)))
       (catch Exception e (println (str e "\n" {:document document})))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn upsert-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (*) document
  ;
  ; @return (*)
  [document]
  (try (if-let [namespace (db/document->namespace document)]
               (return document)
               (throw (Exception. errors/MISSING-NAMESPACE-ERROR)))
       (catch Exception e (println (str e "\n" {:document document})))))
