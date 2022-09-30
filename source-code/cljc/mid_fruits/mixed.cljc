
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



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

(defn str-integer?
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/str-integer? "abCd12")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (mixed/str-integer? "12")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (mixed/str-integer? "-12")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (re-match? n #"^-[\d]{1,}|[\d]{1,}$"))

(defn str-natural-integer?
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/str-natural-integer? "abCd12")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (mixed/str-natural-integer? "12")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (mixed/str-natural-integer? "-12")
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n]
  (re-match? n #"^[0-9]{1,}$"))

(defn str-positive-integer?
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/str-positive-integer? "abCd12")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (mixed/str-positive-integer? "12")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (mixed/str-positive-integer? "0")
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n])

(defn str-negative-integer?
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/str-negative-integer? "abCd12")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (mixed/str-negative-integer? "12")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (mixed/str-negative-integer? "-12")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (re-match? n #"^-[0-9]{1,}$"))

(defn update-str-integer
  ; @param (string) n
  ; @param (function) f
  ; @param (*)(opt) x
  ;
  ; @example
  ;  (mixed/update-str-integer "12" inc)
  ;  =>
  ;  "13"
  ;
  ; @example
  ;  (mixed/update-str-integer "12" + 3)
  ;  =>
  ;  "15"
  ;
  ; @example
  ;  (mixed/update-str-integer "abCd12" + 3)
  ;  =>
  ;  "abCd12"
  ;
  ; @return (string)
  ([n f]
   (update-str-integer n f nil))

  ([n f x]
   (letfn [(update-f [n] (let [integer (string/to-integer n)]
                              (if x (f integer x)
                                    (f integer))))]
          (if (-> n str-integer?)
              (-> n update-f str)
              (-> n return)))))

(defn str-contains-integer?
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/str-contains-integer? "abCd12")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (mixed/str-contains-integer? "abCd")
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n]
  (re-match? n #""))
