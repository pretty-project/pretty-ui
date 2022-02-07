
(ns mongo-db.preparing
    (:require [mid-fruits.candy    :refer [param return]]
              [mid-fruits.gestures :as gestures]
              [mid-fruits.keyword  :as keyword]
              [mongo-db.errors     :as errors]
              [mongo-db.reader     :as reader]
              [x.server-db.api     :as db]))



;; -- Inserting document ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- ordered-insert-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ;
  ; @return (namespaced map)
  ;  {:namespace/order (integer)}
  [collection-name document]
  (if-let [namespace (db/document->namespace document)]
          (let [order-key  (keyword/add-namespace         namespace :order)
                last-order (reader/get-all-document-count collection-name)]
               (assoc document order-key last-order))
          (throw (Exception. errors/MISSING-NAMESPACE-ERROR))))

(defn insert-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ; @param (map) options
  ;  {:ordered? (boolean)(opt)
  ;    Default: false
  ;   :prototype-f (function)(opt)}
  ;
  ; @return (namespaced map)
  ;  {:namespace/order (integer)}
  [collection-name document {:keys [ordered? prototype-f] :as options}]
  (try (cond->> document ordered?    (ordered-insert-input collection-name)
                         prototype-f (prototype-f))
       (catch Exception e (println (str e "\n" {:collection-name collection-name :document document :options options})))))



;; -- Saving document ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ; @param (map) options
  ;  {:ordered? (boolean)(opt)
  ;    Default: false
  ;   :prototype-f (function)(opt)}
  ;
  ; @return (namespaced map)
  ;  {:namespace/order (integer)}
  [collection-name document options]
  (insert-input collection-name document options))



;; -- Updating document -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ; @param (map) options
  ;  {:prototype-f (function)(opt)}
  ;
  ; @return (namespaced map)
  [collection-name document {:keys [prototype-f] :as options}]
  (try (if prototype-f (prototype-f document)
                       (return      document))
       (catch Exception e (println (str e "\n" {:collection-name collection-name :document document :options options})))))



;; -- Upserting document ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn upsert-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ; @param (map) options
  ;  {:prototype-f (function)(opt)}
  ;
  ; @return (namespaced map)
  ;  {:namespace/order (integer)}
  [collection-name document options]
  (update-input collection-name document options))



;; -- Applying document -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ; @param (map) options
  ;  {:prototype-f (function)(opt)}
  ;
  ; @return (namespaced map)
  ;  {:namespace/order (integer)}
  [collection-name document options]
  (update-input collection-name document options))



;; -- Duplicating document ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- labeled-duplicate-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ; @param (map) options
  ;  {:label-key (namespaced keyword)}
  ; @param (namespaced map) document
  ;  A cond->> függvény utolsó paraméterként átadja a document paramétert ...
  ;
  ; @return (string)
  [collection-name document {:keys [label-key]} _]
  (let [document-id         (db/document->document-id  document)
        document            (reader/get-document-by-id collection-name document-id)
        all-documents       (reader/get-all-documents  collection-name)
        document-label      (get  document label-key)
        all-document-labels (mapv label-key all-documents)
        copy-label (gestures/item-label->copy-label document-label all-document-labels)]
       (assoc document label-key copy-label)))

(defn- ordered-duplicate-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ;
  ; @return (namespaced map)
  ;  {:namespace/order (integer)}
  [_ document]
  (if-let [namespace (db/document->namespace document)]
          (let [order-key (keyword/add-namespace namespace :order)]
               (if-let [order (get document order-key)]
                       (update document order-key inc)
                       (throw (Exception. errors/MISSING-DOCUMENT-ORDER-ERROR))))
          (throw (Exception. errors/MISSING-NAMESPACE-ERROR))))

(defn duplicate-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ; @param (map) options
  ;  {:label-key (namespaced keyword)(opt)
  ;   :ordered? (boolean)(opt)
  ;    Default: false
  ;   :prototype-f (function)(opt)}
  ;
  ; @return (namespaced map)
  ;  {:namespace/order (integer)}
  [collection-name document {:keys [label-key ordered? prototype-f] :as options}]
  (try (cond->> document label-key   (labeled-duplicate-input collection-name document options)
                         ordered?    (ordered-duplicate-input collection-name)
                         prototype-f (prototype-f))
       (catch Exception e (println (str e "\n" {:collection-name collection-name :document document :options options})))))
