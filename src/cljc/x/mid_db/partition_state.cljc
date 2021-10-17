
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.22
; Description:
; Version: v0.2.2
; Compatibility: x4.2.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-db.partition-state
    (:require [mid-fruits.candy            :refer [param return]]
              [x.mid-core.api              :refer [r]]
              [x.mid-db.data-range-handler :as data-range-handler]
              [x.mid-db.partition-handler  :as partition-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.data-range-handler
(def partition-ranged?       data-range-handler/partition-ranged?)
(def get-in-range-data-order data-range-handler/get-in-range-data-order)

; x.mid-db.partition-handler
(def partition->data-order partition-handler/partition->data-order)
(def get-partition         partition-handler/get-partition)
(def partition-empty?      partition-handler/partition-empty?)



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn partition-state->data-order
  ; @param (map) partition-state
  ;  {:data-order (vector)(opt)}
  ; 
  ; @return (vector)
  [partition-state]
  (partition->data-order partition-state))

(defn partition-state->in-range-data-order
  ; @param (map) partition-state
  ;  {:in-range-data-order (vector)(opt)}
  ;
  ; @return (vector)
  [{:keys [in-range-data-order]}]
  (if (vector? in-range-data-order)
      (return  in-range-data-order)
      (return  [])))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-partition-state
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (r db/get-partition-state ::my-partition)
  ;
  ; @return (map)
  ;  {:in-range-data-order (vector)
  ;   :partition-empty? (boolean)}
  [db [_ partition-id]]
  (let [partition         (r get-partition     db partition-id)
        partition-empty?  (r partition-empty?  db partition-id)
        partition-ranged? (r partition-ranged? db partition-id)]
       (cond-> (param partition)
               (boolean partition-ranged?)
               (assoc :in-range-data-order (r get-in-range-data-order db partition-id))
               :always (assoc :partition-empty? partition-empty?))))
