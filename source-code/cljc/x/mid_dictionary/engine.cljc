
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.17
; Description:
; Version: v0.7.0
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.engine
    (:require [mid-fruits.candy :refer [param]]
              [mid-fruits.map   :as map]
              [x.mid-core.api   :refer [r]]
              [x.mid-db.api     :as db]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-term
  ; @param (keyword) term-id
  ;
  ; @usage
  ;  (r dictionary/get-term db :save!)
  ;
  ; @return (map)
  [db [_ term-id]]
  (get-in db (db/path :dictionary/terms term-id)))

(defn term-exists?
  ; @param (keyword) term-id
  ;
  ; @usage
  ;  (r dictionary/term-exists? db :save!)
  ;
  ; @return (boolean)
  [db [_ term-id]]
  (map/contains-key? (db/path :dictionary/terms)
                     (param term-id)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-term!
  ; @param (keyword) term-id
  ; @param (map) term
  ;
  ; @usage
  ;  (r dictionary/add-term! db :my-term {:en "My term"})
  ;
  ; @return (map)
  [db [_ term-id term]]
  (assoc-in db (db/path :dictionary/terms term-id) term))

(defn add-terms!
  ; @param (map) terms
  ;
  ; @usage
  ;  (r dictionary/add-terms! db {:my-term {:en "My term"}})
  ;
  ; @return (map)
  [db [_ terms]]
  (update-in db (db/path :dictionary/terms) merge terms))
