
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mongo-db.checking
    (:require [mid-fruits.candy :refer [return]]
              [mid-fruits.map :as map]
              [mongo-db.errors  :as errors]))



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
  (try (if-let [namespace (map/get-namespace document)]
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
  (try (if-let [namespace (map/get-namespace document)]
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
  (try (if-let [namespace (map/get-namespace document)]
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
  (try (if-let [namespace (map/get-namespace document)]
               (return document)
               (throw (Exception. errors/MISSING-NAMESPACE-ERROR)))
       (catch Exception e (println (str e "\n" {:document document})))))
