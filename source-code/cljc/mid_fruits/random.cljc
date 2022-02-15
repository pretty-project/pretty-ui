
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.11
; Description:
; Version: v0.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.random
    (:require [mid-fruits.candy :refer [param]]
              [mid-fruits.math  :as math]))



;; -- Untyped generators ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn generate-boolean
  ; @usage
  ;  (random/generate-boolean)
  ;
  ; @param (boolean)
  []
  (-> 2 rand-int zero?))



;; -- Typed generators --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn generate-uuid
  ; @usage
  ;  (random/generate-uuid)
  ;
  ; @return (string)
  []
  #?(:cljs (str (random-uuid))
     :clj  (str (java.util.UUID/randomUUID))))

(defn generate-string
  ; @usage
  ;  (random/generate-string)
  ;
  ; @return (string)
  []
  (generate-uuid))

(defn generate-keyword
  ; @param (string)(opt) namespace
  ;
  ; @usage
  ;  (random/generate-keyword)
  ;
  ; @return (keyword)
  ([]
   (keyword (generate-uuid)))

  ([namespace]
   (keyword (str namespace "/" (generate-uuid)))))

(defn generate-namespaced-keyword
  ; @return (namespaced keyword)
  []
  (keyword (str (generate-uuid) "/" (generate-uuid))))

(defn generate-react-key
  ; @usage
  ;  (random/generate-react-key)
  ;
  ; @return (string)
  []
  (generate-uuid))

(defn generate-number
  ; @param (integer) digits
  ;
  ; @example
  ;  (random/generate-number 3)
  ;  =>
  ;  420
  ;
  ; @return (integer)
  [digits]
  ; - A (-> 9 rand inc) függvény kimenete egy lebegőpontos érték 1 és 10 között
  ; - A (generate-number n) fügvény visszatérési értéke egy 1 és 9.99 között érték szorozva
  ;   10 (n - 1) hatványával, integer típusra alakítva.
  (int (* (math/power 10 (dec digits)) (min 9.999 (-> 9 rand inc)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn pick-vector-item
  ; @param (vector) n
  ;
  ; @usage
  ;  (random/pick-vector-item [:a :b :c]
  ;  =>
  ;  :a
  ;
  ; @usage
  ;  (random/pick-vector-item [:a :b :c]
  ;  =>
  ;  :c
  ;
  ; @return (*)
  [n]
  (nth n (-> n count rand-int)))
