
(ns mongo-db.checking
    (:require [mid-fruits.candy :refer [param return]]
              [mongo-db.errors  :as errors]
              [x.server-db.api  :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn find-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (*) query
  ;
  ; @return (*)
  [query]
  (try (if-let [namespace (db/document->namespace query)]
               (return query)
               (throw (Exception. errors/MISSING-NAMESPACE-ERROR)))
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
