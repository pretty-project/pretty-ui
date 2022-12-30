
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.items.subs
    (:require [candy.api                        :refer [return]]
              [engines.engine-handler.body.subs :as body.subs]
              [engines.engine-handler.core.subs :as core.subs]
              [loop.api                         :refer [some-indexed]]
              [re-frame.api                     :refer [r]]
              [vector.api                       :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (integer)
  [db [_ engine-id item-id]]
  ; Takes an item id and returns with the item's position in the item list.
  (letfn [(f [item-dex {:keys [id]}] (if (= id item-id) item-dex))]
         (let [item-order (r core.subs/get-item-order db engine-id)]
              (some-indexed f item-order))))

(defn get-item-dexes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (strings in vector) item-ids
  ;
  ; @return (integers in vector)
  [db [_ engine-id item-ids]]
  ; Takes a vector of item id-s and returns with the items' positions in the item list.
  ;
  ; In the iteration ...
  ; (A) ... when the index is higher the last item's index, the iteration is over.
  ; (B) ... when the item's id is in the item-ids vector, the function conjugates
  ;         the current index to its result vector and removes the id from the item-ids vector.
  ; (C) ... when the item's id is NOT in the item-ids vector, the function skips to
  ;         the next lap.
  (let [item-order (r core.subs/get-item-order db engine-id)]
       (letfn [(f [item-dexes item-ids dex]
                  (let [item-id (get-in item-order [dex :id])]
                       (cond
                             ; (A)
                             (= dex (count item-order))
                             (return item-dexes)

                             ; (B)
                             (vector/contains-item? item-ids item-id)
                             (f (conj item-dexes dex)
                                (vector/remove-item item-ids item-id)
                                (inc dex))

                             ; (C)
                             :return (f item-dexes item-ids (inc dex)))))]

              (f [] item-ids 0))))



;; -- Single item events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (boolean)
  [db [_ engine-id item-id]]
  (let [disabled-items (r core.subs/get-meta-item db engine-id :disabled-items)]
       (vector/contains-item? disabled-items item-id)))

(defn item-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (boolean)
  [db [_ engine-id item-id]]
  ; XXX#6487
  (if-let [items-path (r body.subs/get-body-prop db engine-id :items-path)]
          (get-in db (conj items-path item-id :meta-items :changed?))))



;; -- Current item subscriptions ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn current-item-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (let [current-item-id (r core.subs/get-current-item-id db engine-id)]
       (r item-changed? db engine-id current-item-id)))
