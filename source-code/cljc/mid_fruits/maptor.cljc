
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.11
; Description:
; Version: v0.3.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.maptor
    (:require [mid-fruits.candy  :refer [param]]
              [mid-fruits.vector :as vector]))



;; -- Maptor ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A map típushoz hasonlóan kulcs-érték párok tárolására alkalmas típus, ami
; a vektor típushoz hasonlóan megtartja a benne tárolt adatok sorrendjét
;
; Pl.:
; [[:key :value] [:key :value] [:key :value]]



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(def empty-maptor [])



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn maptor->map
  ; @param (maptor) n
  ;
  ; @example
  ;  (maptor/to-map [[:foo 1][:bar 2][:baz 3]])
  ;  =>
  ;  {:foo 1 :bar 2 :baz 3}
  ;
  ; @return (map)
  [n]
  (reduce (fn [n [x y]] (assoc n x y))
          (param {})
          (param n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn maptor-item?
  ; @param (*) n
  ;
  ; @return (boolean)
  [n]
  (boolean (and (vector? n)
                (= 2 (count n)))))

(defn nonempty?
  ; @param (*) n
  ;
  ; @usage
  ;  (maptor/nonempty? [])
  ;
  ; @return (boolean)
  [n]
  (boolean (if (vector/nonempty? n)
               (reduce (fn [%1 %2]
                           (and (param %1)
                                (maptor-item? %2)))
                       (param true)
                       (param n)))))

(defn maptor?
  ; @param (*) n
  ;
  ; @return (boolean)
  [n]
  (nonempty? n))
