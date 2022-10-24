
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



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
  ;  (power 2 3)
  ;  =>
  ;  8
  ;
  ; @return (number)
  [x n]
  (if (zero? n) 1
      (* x (power x (dec n)))))

(defn floor
  ; @param (number) n
  ;
  ; @example
  ;  (floor 4.20)
  ;  =>
  ;  4
  ;
  ; @example
  ;  (floor 4.80)
  ;  =>
  ;  4
  ;
  ; @return (integer)
  [n]
  (Math/floor n))

(defn ceil
  ; @param (number) n
  ;
  ; @example
  ;  (ceil 4.20)
  ;  5
  ;  4
  ;
  ; @example
  ;  (ceil 4.80)
  ;  =>
  ;  5
  ;
  ; @return (integer)
  [n]
  (Math/ceil n))

(defn round
  ; @param (number) n
  ; @param (integer) precision
  ;
  ; @example
  ;  (round 4.20)
  ;  =>
  ;  4
  ;
  ; @example
  ;  (round 4.80)
  ;  =>
  ;  5
  ;
  ; @example
  ;  (round 420 100)
  ;  =>
  ;  400
  ;
  ; @return (integer)
  ([n]
   (if (> 0.5 (rem n 1)) (quot n 1)
                    (inc (quot n 1))))

  ([n precision]
   (-> n (/ precision)
         (round)
         (* precision))))

(defn absolute
  ; @param (number) n
  ;
  ; @example
  ;  (absolute -4.20)
  ;  =>
  ;  4.20
  ;
  ; @return (number)
  [n]
  (max n (- n)))

(defn negative
  ; @param (number) n
  ;
  ; @example
  ;  (negative 4.20)
  ;  =>
  ;  -4.20
  ;
  ; @return (number)
  [n]
  (if (>= 0 n)
      (return n)
      (- 0 n)))

(defn positive
  ; @param (number) n
  ;
  ; @example
  ;  (positive -4.20)
  ;  =>
  ;  4.20
  ;
  ; @return (number)
  [n]
  (if (<= 0 n)
      (return n)
      (- 0 n)))

(defn absolute-difference
  ; @param (number) a
  ; @param (number) b
  ;
  ; @usage
  ;  (absolute-difference 4.20 42)
  ;
  ; @return (number)
  [a b]
  (absolute (- a b)))

(defn opposite
  ; @param (number) n
  ;
  ; @example
  ;  (opposite 4.20)
  ;  =>
  ;  -4.20
  ;
  ; @return (number)
  [n]
  (- 0 n))

(defn between?
  ; @param (number) n
  ; @param (number) min
  ; @param (number) max
  ;
  ; @example
  ;  (between? 4.20 0 42)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n min max]
  (and (<= n max)
       (>= n min)))

(defn between!
  ; @param (number) n
  ; @param (number) min
  ; @param (number) max
  ;
  ; @example
  ;  (between! 4.20 0 42)
  ;  =>
  ;  4.20
  ;
  ; @return (number)
  [n min max]
  (cond (< n min) min
        (> n max) max
        :return   n))

(defn negative?
  ; @param (number) n
  ;
  ; @example
  ;  (negative? -4.20)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (> 0 n))

(defn positive?
  ; @param (number) n
  ;
  ; @example
  ;  (positive? 4.20)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (< 0 n))

(defn nonnegative?
  ; @param (number) n
  ;
  ; @example
  ;  (nonnegative? 4.20)
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
  ;  (collection-minimum [100 14 3 55])
  ;  =>
  ;  3
  ;
  ; @example
  ;  (collection-minimum ["0" 1 "a" nil])
  ;  =>
  ;  1
  ;
  ; @example
  ;  (collection-minimum ["0" "a"])
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
  ;  (collection-maximum [100 14 3 55])
  ;  =>
  ;  100
  ;
  ; @example
  ;  (collection-maximum ["0" 1 "a" nil])
  ;  =>
  ;  1
  ;
  ; @example
  ;  (collection-maximum ["0" "a"])
  ;  =>
  ;  nil
  ;
  ; @return (nil or integer)
  [n]
  (apply max n))

(defn minimum
  ; @param (list of number) xyz
  ;
  ; @example
  ;  (minimum -4.20 2 0)
  ;  =>
  ;  2
  ;
  ; @return (number)
  [& xyz]
  (collection-minimum xyz))

(defn maximum
  ; @param (list of numbers) xyz
  ;
  ; @example
  ;  (maximum -4.20 2 0)
  ;  =>
  ;  2
  ;
  ; @return (number)
  [& xyz]
  (collection-maximum xyz))

(defn percent->angle
  ; @param (number) n
  ;
  ; @example
  ;  (percent->angle 50)
  ;  =>
  ;  180
  ;
  ; @return (number)
  [n]
  (* 360 (/ n 100)))

(defn percent
  ; @param (number) total
  ; @param (number) value
  ;
  ; @example
  ;  (percent 50 20)
  ;  =>
  ;  40
  ;
  ; @return (number)
  [total value]
  (/ value (/ total 100)))

; TEMP
; Ennek mi az igazi neve?
(defn percent-result
  ; @param (number) total
  ; @param (number) percentage
  ;
  ; @example
  ;  (percent 50 40)
  ;  =>
  ;  20
  ;
  ; @return (number)
  [total percentage]
  (/ (* total percentage) 100))

(defn percent-rest
  ; @param (number) total
  ; @param (number) percentage
  ;
  ; @example
  ;  (percent-rest 50 40)
  ;  =>
  ;  30
  ;
  ; @return (number)
  [total percentage]
  (- total (percent-result total percentage)))

(defn percent-add
  ; @param (number) total
  ; @param (number) percentage
  ;
  ; @example
  ;  (percent-add 50 40)
  ;  =>
  ;  70
  ;
  ; @return (number)
  [total percentage]
  (+ total (percent-result total percentage)))



;; -- Domain functions --------------------------------------------------------
;; ----------------------------------------------------------------------------

; Egy n szám milyen tulajdonságokkal rendelkezik egy tartományban.
; Pl. 5 tartományai: -4–0, 1–5, 6–10, 11–15, ...

(defn domain-inchoate
  ; @param (integer) n
  ; @param (integer) domain
  ;
  ; @example
  ;  (domain-inchoate 9 5)
  ;  =>
  ;  2
  ;
  ; @example
  ;  (domain-inchoate 10 5)
  ;  =>
  ;  2
  ;
  ; @example
  ;  (domain-inchoate 11 5)
  ;  =>
  ;  3
  ;
  ; @example
  ;  (domain-inchoate 0 5)
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
  ;  (domain-floor 9 5)
  ;  =>
  ;  6
  ;
  ; @example
  ;  (domain-floor 10 5)
  ;  =>
  ;  6
  ;
  ; @example
  ;  (domain-floor 11 5)
  ;  =>
  ;  11
  ;
  ; @example
  ;  (domain-floor 0 5)
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
  ;  (domain-ceil 9 5)
  ;  =>
  ;  10
  ;
  ; @example
  ;  (domain-ceil 10 5)
  ;  =>
  ;  10
  ;
  ; @example
  ;  (domain-ceil 11 5)
  ;  =>
  ;  15
  ;
  ; @example
  ;  (domain-ceil 0 5)
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
  ;  (choose 4.20 42 "A" "B")
  ;  =>
  ;  "B"
  ;
  ; @return (*)
  [n limit value-if-bigger & [value-if-smaller]]
  (if (>= limit n) value-if-smaller value-if-bigger))

(defn calc
  ; A calc fuggveny kiszamolja egy A valtozo erteketol fuggo B valtozo
  ; pillanatnyi erteket.
  ; Pl. Egy elem left position erteke fuggjon a scroll-y kornyezeti valtozotol
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
  ;  (calc 42 [10 50] [100 500])
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
