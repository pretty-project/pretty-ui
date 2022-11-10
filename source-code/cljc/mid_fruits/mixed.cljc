
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
              [mid-fruits.map    :as map]
              [regex.api         :refer [re-match?]]
              [mid-fruits.reader :as reader]
              [mid-fruits.vector :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn nonempty?
  ; @param (*) n
  ;
  ; @example
  ;  (nonempty? nil)
  ;  =>
  ;  false
  ;
  ; @example
  ;  (nonempty? "")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (nonempty? [])
  ;  =>
  ;  false
  ;
  ; @example
  ;  (nonempty? {})
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
  ;  (blank? nil)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (blank? "")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (blank? [])
  ;  =>
  ;  true
  ;
  ; @example
  ;  (=blank? {})
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  ; Az empty? függvényt csak a seqable értékeken lehetséges alkalmazni!
  ; (A nonseqable értékek nem lehetnek üresek! Pl.: :keyword)
  (and (-> n seqable?)
       (-> n   empty?)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn rational-number?
  ; @param (*) n
  ;
  ; @example
  ;  (rational-number? 12)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (rational-number? 12.1)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (rational-number? "12")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (rational-number? "12.1")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (rational-number? "+12.1")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (rational-number? "-12.1")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (or (number?   n)
      (re-match? n #"^[+-]{0,1}[\d]{1,}$")
      (re-match? n #"^[+-]{0,1}[\d]{1,}[\.][\d]{1,}$")))

(defn whole-number?
  ; @param (*) n
  ;
  ; @example
  ;  (whole-number? 12)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (whole-number? "12")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (whole-number? "+12")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (whole-number? "-12")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (or (integer?  n)
      (re-match? n #"^[+-]{0,1}[\d]{1,}$")))

(defn natural-whole-number?
  ; @param (*) n
  ;
  ; @example
  ;  (natural-whole-number? 12)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (natural-whole-number? "12")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (natural-whole-number? "+12")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (natural-whole-number? "-12")
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n]
  (or (and (integer? n)
           (<= 0     n))
      (re-match? n #"^[+]{0,1}[\d]{1,}$")))

(defn positive-whole-number?
  ; @param (*) n
  ;
  ; @example
  ;  (positive-whole-number? 12)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (positive-whole-number? "12")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (positive-whole-number? "+12")
  ;  =>
  ;  true
  ;
  ; @example
  ;  (positive-whole-number? "0")
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n]
  (or (and (integer?  n)
           (<    0    n))
      (and (not= 0    n)
           (re-match? n #"^[+]{0,1}[\d]{1,}$"))))

(defn negative-whole-number?
  ; @param (*) n
  ;
  ; @example
  ;  (negative-whole-number? -12)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (negative-whole-number? "12")
  ;  =>
  ;  false
  ;
  ; @example
  ;  (negative-whole-number? "-12")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (or (and (integer? n)
           (> 0      n))
      (re-match? n #"^[-][0-9]{1,}$")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn to-string
  ; @param (*) n
  ;
  ; @example
  ;  (to-string [{:a "a"}])
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
  ;  (to-data-url "My text file content")
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
  ;  (to-vector [:a])
  ;  =>
  ;  [:a]
  ;
  ; @example
  ;  (to-vector nil)
  ;  =>
  ;  []
  ;
  ; @example
  ;  (to-vector {:a "a" :b "b"})
  ;  =>
  ;  ["a" "b"]
  ;
  ; @example
  ;  (to-vector :x)
  ;  =>
  ;  [:x]
  ;
  ; @return (vector)
  [n]
  (cond (map?    n) (map/to-vector n)
        (vector? n) (return        n)
        (nil?    n) (return        [])
        :return                    [n]))

(defn to-map
  ; @param (*) n
  ;
  ; @example
  ;  (to-map {:a})
  ;  =>
  ;  {:a}
  ;
  ; @example
  ;  (to-map nil)
  ;  =>
  ;  {}
  ;
  ; @example
  ;  (to-map [:x :y :z])
  ;  =>
  ;  {0 :x 1 :y 2 :z}
  ;
  ; @example
  ;  (to-map :x)
  ;  =>
  ;  {0 :x}
  ;
  ; @return (map)
  [n]
  (cond (vector? n) (vector/to-map n)
        (map?    n) (return        n)
        (nil?    n) (return        {})
        :return                    {0 n}))

(defn to-number
  ; @param (*) n
  ;
  ; @example
  ;  (to-number 3)
  ;  =>
  ;  3
  ;
  ; @example
  ;  (to-number nil)
  ;  =>
  ;  0
  ;
  ; @example
  ;  (to-number "a")
  ;  =>
  ;  0
  ;
  ; @example
  ;  (to-number "-3")
  ;  =>
  ;  -3
  ;
  ; @example
  ;  (to-number "1.1")
  ;  =>
  ;  1.1
  ;
  ; @return (number)
  [n]
  (cond (nil?             n) (return               0)
        (number?          n) (return               n)
        (whole-number?    n) (reader/string->mixed n)
        (rational-number? n) (reader/string->mixed n)
        :return 0))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-numbers
  ; @param (list of *) abc
  ;
  ; @example
  ;  (add-numbers 1 "3")
  ;  =>
  ;  4
  ;
  ; @example
  ;  (add-numbers 1 "3" "a")
  ;  =>
  ;  4
  ;
  ; @return (integer)
  [& abc]
  (letfn [(f [result x]
             (+ result (to-number x)))]
         (reduce f 0 abc)))

(defn multiply-numbers
  ; @param (list of *) abc
  ;
  ; @example
  ;  (multiply-numbers 1 "3")
  ;  =>
  ;  3
  ;
  ; @example
  ;  (multiply-numbers 1 "3" "a")
  ;  =>
  ;  0
  ;
  ; @return (integer)
  [& abc]
  (letfn [(f [result x]
             (* result (to-number x)))]
         (reduce f 1 abc)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-whole-number
  ; @param (integer or string) n
  ; @param (function) f
  ; @param (*)(opt) x
  ;
  ; @example
  ;  (update-whole-number "12" inc)
  ;  =>
  ;  "13"
  ;
  ; @example
  ;  (update-whole-number "12" + 3)
  ;  =>
  ;  "15"
  ;
  ; @example
  ;  (update-whole-number 12 + 3)
  ;  =>
  ;  15
  ;
  ; @example
  ;  (update-whole-number "abCd12" + 3)
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
                (-> n whole-number?) (let [integer (reader/string->mixed n)]
                                          (update-f integer))
                (-> n         some?)      (return n)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn parse-rational-number
  ; @param (*) n
  ;
  ; @example
  ;  (parse-rational-number 12)
  ;  =>
  ;  12
  ;
  ; @example
  ;  (parse-rational-number 12.1)
  ;  =>
  ;  12.1
  ;
  ; @example
  ;  (parse-rational-number "12.1")
  ;  =>
  ;  12.1
  ;
  ; @example
  ;  (parse-rational-number "abCd12.1")
  ;  =>
  ;  "abCd12.1"
  ;
  ; @return (*)
  [n]
  (cond (number?          n) (return               n)
        (rational-number? n) (reader/string->mixed n)
        (some?            n) (return               n)))

(defn parse-number
  ; @param (*) n
  ;
  ; @example
  ;  (parse-number 12)
  ;  =>
  ;  12
  ;
  ; @example
  ;  (parse-number 12.1)
  ;  =>
  ;  12.1
  ;
  ; @example
  ;  (parse-number "12.1")
  ;  =>
  ;  12.1
  ;
  ; @example
  ;  (parse-number "abCd12.1")
  ;  =>
  ;  "abCd12.1"
  ;
  ; @return (*)
  [n]
  (parse-rational-number n))

(defn parse-whole-number
  ; @param (*) n
  ;
  ; @example
  ;  (parse-whole-number 12)
  ;  =>
  ;  12
  ;
  ; @example
  ;  (parse-whole-number "12")
  ;  =>
  ;  12
  ;
  ; @example
  ;  (parse-whole-number "abCd12")
  ;  =>
  ;  "abCd12"
  ;
  ; @return (*)
  [n]
  (cond (integer?      n) (return               n)
        (whole-number? n) (reader/string->mixed n)
        (some?         n) (return               n)))
