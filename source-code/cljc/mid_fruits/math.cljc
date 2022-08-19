

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.math
    (:require [mid-fruits.candy :refer [param return]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (float)
(def pi Math/PI)

(defn power
  ; @param (number) x
  ; @param (integer) n
  ;
  ; @example
  ;  (math/power 2 3)
  ;  =>
  ;  8
  ;
  ; @return (number)
  [x n]
  (if (zero? n) 1
      (* x (power x (dec n)))))

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
  (and (<= n max)
       (>= n min)))

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
  (or (zero?     n)
      (positive? n)))

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

; TEMP
; Ennek mi az igazi neve?
(defn percent-result
  ; @param (float or integer) total
  ; @param (float or integer) percentage
  ;
  ; @example
  ;  (math/percent 50 40)
  ;  =>
  ;  20
  ;
  ; @return (float or integer)
  [total percentage]
  (/ (* total percentage) 100))



;; -- Domain functions --------------------------------------------------------
;; ----------------------------------------------------------------------------

; Egy n szám milyen tulajdonságokkal rendelkezik egy tartományban.
; Pl.: 5 tartományai: -4–0, 1–5, 6–10, 11–15, ...

(defn domain-inchoate
  ; @param (integer) n
  ; @param (integer) domain
  ;
  ; @example
  ;  (math/domain-inchoate 9 5)
  ;  =>
  ;  2
  ;
  ; @example
  ;  (math/domain-inchoate 10 5)
  ;  =>
  ;  2
  ;
  ; @example
  ;  (math/domain-inchoate 11 5)
  ;  =>
  ;  3
  ;
  ; @example
  ;  (math/domain-inchoate 0 5)
  ;  =>
  ;  0
  ;
  ; @return (integer)
  [n domain]
  ; Az n értéke hányadik domain tartományban helyezkedik el
  (let [quot (quot n domain)
        rem  (rem  n domain)]
       (if (= rem 0)
           (return quot)
           (inc    quot))))

(defn domain-floor
  ; @param (integer) n
  ; @param (integer) domain
  ;
  ; @example
  ;  (math/domain-floor 9 5)
  ;  =>
  ;  6
  ;
  ; @example
  ;  (math/domain-floor 10 5)
  ;  =>
  ;  6
  ;
  ; @example
  ;  (math/domain-floor 11 5)
  ;  =>
  ;  11
  ;
  ; @example
  ;  (math/domain-floor 0 5)
  ;  =>
  ;  -4
  ;
  ; @return (integer)
  [n domain]
  ; Az n értékéhez tartozó tartomány kezdő értéke
  (let [quot (quot n domain)
        rem  (rem  n domain)]
       (if (= rem 0)
           (inc (* (dec quot) domain))
           (inc (*      quot  domain)))))

(defn domain-ceil
  ; @param (integer) n
  ; @param (integer) domain
  ;
  ; @example
  ;  (math/domain-ceil 9 5)
  ;  =>
  ;  10
  ;
  ; @example
  ;  (math/domain-ceil 10 5)
  ;  =>
  ;  10
  ;
  ; @example
  ;  (math/domain-ceil 11 5)
  ;  =>
  ;  15
  ;
  ; @example
  ;  (math/domain-ceil 0 5)
  ;  =>
  ;  0
  ;
  ; @return (integer)
  [n domain]
  ; Az n értékéhez tartozó tartomány záró értéke
  (let [quot (quot n domain)
        rem  (rem  n domain)]
       (if (= rem 0)
           (*      quot  domain)
           (* (inc quot) domain))))



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
  (if (>= limit n) value-if-smaller value-if-bigger))

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
