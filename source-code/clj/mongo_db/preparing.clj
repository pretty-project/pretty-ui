
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mongo-db.preparing
    (:require [mid-fruits.candy    :refer [return]]
              [mid-fruits.gestures :as gestures]
              [mid-fruits.keyword  :as keyword]
              [mid-fruits.map      :as map]
              [mongo-db.engine     :as engine]
              [mongo-db.errors     :as errors]
              [mongo-db.reader     :as reader]))



;; -- Inserting document ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- ordered-insert-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ; @param (map) options
  ;
  ; @return (namespaced map)
  ;  {:namespace/order (integer)}
  [collection-name document _]
  (if-let [namespace (map/get-namespace document)]
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
  (try (as-> document % (if-not ordered?    % (ordered-insert-input collection-name % options))
                        (if-not prototype-f % (prototype-f %)))
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

(defn- changed-duplicate-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ; @param (map) options
  ;  {:changes (namespaced map)}
  ;
  ; @return (string)
  [_ document {:keys [changes]}]
  ; Ha a dokumentum kliens-oldali változata esetlegesen el nem mentett változtatásokat tartalmaz,
  ; akkor a változtatások a {:changes ...} tulajdonság értékeként megadhatók és a dokumentumról
  ; készülő másolat tartalmazni fogja őket.
  (merge document changes))

(defn- labeled-duplicate-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ; @param (map) options
  ;  {:label-key (namespaced keyword)}
  ;
  ; @return (string)
  [collection-name document {:keys [label-key]}]
  (let [collection          (reader/get-collection collection-name)
        document-label      (get  document label-key)
        all-document-labels (mapv label-key collection)
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
  [_ document _]
  (if-let [namespace (map/get-namespace document)]
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
  ;  {:changes (namespaced map)(opt)
  ;   :label-key (namespaced keyword)(opt)
  ;   :ordered? (boolean)(opt)
  ;    Default: false
  ;   :prototype-f (function)(opt)}
  ;
  ; @return (namespaced map)
  ;  {:namespace/order (integer)}
  [collection-name document {:keys [changes label-key ordered? prototype-f] :as options}]
  (try (as-> document % (if-not changes   % (changed-duplicate-input collection-name % options))
                        (if-not label-key % (labeled-duplicate-input collection-name % options))
                        (if-not ordered?  % (ordered-duplicate-input collection-name %))
                        ; - A dokumentum a changes térképpel való összefésülés után kapja meg a másolat azonosítóját,
                        ;   így nem okoz hibát, ha a changes térkép tartalmazza az eredeti azonosítót
                        ; - A dokumentum a prototípus függvény alkalmazása előtt megkapja a másolat azonosítóját,
                        ;   így az már elérhető a prototípus függvény számára
                        (engine/assoc-id %)
                        (if-not prototype-f % (prototype-f %)))
       (catch Exception e (println (str e "\n" {:collection-name collection-name :document document :options options})))))
