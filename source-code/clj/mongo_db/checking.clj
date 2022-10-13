
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
  ; Az update művelet számára névtéres dokumentumot vagy utasításokat
  ; tartalmazó névtér nélküli térképet is lehetséges átadni.
  ; Pl.: {:$inc {:namespace/my-integer 1}}
  (try (if (map?   document)
           (return document)
           (throw (Exception. errors/INPUT-MUST-BE-MAP-ERROR)))
       (catch Exception e (println (str e "\n" {:document document})))))

(defn update-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (*) query
  ;
  ; @return (*)
  [query]
  (find-query query))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn upsert-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (*) document
  ;
  ; @return (*)
  [document]
  (update-input document))



;; -- Aggregation -------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn search-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (*) query
  ;
  ; @return (*)
  [query]
  (find-query query))
