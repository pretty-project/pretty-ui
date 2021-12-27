
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.04
; Description:
; Version: v0.8.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.mixed
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map]
              [mid-fruits.regex  :refer [re-match?]]
              [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn to-string
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/to-string [{:a "a"}])
  ;  =>
  ;  "[{:a a}]"
  ;
  ; @return (string)
  [n]
  (str n))

(defn to-data-url
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/to-data-url "My text file content")
  ;  =>
  ;  "data:text/plain;charset=utf-8,My text file content"
  ;
  ; @return (string)
  [n]
  (str "data:text/plain;charset=utf-8," n))

(defn to-vector
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/to-vector [:a])
  ;  =>
  ;  [:a]
  ;
  ; @example
  ;  (mixed/to-vector nil)
  ;  =>
  ;  []
  ;
  ; @example
  ;  (mixed/to-vector {:a "a" :b "b"})
  ;  =>
  ;  ["a" "b"]
  ;
  ; @example
  ;  (mixed/to-vector :x)
  ;  =>
  ;  [:x]
  ;
  ; @return (vector)
  [n]
  (cond (map?    n) (map/to-vector n)
        (vector? n) (return        n)
        (nil?    n) (return        [])
        :else       (return        [n])))

(defn to-map
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/to-map {:a})
  ;  =>
  ;  {:a}
  ;
  ; @example
  ;  (mixed/to-map nil)
  ;  =>
  ;  {}
  ;
  ; @example
  ;  (mixed/to-map [:x :y :z])
  ;  =>
  ;  {0 :x 1 :y 2 :z}
  ;
  ; @example
  ;  (mixed/to-map :x)
  ;  =>
  ;  {0 :x}
  ;
  ; @return (map)
  [n]
  (cond (vector? n) (vector/to-map n)
        (map?    n) (return        n)
        (nil?    n) (return        {})
        :else       (return        {0 n})))

(defn str-number?
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/str-number? "abCd12")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (mixed/str-number? "12")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (re-match? n #"^[0-9]*$"))

(defn update-str-number
  ; @param (*) n
  ; @param (function) f
  ; @param (*)(opt) x
  ;
  ; @example
  ;  (mixed/update-str-number "12" inc)
  ;  =>
  ;  "13"
  ;
  ; @example
  ;  (mixed/update-str-number "12" + 3)
  ;  =>
  ;  "15"
  ;
  ; @example
  ;  (mixed/update-str-number "abCd12" + 3)
  ;  =>
  ;  "abCd12"
  ;
  ; @return (*)
  ([n f]
   (update-str-number n f nil))

  ([n f x]
   (if (str-number? n)
       (let [integer (string/to-integer n)
             result  (if (some? x)
                         (f integer x)
                         (f integer))]
            (str result))
       (return n))))

(defn str-contains-number?
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/str-contains-number? "abCd12")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (re-match? n #""))
