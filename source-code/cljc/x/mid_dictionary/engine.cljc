
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.17
; Description:
; Version: v0.7.8
; Compatibility: x4.6.1



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
  ; @param (keyword)(opt) language-id
  ;
  ; @example
  ;  (r dictionary/get-term db :my-term)
  ;  =>
  ;  {:en "My term"}
  ;
  ; @example
  ;  (r dictionary/get-term db :my-term :en)
  ;  =>
  ;  "My term"
  ;
  ; @return (map or string)
  [db [_ term-id language-id]]
  (if language-id (get-in db (db/path :dictionary/terms term-id language-id))
                  (get-in db (db/path :dictionary/terms term-id))))

(defn term-exists?
  ; @param (keyword) term-id
  ; @param (keyword)(opt) language-id
  ;
  ; @usage
  ;  (r dictionary/term-exists? db :my-term)
  ;
  ; @usage
  ;  (r dictionary/term-exists? db :my-term :en)
  ;
  ; @return (boolean)
  [db [_ term-id language-id]]
  (if language-id (map/contains-key? (db/path :dictionary/terms term-id) language-id)
                  (map/contains-key? (db/path :dictionary/terms)             term-id)))



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
