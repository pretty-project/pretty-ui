
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.mixed
    (:require [mid-fruits.candy  :refer [return]]
              [mid-fruits.regex  :refer [re-match?]]
              [mid-fruits.string :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn nonempty?
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/nonempty? nil)
  ;  =>
  ;  false
  ;
  ; @example
  ;  (mixed/nonempty? "")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (mixed/nonempty? [])
  ;  =>
  ;  false
  ;
  ; @example
  ;  (mixed/nonempty? {})
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n]
  ; Az empty? függvényt csak a seqable értékeken lehetséges alkalmazni!
  ;
  ; A) Ha az n értéke nem seqable, akkor igaz rá, hogy nonempty (keyword, integer, ...)
  ;
  ; B) Ha az n értéke seqable, akkor megvizsgálja, hogy üres-e (nil, map, string, vector, ...)
  (or ; A)
      (-> n seqable? not)
      ; B)
      (-> n empty?   not)))

(defn blank?
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/blank? nil)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (mixed/blank? "")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (mixed/blank? [])
  ;  =>
  ;  true
  ;
  ; @example
  ;  (mixed/=blank? {})
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  ; Az empty? függvényt csak a seqable értékeken lehetséges alkalmazni!
  (and (-> n seqable?)
       (-> n   empty?)))

(defn whole-number?
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/whole-number? "12")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (mixed/whole-number? "-12")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (mixed/whole-number? 12)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (or (integer? n)
      (re-match? n #"^-[\d]{1,}|[\d]{1,}$")))

(defn natural-whole-number?
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/natural-whole-number? "12")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (mixed/natural-whole-number? "-12")
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n]
  (or (and (integer? n)
           (<= 0     n))
      (re-match? n #"^[0-9]{1,}$")))

(defn positive-whole-number?
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/positive-whole-number? "12")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (mixed/positive-whole-number? "0")
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n]
  (or (and (integer?  n)
           (<    0    n))
      (and (not= 0    n)
           (re-match? n #"^[0-9]{1,}$"))))

(defn negative-whole-number?
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/negative-whole-number? "12")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (mixed/negative-whole-number? "-12")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (mixed/negative-whole-number? -12)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (or (and (integer? n)
           (> 0      n))
      (re-match? n #"^-[0-9]{1,}$")))



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
  (cond (map?    n) (reduce-kv #(conj %1 %3) [] n)
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
  (cond (vector? n) (reduce-kv #(assoc %1 (keyword (str %2)) %3) {} n)
        (map?    n) (return        n)
        (nil?    n) (return        {})
        :else       (return        {0 n})))

(defn to-number
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/to-number 3)
  ;  =>
  ;  3
  ;
  ; @example
  ;  (mixed/to-number nil)
  ;  =>
  ;  0
  ;
  ; @example
  ;  (mixed/to-number "-3")
  ;  =>
  ;  -3
  ;
  ; @example
  ;  (mixed/to-number "1.1")
  ;  =>
  ;  TODO
  ;
  ; @return (number)
  [n]
  (cond (nil?          n) (return            0)
        (number?       n) (return            n)
        (whole-number? n) (string/to-integer n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-whole-number
  ; @param (integer or string) n
  ; @param (function) f
  ; @param (*)(opt) x
  ;
  ; @example
  ;  (mixed/update-whole-number "12" inc)
  ;  =>
  ;  "13"
  ;
  ; @example
  ;  (mixed/update-whole-number "12" + 3)
  ;  =>
  ;  "15"
  ;
  ; @example
  ;  (mixed/update-whole-number 12 + 3)
  ;  =>
  ;  15
  ;
  ; @example
  ;  (mixed/update-whole-number "abCd12" + 3)
  ;  =>
  ;  "abCd12"
  ;
  ; @return (integer or string)
  ([n f]
   (update-whole-number n f nil))

  ([n f x]
   (letfn [(update-f [n] (if x (f n x)
                               (f n)))]
          (cond (-> n      integer?)      (update-f n)
                (-> n whole-number?) (let [integer (string/to-integer n)]
                                          (update-f integer))
                (-> n         some?)      (return n)))))

(defn parse-whole-number
  ; @param (*) n
  ;
  ; @example
  ;  (mixed/parse-whole-number "12")
  ;  =>
  ;  12
  ;
  ; @example
  ;  (mixed/parse-whole-number 12)
  ;  =>
  ;  12
  ;
  ; @example
  ;  (mixed/update-whole-number "abCd12")
  ;  =>
  ;  "abCd12"
  ;
  ; @return (*)
  [n]
  (cond (integer?      n) (return            n)
        (whole-number? n) (string/to-integer n)
        (some?         n) (return            n)))
