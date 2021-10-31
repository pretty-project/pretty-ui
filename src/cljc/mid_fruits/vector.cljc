
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.10
; Description:
; Version: v1.0.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.vector
    (:require [mid-fruits.candy    :refer [param return]]
              [mid-fruits.integer  :as integer]
              [mid-fruits.keyword  :as keyword]
              [mid-fruits.math     :as math]
              [mid-fruits.loop     :refer [reduce-while reduce-while-indexed]]
              [mid-fruits.random   :as random]
              [mid-fruits.sequence :as sequence]
              [mid-fruits.string   :as string]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(def empty-vector [])



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn vector->map
  ; @param (vector) n
  ;
  ; @example
  ;  (vector/vector->map [:x :y :z])
  ;  => {0 :x 1 :y 2 :z}
  ;
  ; @return (map)
  [n]
  (reduce-kv (fn [%1 %2 %3] (assoc %1 (keyword (str %2)) %3))
             (param {})
             (param n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn nonempty?
  ; @param (*) n
  ;
  ; @usage
  ;  (vector/nonempty? [])
  ;
  ; @return (boolean)
  ;  Is n a nonempty vector?
  [n]
  (boolean (and (vector? n)
                (not (empty? n)))))

(defn dex-out-of-bounds?
  ; @param (vector) n
  ; @param (integer) dex
  ;
  ; @return (boolean)
  [n dex]
  (boolean (or (<  dex 0)
               (>= dex (count n)))))

(defn dex-in-bounds?
  ; @param (vector) n
  ; @param (integer) dex
  ;
  ; @return (boolean)
  [n dex]
  (let [dex-out-of-bounds? (dex-out-of-bounds? n dex)]
       (not dex-out-of-bounds?)))

(defn item-dex?
  ; @param (*) n
  ;
  ; @return (boolean)
  [n]
  (and (integer? n)
       (>=       n 0)))

(defn contains-item?
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (vector/contains-item? [:a :b] :a)
  ;  => true
  ;
  ; @return (boolean)
  [n x]
  ;(some? (some #(= x %) n)))
  (reduce-while (fn [_ %2] (= %2 x))
                (param false)
                (param n)
                (fn [%1 _] (true? %1))))

(defn min?
  ; @param (vector) n
  ; @param (integer) x
  ;
  ; @example
  ;  (vector/min? [:a :b] 3)
  ;  => false
  ;
  ; @example
  ;  (vector/min? [:a :b :c] 3)
  ;  => true
  ;
  ; @return (boolean)
  [n x]
  (boolean (and (nonempty? n)
                (>= (count n)
                    (param x)))))

(defn max?
  ; @param (vector) n
  ; @param (integer) x
  ;
  ; @example
  ;  (vector/max? [:a :b :c] 3)
  ;  => true
  ;
  ; @example
  ;  (vector/max? [:a :b :c :d] 3)
  ;  => false
  ;
  ; @return (boolean)
  [n x]
  (boolean (and (nonempty? n)
                (<= (count n)
                    (param x)))))

(defn longer?
  ; @param (vector) a
  ; @param (vector) b
  ;
  ; @example
  ;  (vector/longer? [:a :b :c] [:a :b :c :d])
  ;  => true
  ;
  ; @example
  ;  (vector/longer? [:a :b :c :d] [:a :b])
  ;  => false
  ;
  ; @return (boolean)
  [a b]
  (> (count b)
     (count a)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reverse-items
  ; @param (vector) n
  ;
  ; @example
  ;  (vector/reverse-items [:a :b :c])
  ;  => [:c :b :a]
  ;
  ; @return (vector)
  [n]
  (let [result (reverse n)]
       (vec result)))

(defn repeat-item
  ; @param (*) n
  ; @param (integer) x
  ;
  ; @example
  ;  (vector/repeat-item :a 5)
  ;  => [:a :a :a :a :a]
  ;
  ; @return (vector)
  [n x]
  (let [result (repeat x n)]
       (vec result)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cons-item
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (vector/cons-item [:a :b] :c)
  ;  => [:c :a :b]
  ;
  ; @return (vector)
  [n x]
  (let [result (cons x n)]
       (vec result)))

(defn conj-item
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (vector/conj-item [:a :b] :c)
  ;  => [:a :b :c]
  ;
  ; @example
  ;  (vector/conj-item [:a :b] :c :d)
  ;  => [:a :b :c :d]
  ;
  ; @return (vector)
  [n & xyz]
  (let [result (apply conj n xyz)]
       (vec result)))

(defn conj-item-once
  ; Conj item to vector if vector does not contains it.
  ;
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (vector/conj-item-once [:a :b] :b)
  ;  => [:a :b]
  ;
  ; @example
  ;  (vector/conj-item-once [:a :b] :c)
  ;  => [:a :b :c]
  ;
  ; @return (vector)
  [n x]
  (if (contains-item? n x)
      (return         n)
      (conj-item      n x)))

(defn conj-some
  ; Conj item to vector if item is not nil.
  ;
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (vector/conj-some [:a :b] :c)
  ;  => [:a :b :c]
  ;
  ; @example
  ;  (vector/conj-some [:a :b] nil)
  ;  => [:a :b]
  ;
  ; @return (vector)
  [n x]
  (if (some?     x)
      (conj-item n x)
      (return    n)))

(defn concat-items
  ; @param (list of vectors) abc
  ;
  ; @usage
  ;  (vector/concat-items [:a :b] [:c :d] [:e :f])
  ;
  ; @return (vector)
  [& abc]
  (vec (apply concat abc)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dex-first?
  ; @param (integer) dex
  ;
  ; @usage
  ;  (vector/dex-first? [:a :b] 1)
  ;
  ; @return (boolean)
  [dex]
  (= dex 0))

(defn dex-last?
  ; @param (vector) n
  ; @param (integer) dex
  ;
  ; @example
  ;  (vector/dex-last? [:a :b :c] 2)
  ;  => true
  ;
  ; @return (boolean)
  [n dex]
  (= (inc   dex)
     (count n)))

(defn last-dex
  ; @param (vector) n
  ;
  ; @example
  ;  (vector/last-dex [:a :b :c])
  ;  => 2
  ;
  ; @return (integer)
  [n]
  (if (nonempty? n)
      (dec (count n))))

(defn next-dex
  ; A vektor elemeinek szama alapjan meghatarozza, a dex utan kovetkezo indexet
  ;
  ; @param (vector) n
  ; @param (integer) dex
  ;
  ; @example
  ;  (vector/next-dex [:a :b :c :d] 3)
  ;  => 0
  ;
  ; @example
  ;  (vector/next-dex [] 3)
  ;  => 0
  ;
  ; @return (integer)
  [n dex]
  (if (nonempty?         n)
      (sequence/next-dex dex 0 (dec (count n)))
      (return            0)))

(defn inc-dex
  ; A vektor elemeinek szama alapjan meghatarozza, a dex utan kovetkezo indexet
  ; A legmagasabb erteknel megall
  ;
  ; @param (vector) n
  ; @param (integer) dex
  ;
  ; @example
  ;  (vector/inc-dex [:a :b :c :d] 3)
  ;  => 3
  ;
  ; @return (integer)
  [n dex]
  (if (dex-last? n dex)
      (return    dex)
      (inc       dex)))

(defn prev-dex
  ; A vektor elemeinek szama alapjan meghatarozza, a dex-et megelozo indexet
  ;
  ; @param (vector) n
  ; @param (integer) dex
  ;
  ; @example
  ;  (vector/prev-dex [:a :b :c :d] 0)
  ;  => 3
  ;
  ; @example
  ;  (vector/prev-dex [] 3)
  ;  => 0
  ;
  ; @return (integer)
  [n dex]
  (if (nonempty?         n)
      (sequence/prev-dex dex 0 (dec (count n)))
      (return            0)))

(defn dec-dex
  ; A vektor elemeinek szama alapjan meghatarozza, a dex-et megelozo indexet
  ; A legalacsonyabb erteknel (0) megall
  ;
  ; @param (vector) n
  ; @param (integer) dex
  ;
  ; @example
  ;  (vector/dec-dex [:a :b :c :d] 0)
  ;  => 0
  ;
  ; @return (integer)
  [n dex]
  (if (dex-first? dex)
      (return     dex)
      (dec        dex)))

(defn match-dex
  ; @param (vector) n
  ; @param (integer) dex
  ;
  ; @example
  ;  (vector/match-dex ["a" "b" "c"] 0)
  ;  => 0
  ;
  ; @example
  ;  (vector/match-dex ["a" "b" "c"] 1)
  ;  => 1
  ;
  ; @example
  ;  (vector/match-dex ["a" "b" "c"] 2)
  ;  => 2
  ;
  ; @example
  ;  (vector/match-dex ["a" "b" "c"] 3)
  ;  => 0
  ;
  ; @example
  ;  (vector/match-dex ["a" "b" "c"] 4)
  ;  => 1
  ;
  ; @return (integer)
  [n dex]
  (let [n-count (count n)
        x       (/ dex n-count)
        x       (math/floor x)
        y       (* x n-count)]
       (- dex y)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn nth-item
  ; @param (vector) n
  ; @param (integer) dex
  ;
  ; @return (*)
  [n dex]
  (if (and (nonempty?      n)
           (dex-in-bounds? n dex))
      (nth n dex)))

(defn last-item
  ; @param (vector) n
  ;
  ; @example
  ;  (vector/last-item [:a :b :c])
  ;  => :c
  ;
  ; @return (*)
  [n]
  (last n))

(defn first-item
  ; @param (vector) n
  ;
  ; @example
  ;  (vector/first-item [:a :b :c])
  ;  => :a
  ;
  ; @return (*)
  [n]
  (first n))

(defn item-last?
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (vector/item-last? [:a :b] :b)
  ;  => true
  ;
  ; @return (boolean)
  [n x]
  (= (last n)
     (param x)))

(defn item-first?
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (vector/item-first? [:a :b] :a)
  ;  => true
  ;
  ; @return (boolean)
  [n x]
  (= (first n)
     (param x)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ranged-items
  ; @param (vector) n
  ; @param (integer) low
  ; @param (integer)(opt) high
  ;
  ; @example
  ;  (vector/ranged-items [:a :b :c :d :e :f] 1 3)
  ;  => [:b :c]
  ;
  ; @example
  ;  (vector/ranged-items [:a :b :c :d :e :f] 2)
  ;  => [:c :d :e :f]
  ;
  ; @return (vector)
  [n low & [high]]
  (let [high (or  high (count n))
        high (min high (count n))]
       (if (and (nonempty? n)
                (<  low high)
                (>= low 0))
           (subvec n low high)
           (return []))))

(defn last-items
  ; @param (vector) n
  ; @param (integer) length
  ;
  ; @example
  ;  (vector/last-items [:a :b :c :d :e] 2)
  ;  => [:d :e]
  ;
  ; @return (vector)
  [n length]
  (if (<= (count n)
          (param length))
      (return n)
      (subvec (param n)
              (- (count n)
                 (param length)))))

(defn first-items
  ; @param (vector) n
  ; @param (integer) length
  ;
  ; @example
  ;  (vector/first-items [:a :b :c :d :e] 3)
  ;  => [:a :b :c]
  ;
  ; @return (vector)
  [n length]
  (if (<= (count n)
          (param length))
      (return n)
      (subvec n 0 length)))

(def trim first-items)

(defn shift-first-item
  ; @param (vector) n
  ;
  ; @example
  ;  (vector/shift-first-item [:a :b :c :d :e])
  ;  => [:b :c :d :e]
  ;
  ; @return (vector)
  [n]
  (if (nonempty? n)
      (subvec    n 1)
      (return    [])))

(defn pop-last-item
  ; @param (vector) n
  ;
  ; @example
  ;  (vector/pop-last-item [:a :b :c :d :e])
  ;  => [:a :b :c :d]
  ;
  ; @return (vector)
  [n]
  (if (nonempty? n)
      (let [length (count n)]
           (subvec n 0 (dec length)))
      (return [])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn count?
  ; @param (vector) n
  ; @param (integer) x
  ;
  ; @example
  ;  (vector/count? [:a :b :c] 3)
  ;  => true
  ;
  ; @return (boolean)
  [n x]
  (boolean (or (and (nonempty? n)
                    (= x (count n)))
               (and (= n [])
                    (= x 0)))))

(defn count!
  ; @param (vector) n
  ; @param (integer) x
  ;
  ; @example
  ;  (vector/count! [:a :b :c] 3)
  ;  => [:a :b :c]
  ;
  ; @example
  ;  (vector/count! [:a :b :c] 2)
  ;  => [:a :b]
  ;
  ; @example
  ;  (vector/count! [:a :b :c] 5)
  ;  => [:a :b :c nil nil]
  ;
  ; @return (vector)
  [n x]
  (cond (= (count n) x) (return       n)
        (> (count n) x) (first-items  n x)
        (< (count n) x) (concat-items n (repeat-item nil (- x (count n))))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-item
  ; @param (vector) n
  ; @param (*) dex
  ;
  ; @example
  ;  (vector/remove-item [:a :b] :b)
  ;  => [:a]
  ;
  ; @example
  ;  (vector/remove-item [:a :b :a] :a)
  ;  => [:b]
  ;
  ; @return (vector)
  [n x]
  (vec (remove #(= % x)
                (param n))))

(defn remove-nth-item
  ; @param (vector) n
  ; @param (integer) dex
  ;
  ; @example
  ;  (vector/remove-nth-item [:a :b :c :d :e] 2)
  ;  => [:a :b :d :e]
  ;
  ; @return (vector)
  [n dex]
  (concat-items (ranged-items n 0 dex)
                (ranged-items n (inc dex))))

(defn remove-items
  ; @param (vector) n
  ; @param (vector) xyz
  ;
  ; @example
  ;  (vector/remove-items [:a :b :c] [:b :c])
  ;  => [:a]
  ;
  ; @example
  ;  (vector/remove-items [:a :b :b :c ] [:b :c])
  ;  => [:a]
  ;
  ; @return (vector)
  [n xyz]
  (reduce (fn [%1 %2]
              (if (contains-item? xyz %2)
                  (return %1)
                  (conj   %1 %2)))
          (param [])
          (param n)))

(defn difference
  ; @param (vector) a
  ; @param (vector) b
  ;
  ; @example
  ;  (vector/difference [:a :b :c] [:b :c])
  ;  => [:a]
  ;
  ; @return (vector)
  [a b]
  (remove-items a b))

(defn keep-items
  ; @param (vector) n
  ; @param (vector) xyz
  ;
  ; @example
  ;  (vector/keep-items [:a :b :c :d] [:b :c])
  ;  => [:b :c]
  ;
  ; @return (vector)
  [n xyz]
  (reduce (fn [%1 %2]
              (if (contains-item? xyz %2)
                  (conj %1 %2)
                  (return %1)))
          (param [])
          (param n)))

(defn remove-items-kv
  ; @param (maps in vector) n
  ; @param (*) k
  ; @param (*) v
  ;
  ; @example
  ;  (vector/remove-items-kv [{:a "a"} {:b "b"} {:c "c"}] :b "b")
  ;  => [{:a "a"} {:c "c"}]
  ;
  ; @return (maps in vector)
  [n k v]
  (vec (remove #(= (k %) v)
                (param n))))

(defn remove-last-item
  ; @param (vector) n
  ;
  ; @usage
  ;  (vector/remove-last-item [:a :b :c])
  ;
  ; @return (vector)
  [n]
  (vec (drop-last n)))

(defn remove-first-item
  ; @param (vector) n
  ;
  ; @usage
  ;  (vector/remove-first-item [:a :b :c])
  ;
  ; @return (vector)
  [n]
  (vec (drop 1 n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn concat-once
  ; @param (vector) a
  ; @param (vector) b
  ;
  ; @example
  ;  (vector/concat-once [:a :b :c] [:c :d :e :a])
  ;  => [:b :c :d :e :a]
  ;
  ; @return (vector)
  [a b]
  (let [c (remove-items a b)]
       (vec (concat c b))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn move-item
  ; @param (vector) n
  ; @param (integer) from
  ; @param (integer) to
  ;
  ; @example
  ;  (vector/move-item [:a :b :c :d :e :f :g :h] 2 2)
  ;  => [:a :b :c :d :e :f :g :h]
  ;
  ; @example
  ;  (vector/move-item [:a :b :c :d :e :f :g :h] 2 5)
  ;  => [:a :b :d :e :c :f :g :h]
  ;
  ; @example
  ;  (vector/move-item [:a :b :c :d :e :f :g :h] 5 2)
  ;  => [:a :b :f :c :d :e :g :h]
  ;
  ; @return (vector)
  [n from to]
  (if (dex-in-bounds? n from)
      (let [to (math/between! to 0 (count n))]
                 ; Stay in place
           (cond (= from to)
                 (return n)
                 ; Move item fwd
                 (< from to)
                 (concat-items (ranged-items n 0 from)
                               (ranged-items n (inc from) (inc to))
                               (ranged-items n from (inc from))
                               (ranged-items n (inc to)))
                 ; Move item bwd
                 (> from to)
                 (concat-items (ranged-items n 0 to)
                               (ranged-items n from (inc from))
                               (ranged-items n to from)
                               (ranged-items n (inc from)))))
      (return n)))

(defn move-item-to-last
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (vector/move-item-to-last [:a :b] :a)
  ;  => [:b :a]
  ;
  ; @example
  ;  (vector/move-item-to-last [:b] :a)
  ;  => [:b :a]
  ;
  ; @return (vector)
  [n x]
  (conj (remove-item n x)
        (param x)))

(defn move-item-to-first
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (vector/move-item-to-first [:a :b] :b)
  ;  => [:b :a]
  ;
  ; @example
  ;  (vector/move-item-to-first [:a] :b)
  ;  => [:b :a]
  ;
  ; @return (vector)
  [n x]
  (vec (cons x (remove-item n x))))

(defn change-item
  ; @param (vector) n
  ; @param (*) x
  ; @param (*) y
  ;
  ; @example
  ;  (vector/change-item [:a :b :c :d :c] :c :x)
  ;  => [:a :b :x :d :x]
  ;
  ; @return (vector)
  [n x y]
  (reduce (fn [%1 %2]
              (if (= %2 x)
                  (conj-item %1 y)
                  (conj-item %1 %2)))
          (param [])
          (param n)))

(defn change-nth-item
  ; @param (vector) n
  ; @param (integer) dex
  ; @param (*) x
  ;
  ; @example
  ;  (vector/change-nth-item [:a :b :c :d] 1 :x)
  ;  => [:a :x :c :d]
  ;
  ; @example
  ;  (vector/change-nth-item [:a :b :c :d] 999 :x)
  ;  => [:a :b :c :d]
  ;
  ; @return (vector)
  [n dex x]
  (if (and (vector?        n)
           (dex-in-bounds? n dex))
      (concat-items (subvec n 0 dex)
                    [x]
                    (subvec n (inc dex)))
      (return n)))

(defn inject-item
  ; @param (vector) n
  ; @param (integer) dex
  ; @param (*) x
  ;
  ; @example
  ;  (vector/inject-item [:a :b :c] 2 :d)
  ;  => [:a :b :d :c]
  ;
  ; @example
  ;  (vector/inject-item [:a :b :c] 999 :d)
  ;  => [:a :b :d :c]
  ;
  ; @example
  ;  (vector/inject-item nil 999 :d)
  ;  => [:d]
  ;
  ; @example
  ;  (vector/inject-item {:a "b"} 1 :d)
  ;  => {:a "b"}
  ;
  ; @return (vector)
  [n dex x]
  (cond (vector? n)
        (if (dex-out-of-bounds? n dex)
            (conj-item n x)
            (concat-items (subvec n 0 dex)
                          [x]
                          (subvec n dex)))
        (nil? n) (return [x])
        :else    (return n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-duplicates
  ; @param (vector) n
  ;
  ; @usage
  ;  (vector/remove-duplicates [:a :b :c :a])
  ;
  ; @return (vector)
  [n]
  (reduce (fn [%1 %2]
              (if (contains-item? %1 %2)
                  (return %1)
                  (conj-item %1 %2)))
          (param [])
          (param n)))

(defn similars
  ; @param (vector) a
  ; @param (vector) b
  ;
  ; @example
  ;  (vector/similars [:a :b :c] [:c :d :e])
  ;  => [:c]
  ;
  ; @return (vector)
  [a b]
  (reduce (fn [%1 %2]
              (if (contains-item? b %2)
                  (conj-item %1 %2)
                  (return    %1)))
          (param [])
          (param a)))

(defn contains-similars?
  ; @param (vector) a
  ; @param (vector) b
  ;
  ; @example
  ;  (vector/contains-similars? [:a :b :c] [:c :d :e])
  ;  => true
  ;
  ; @example
  ;  (vector/contains-similars? [:a :b :c] [:d :e :f])
  ;  => false
  ;
  ; @return (boolean)
  [a b]
  (nonempty? (similars a b)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-last-dex
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (vector/item-last-dex [:a :b :a :b] :b)
  ;  => 3
  ;
  ; @return (nil or integer)
  [n x]
  (cond (not (vector? n))
        (return nil)
        (not (contains-item? n x))
        (return nil)
        :else (reduce-kv (fn [%1 %2 %3]
                             (if (= x %3)
                                 (string/to-integer (name %2))
                                 (return %1)))
                         (param nil)
                         (vector->map n))))

(defn item-first-dex
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (vector/item-first-dex [:a :b :a :b] :b)
  ;  => 1
  ;
  ; @return (nil or integer)
  [n x]
  (cond (not (vector? n))          nil
        (not (contains-item? n x)) nil
        :else
        (reduce-kv (fn [%1 %2 %3]
                       (cond (some? %1) (return %1)
                             (= x %3)   (string/to-integer (name %2))
                             :else      (return %1)))
                   (param nil)
                   (vector->map n))))

(defn items-before-first-occurence
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (vector/items-before-first-occurence [:a :b :c :d :d :e] :d)
  ;  => [:a :b :c]
  ;
  ; @return (vector)
  [n x]
  (if-let [item-first-dex (item-first-dex n x)]
          (subvec n 0 item-first-dex)
          (return [])))

(defn items-after-first-occurence
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (vector/items-after-first-occurence [:a :b :c :d :d :e] :d)
  ;  => [:d :e]
  ;
  ; @return (vector)
  [n x]
  (if-let [item-first-dex (item-first-dex n x)]
          (subvec n (inc item-first-dex))
          (return [])))

(defn remove-first-occurence
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (vector/remove-first-occurence [:a :b :c :d :d :e] :d)
  ;  => [:a :b :c :d :e]
  ;
  ; @return (vector)
  [n x]
  (if (contains-item? n x)
      (concat-items (items-before-first-occurence n x)
                    (items-after-first-occurence  n x))
      (return n)))

(defn move-first-occurence
  ; @param (vector) n
  ; @param (*) x
  ; @param (integer) dex
  ;
  ; @example
  ;  (vector/move-first-occurence [:a :b :c :a :b :c] :b 3)
  ;  => [:a :c :b :a :b :c]
  ;
  ; @example
  ;  (vector/move-first-occurence [:a :b :c :a :b :c] :b 1)
  ;  => [:a :b :c :a :b :c]
  ;
  ; @example
  ;  (vector/move-first-occurence [:a :b :c :a :b :c] :b 20)
  ;  => [:a :c :a :b :c :b]
  ;
  ; @return (vector)
  [n x dex]
  (if-let [item-first-dex (item-first-dex n x)]
          (move-item n item-first-dex dex)
          (return    n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn filter-items
  ; @param (vector) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (vector/filter-items [:a :b "c"] keyword?)
  ;  => [:a :b]
  ;
  ; @return (vector)
  [n test-f]
  (vec (filter test-f n)))

(defn first-of-filtered
  ; @param (vector) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (vector/first-of-filtered ["a" :b "c" :d "e"] keyword?)
  ;  => :b
  ;
  ;  (vector/first-of-filtered ["a" :b "c" :d "e"] #(string? %1))
  ;  => "a"
  ;
  ; @return (*)
  [n test-f]
  (reduce-while (fn [_ %2]
                    (if (test-f %2)
                        (return %2)))
                (param nil)
                (param n)
                (fn [%1 _] (some? %1))))

(defn last-of-filtered
  ; @param (vector) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (vector/first-of-filtered ["a" :b "c" :d "e"] keyword?)
  ;  => :d
  ;
  ;  (vector/first-of-filtered ["a" :b "c" :d "e"] #(string? %1))
  ;  => "e"
  ;
  ; @return (*)
  [n test-f]
  (first-of-filtered (reverse-items n) test-f))

(defn nth-of-filtered
  ; @param (vector) n
  ; @param (function) test-f
  ; @param (integer) dex
  ;
  ; @example
  ;  (vector/nth-of-filtered ["a" :b "c" :d "e"] keyword? 2)
  ;  => :d
  ;
  ;  (vector/nth-of-filtered ["a" :b "c" :d "e"] #(string? %1) 2)
  ;  => "c"
  ;
  ; @return (*)
  [n test-f dex]
  (let [filtered-items (filter-items n test-f)]
       (if (> (count filtered-items))
           (return dex)
           (nth (filter-items n test-f)
                (param dex)))))

(defn filtered-count
  ; @param (vector) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (vector/filtered-count [:a :b "c"] keyword?)
  ;  => 2
  ;
  ; @return (integer)
  [n test-f]
  (count (filter-items n test-f)))

(defn filtered-count?
  ; @param (vector) n
  ; @param (function) test-f
  ; @param (integer) x
  ;
  ; @example
  ;  (vector/filtered-count? [:a :b "c"] keyword? 2)
  ;  => true
  ;
  ; @return (boolean)
  [n test-f x]
  (= x (filtered-count n test-f)))



;; -- Alphabetical ordering ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sortable-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) n
  ;
  ; @return (vector)
  [n]
  (reduce (fn [%1 %2]
              (cond (keyword? %2) (conj-item %1 %2)
                    (string?  %2) (conj-item %1 %2)
                    :else         (return %1)))
          (param [])
          (param n)))

(defn- unsortable-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) n
  ;
  ; @return (vector)
  [n]
  (reduce (fn [%1 %2]
              (cond (keyword? %2) (return %1)
                    (string?  %2) (return %1)
                    :else         (conj-item %1 %2)))
          (param [])
          (param n)))

(defn- sortable-items->strings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) sortable-items
  ;
  ; @example
  ;  (sortable-items->strings [:b "a"])
  ;  =>
  ;  ["b" "a"]
  ;
  ; @return (vector)
  [sortable-items]
  (reduce (fn [%1 %2]
              (cond (keyword? %2) (conj-item %1 (keyword/to-string %2))
                    (string?  %2) (conj-item %1 %2)))
          (param [])
          (param sortable-items)))

(defn- sortable-items->ordered-strings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) sortable-items
  ;
  ; @example
  ;  (sortable-items->ordered-strings [:b "a"])
  ;  =>
  ;  ["a" "b"]
  ;
  ; @return (vector)
  [sortable-items]
  (vec (sort (sortable-items->strings sortable-items))))

(defn- sortable-items->indexed-map
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) sortable-items
  ;
  ; @return (map)
  [sortable-items]
  (let [ordered-strings (sortable-items->ordered-strings sortable-items)]
       (reduce (fn [%1 %2]
                   (let [string-item    (cond (keyword? %2) (keyword/to-string %2) :else %2)
                         ordinal-number (item-first-dex ordered-strings string-item)]
                        (assoc %1 (integer/to-keyword ordinal-number) %2)))
               (param {})
               (param sortable-items))))

(defn- sort-sortable-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) sortable-items
  ;
  ; @return (vector)
  [sortable-items]
  (let [indexed-map (sortable-items->indexed-map sortable-items)]
       (reduce (fn [%1 %2]
                   (conj-item (param %1)
                              (get indexed-map (keyword (str %2)))))
               (param [])
               (range (count sortable-items)))))

(defn abc
  ; @param (vector) n
  ;
  ; @example
  ;  (vector/abc [:a :b :d :c])
  ;  => [:a :b :c :d]
  ;
  ; @return (vector)
  [n]
  (let [sortable-items   (sortable-items   n)
        unsortable-items (unsortable-items n)]
       (concat-items unsortable-items (sort-sortable-items sortable-items))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn order-items
  ; @param (vector) n
  ; @param (function) comparator-f
  ;
  ; @example
  ;  (vector/order-items ["a" "c" "b"] string/abc?)
  ;  => ["a" "b" "c"]
  ;
  ; @return (vector)
  [n comparator-f]
  (vec (sort comparator-f n)))

(defn items-ordered?
  ; @param (vector) n
  ; @param (function) order-f
  ;
  ; @example
  ;  (vector/items-ordered? ["a" "c" "b"] string/abc?)
  ;  => false
  ;
  ; @example
  ;  (vector/items-ordered? ["a" "b" "c"] string/abc?)
  ;  => true
  ;
  ; @return (boolean)
  [n order-f]
  (let [sorted-n (order-items n order-f)]
       (= n sorted-n)))

(defn order-items-by
  ; @param (vector) n
  ; @param (function)(opt) comparator-f
  ; @param (function) value-f
  ;
  ; @example
  ;  (vector/order-items-by [{:a 3} {:a 2} {:a 1}] :a)
  ;  => [{:a 1} {:a 2} {:a 3}]
  ;
  ; @example
  ;  (vector/order-items-by [[1 2] [2 2] [2 3]] > first)
  ;  => [[2 2] [2 3] [1 2]]
  ;
  ; @return (vector)
  ([n value-f]
   (vec (sort-by value-f n)))

  ([n comparator-f value-f]
   (vec (sort-by value-f comparator-f n))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn any-item-match?
  ; @param (vector) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (vector/any-item-match? [:a :b :c] string?)
  ;  => false
  ;
  ; @example
  ;  (vector/any-item-match? [:a "b" :c] string?)
  ;  => true
  ;
  ; @return (boolean)
  [n test-f]
  (reduce-while (fn [_ %2] (test-f %2))
                (param false)
                (param n)
                (fn [%1 _] (boolean %1))))

(defn all-items-match?
  ; @param (vector) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (vector/all-items-match? [:a "b" "c"] string?)
  ;  => false
  ;
  ; @example
  ;  (vector/all-items-match? ["a" "b" "c"] string?)
  ;  => true
  ;
  ; @return (boolean)
  [n test-f]
  (every? test-f n))

(defn get-first-match-item
  ; @param (vector) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (vector/get-first-match-item [:a :b :c] string?)
  ;  => nil
  ;
  ; @example
  ;  (vector/get-first-match-item [:a "b" :c] string?)
  ;  => "b"
  ;
  ; @return (*)
  [n test-f]
  (reduce-while (fn [_ %2] (if (test-f %2)
                               (return %2)))
                (param nil)
                (param n)
                (fn [%1 _] (some? %1))))

(defn get-first-match-item-dex
  ; @param (vector) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (vector/get-first-match-item-dex [:a :b :c] string?)
  ;  => nil
  ;
  ; @example
  ;  (vector/get-first-match-item-dex [:a "b" :c] string?)
  ;  => 1
  ;
  ; @return (*)
  [n test-f]
  (reduce-while-indexed (fn [_ %2 dex] (if (test-f %2)
                                           (return dex)))
                        (param nil)
                        (param n)
                        (fn [%1 _] (some? %1))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn compared-items-ordered?
  ; @param (vector) a
  ; @param (vector) b
  ; @param (function) comparator-f
  ;
  ; @example
  ;  (vector/compared-items-ordered? [0 1 3] [0 1 2] <)
  ;  => false
  ;
  ; @example
  ;  (vector/compared-items-ordered? [0 1 3] [0 1 4] <)
  ;  => true
  ;
  ; @example
  ;  (vector/compared-items-ordered? [0 1 3] [0 1 3] <)
  ;  => true
  ;
  ; @example
  ;  (vector/compared-items-ordered? ["a" "b" "c"] ["d" "a"] string/abc?)
  ;  => true
  ;
  ; @return (boolean)
  [a b comparator-f]
  (let [max-count (min (count a)
                       (count b))
        ; Ha a vagy b vektor elemeinek száma nem egyenlő, akkor az elemek
        ; összehasonlítása az alacsonyabb elemszámig történik.
        base      (trim a max-count)]
       (boolean (reduce-while-indexed (fn [%1 %2 dex]
                                          (let [x (param %2)
                                                y (nth-item b dex)]
                                               (if (= x y)
                                                   (return %1)
                                                   (comparator-f x y))))
                                      (param :no-difference-found)
                                      (param base)
                                      (fn [%1 _ _] (boolean? %1))))))
