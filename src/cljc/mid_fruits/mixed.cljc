
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.04
; Description:
; Version: v0.6.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.mixed
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.loop   :refer [reduce-while]]
              [mid-fruits.map    :as map]
              [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (vector)
(def DIGITS ["0" "1" "2" "3" "4" "5" "6" "7" "8" "9"])



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mixed->string
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/mixed->string [{:a "a"}])
  ;  => "[{:a a}]"
  ;
  ; @return (string)
  [n]
  (str n))

(defn mixed->data-url
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/mixed->data-url "My text file content")
  ;  => "data:text/plain;charset=utf-8,My text file content"
  ;
  ; @return (string)
  [n]
  (str "data:text/plain;charset=utf-8," n))

(defn mixed->vector
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/mixed->vector [:a])
  ;  => [:a]
  ;
  ; @example
  ;  (mixed/mixed->vector nil)
  ;  => []
  ;
  ; @example
  ;  (mixed/mixed->vector {:a "a" :b "b"})
  ;  => ["a" "b"]
  ;
  ; @example
  ;  (mixed/mixed->vector :x)
  ;  => [:x]
  ;
  ; @return (vector)
  [n]
  (cond (vector? n) (return          n)
        (nil? n)    (return          [])
        (map? n)    (map/map->vector n)
        :else       (return          [n])))

(defn mixed->map
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/mixed->map {:a})
  ;  => {:a}
  ;
  ; @example
  ;  (mixed/mixed->map nil)
  ;  => {}
  ;
  ; @example
  ;  (mixed/mixed->map [:x :y :z])
  ;  => {0 :x 1 :y 2 :z}
  ;
  ; @example
  ;  (mixed/mixed->map :x)
  ;  => {0 :x}
  ;
  ; @return (map)
  [n]
  (cond (map?    n) (return             n)
        (nil?    n) (return             {})
        (vector? n) (vector/vector->map n)
        :else       (return             {0 n})))

(defn mixed->number?
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/mixed->number? "abCd12")
  ;  => false
  ;
  ; @example
  ;  (mixed/mixed->number? "12")
  ;  => true
  ;
  ; @return (boolean)
  [n]
  ; BUG#7609
  ;  A Java és JavaScript nyelvek különbségei miatt szükséges %2 helyett (str %2)
  ;  vizsgálata.
  ;  A Java nyelvben az egy elemű string típusa CHAR, nem pedig STRING!
  (boolean (reduce-while (fn [_ %2] (vector/contains-item? DIGITS (str %2)))
                         (param nil)
                         (str   n)
                         (fn [%1 _] (false? %1)))))

(defn mixed->update-number
  ; @param (*) n
  ; @param (function) f
  ; @param (*)(opt) x
  ;
  ; @example
  ;  (mixed/mixed->update-number "12" inc)
  ;  => "13"
  ;
  ; @example
  ;  (mixed/mixed->update-number "12" + 3)
  ;  => "15"
  ;
  ; @example
  ;  (mixed/mixed->update-number "abCd12" + 3)
  ;  => "abCd12"
  ;
  ; @return (*)
  ([n f]
   (mixed->update-number n f nil))

  ([n f x]
   (if (mixed->number? n)
       (let [integer (string/to-integer n)
             result  (if (some? x)
                         (f integer x)
                         (f integer))]
            (str result))
       (return n))))

(defn mixed->contains-number?
  ; @param (*) n
  ;
  ; @usage
  ;  (mixed/mixed->contains-number? "abCd12")
  ;
  ; @return (boolean)
  [n]
  ; BUG#7609
  (reduce-while (fn [_ %2] (vector/contains-item? DIGITS (str %2)))
                (param false)
                (str   n)
                (fn [%1 _] (true? %1))))
