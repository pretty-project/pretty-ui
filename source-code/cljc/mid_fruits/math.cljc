
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.10
; Description:
; Version: v0.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.math
    (:require [mid-fruits.candy :refer [param return]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (float)
(def pi Math/PI)

(defn percent->angle
  ; @param (float or integer) n
  ;
  ; @example
  ;  (math/percent->angle 50)
  ;  => 
  ;  180
  ;
  ; @return (float or integer)
  [n]
  (* 360 (/ n 100)))

(defn floor
  ; @param (float or integer) n
  ;
  ; @example
  ;  (math/floor 4.20)
  ;  =>
  ;  4
  ;
  ; @example
  ;  (math/floor 4.80)
  ;  =>
  ;  4
  ;
  ; @return (integer)
  [n]
  (Math/floor n))

(defn round
  ; @param (float or integer) n
  ;
  ; @example
  ;  (math/round 4.20)
  ;  =>
  ;  4
  ;
  ; @example
  ;  (math/round 4.80)
  ;  =>
  ;  5
  ;
  ; @return (integer)
  [n]
  (Math/round n))

(defn absolute
  ; @param (float or integer) n
  ;
  ; @example
  ;  (math/absolute -4.20)
  ;  =>
  ;  4.20
  ;
  ; @return (float or integer)
  [n]
  (max n (- n)))

(defn negative
  ; @param (float or integer) n
  ;
  ; @example
  ;  (math/negative 4.20)
  ;  =>
  ;  -4.20
  ;
  ; @return (float or integer)
  [n]
  (if (>= 0 n)
      (return n)
      (- 0 n)))

(defn positive
  ; @param (float or integer) n
  ;
  ; @example
  ;  (math/positive -4.20)
  ;  =>
  ;  4.20
  ;
  ; @return (float or integer)
  [n]
  (if (<= 0 n)
      (return n)
      (- 0 n)))

(defn absolute-difference
  ; @param (float or integer) a
  ; @param (float or integer) b
  ;
  ; @usage
  ;  (math/absolute-difference 4.20 42)
  ;
  ; @return (float or integer)
  [a b]
  (absolute (- a b)))

(defn opposite
  ; @param (float or integer) n
  ;
  ; @example
  ;  (math/opposite 4.20)
  ;  =>
  ;  -4.20
  ;
  ; @return (float or integer)
  [n]
  (- 0 n))

(defn between?
  ; @param (float or integer) n
  ; @param (float or integer) min
  ; @param (float or integer) max
  ;
  ; @example
  ;  (math/between? 4.20 0 42)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n min max]
  (boolean (and (<= n max)
                (>= n min))))

(defn between!
  ; @param (float or integer) n
  ; @param (float or integer) min
  ; @param (float or integer) max
  ;
  ; @example
  ;  (math/between! 4.20 0 42)
  ;  =>
  ;  4.20
  ;
  ; @return (float or integer)
  [n min max]
  (cond (< n min) min
        (> n max) max
        :else     n))

(defn negative?
  ; @param (float or integer) n
  ;
  ; @example
  ;  (math/negative? -4.20)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (> 0 n))

(defn positive?
  ; @param (float or integer) n
  ;
  ; @example
  ;  (math/positive? 4.20)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (< 0 n))

(defn nonnegative?
  ; @param (float or integer) n
  ;
  ; @example
  ;  (math/nonnegative? 4.20)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (boolean (or (zero?     n)
               (positive? n))))

(defn collection-minimum
  ; @param (collection) n
  ;
  ; @example
  ;  (math/collection-minimum [100 14 3 55])
  ;  =>
  ;  3
  ;
  ; @example
  ;  (math/collection-minimum ["0" 1 "a" nil])
  ;  =>
  ;  1
  ;
  ; @example
  ;  (math/collection-minimum ["0" "a"])
  ;  =>
  ;  nil
  ;
  ; @return (nil or integer)
  [n]
  (apply min n))

(defn collection-maximum
  ; @param (collection) n
  ;
  ; @example
  ;  (math/collection-maximum [100 14 3 55])
  ;  =>
  ;  100
  ;
  ; @example
  ;  (math/collection-maximum ["0" 1 "a" nil])
  ;  =>
  ;  1
  ;
  ; @example
  ;  (math/collection-maximum ["0" "a"])
  ;  =>
  ;  nil
  ;
  ; @return (nil or integer)
  [n]
  (apply max n))

(defn minimum
  ; @param (list of float or integer) xyz
  ;
  ; @example
  ;  (math/minimum -4.20 2 0)
  ;  =>
  ;  2
  ;
  ; @return (float or integer)
  [& xyz]
  (collection-minimum xyz))

(defn maximum
  ; @param (list of float or integer) xyz
  ;
  ; @example
  ;  (math/maximum -4.20 2 0)
  ;  =>
  ;  2
  ;
  ; @return (float or integer)
  [& xyz]
  (collection-maximum xyz))

(defn percent
  ; @param (float or integer) total
  ; @param (float or integer) value
  ;
  ; @example
  ;  (math/percent 50 20)
  ;  =>
  ;  40
  ;
  ; @return (float or integer)
  [total value]
  (/ value (/ total 100)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn choose
  ; @param (integer) n
  ; @param (integer) limit
  ; @param (*) value-if-bigger
  ; @param (*)(opt) value-if-smaller
  ;
  ; @example
  ;  (math/choose 4.20 42 "A" "B")
  ;  =>
  ;  "B"
  ;
  ; @return (*)
  [n limit value-if-bigger & [value-if-smaller]]
  (if (>= limit n)
      (return value-if-smaller)
      (return value-if-bigger)))

(defn calc
  ; A calc fuggveny kiszamolja egy A valtozo erteketol fuggo B valtozo
  ; pillanatnyi erteket.
  ; Pl.: Egy elem left position erteke fuggjon a scroll-y kornyezeti valtozotol
  ;
  ; A fuggveny mukodese:
  ; Az n erteke amint eleri a domain-from erteket,
  ; akkor a fuggveny kimenete range-from ertekrol indul es amikor az n erteke
  ; elerei a domain-to erteket addigra a fuggveny kimenete elerei
  ; a range-to erteket.
  ;
  ; @param (float, int) n
  ;  Az A valtozo pillanatnyi erteke
  ; @param (vector) domain
  ;  Az A valtozo ertelmezesi tartomanya
  ;  XXX A domain-from mindig legyen kisebb, mint a domain-to!
  ;  [(integer) domain-from
  ;   (integer) domain-to
  ; @param (vector) range
  ;  A B valtozo kimeneti tartomanya
  ;  [(integer) range-from
  ;   (integer) range-to]
  ;
  ; @example
  ;  (math/calc 42 [10 50] [100 500])
  ;  =>
  ;  420
  ;
  ; @return (*)
  ;  A B valtozo pillanatnyi erteke (az A valtozotol fuggoen)
  [n [domain-from domain-to] [range-from range-to]]
  (let [domain-length (- domain-to domain-from)
        domain-offset (- n domain-from)
        range-length  (- range-to range-from)
        range-offset  (absolute range-from)
        ratio (/ range-length domain-length)]
       (if (< n domain-from)
           (return range-from)
           (if (> n domain-to)
               (return range-to)
               (+ (param range-from)
                  (* domain-offset ratio))))))
