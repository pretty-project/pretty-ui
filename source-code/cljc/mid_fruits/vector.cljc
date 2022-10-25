

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.vector
    (:require [mid-fruits.candy    :refer [param return]]
              [mid-fruits.math     :as math]
              [mid-fruits.loop     :refer [some-indexed]]
              [mid-fruits.sequence :as sequence]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn to-map
  ; @param (vector) n
  ;
  ; @example
  ;  (to-map [:x :y :z])
  ;  =>
  ;  {0 :x 1 :y 2 :z}
  ;
  ; @return (map)
  [n]
  (letfn [(f [%1 %2 %3] (assoc %1 (keyword (str %2)) %3))]
         (reduce-kv f {} n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn nonempty?
  ; @param (*) n
  ;
  ; @usage
  ;  (nonempty? [])
  ;
  ; @return (boolean)
  ;  Is n a nonempty vector?
  [n]
  (and (-> n vector?)
       (-> n  empty? not)))

(defn dex-out-of-bounds?
  ; @param (vector) n
  ; @param (integer) dex
  ;
  ; @return (boolean)
  [n dex]
  (or (<  dex 0)
      (>= dex (count n))))

(defn dex-in-bounds?
  ; @param (vector) n
  ; @param (integer) dex
  ;
  ; @return (boolean)
  [n dex]
  (and (>= dex 0)
       (<  dex (count n))))

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
  ;  (contains-item? [:a :b] :a)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n x]
  (boolean (some #(= x %) n)))

(defn not-contains-item?
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (not-contains-item? [:a :b] :a)
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n x]
  (not (contains-item? n x)))

(defn min?
  ; @param (vector) n
  ; @param (integer) x
  ;
  ; @example
  ;  (min? [:a :b] 3)
  ;  =>
  ;  false
  ;
  ; @example
  ;  (min? [:a :b :c] 3)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n x]
  (and (nonempty? n)
       (>= (count n) x)))

(defn max?
  ; @param (vector) n
  ; @param (integer) x
  ;
  ; @example
  ;  (max? [:a :b :c] 3)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (max? [:a :b :c :d] 3)
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n x]
  (and (nonempty? n)
       (<= (count n) x)))

(defn longer?
  ; @param (vector) a
  ; @param (vector) b
  ;
  ; @example
  ;  (longer? [:a :b :c] [:a :b :c :d])
  ;  =>
  ;  true
  ;
  ; @example
  ;  (longer? [:a :b :c :d] [:a :b])
  ;  =>
  ;  false
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
  ;  (reverse-items [:a :b :c])
  ;  =>
  ;  [:c :b :a]
  ;
  ; @return (vector)
  [n]
  (-> n reverse vec))

(defn repeat-item
  ; @param (*) n
  ; @param (integer) x
  ;
  ; @example
  ;  (repeat-item :a 5)
  ;  =>
  ;  [:a :a :a :a :a]
  ;
  ; @return (vector)
  [n x]
  (vec (repeat x n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cons-item
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (cons-item [:a :b] :c)
  ;  =>
  ;  [:c :a :b]
  ;
  ; @return (vector)
  [n x]
  (vec (cons x n)))

(defn cons-item-once
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (cons-item-once [:a :b] :b)
  ;  =>
  ;  [:a :b]
  ;
  ; @example
  ;  (cons-item-once [:a :b] :c)
  ;  =>
  ;  [:c :a :b]
  ;
  ; @return (vector)
  [n x]
  (if (contains-item? n x)
      (return         n)
      (cons-item      n x)))

(defn conj-item
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (conj-item [:a :b] :c)
  ;  =>
  ;  [:a :b :c]
  ;
  ; @example
  ;  (conj-item [:a :b] :c :d)
  ;  =>
  ;  [:a :b :c :d]
  ;
  ; @return (vector)
  [n & xyz]
  (vec (apply conj n xyz)))

(defn conj-item-once
  ; Conj item to vector if vector does not contains it.
  ;
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (conj-item-once [:a :b] :b)
  ;  =>
  ;  [:a :b]
  ;
  ; @example
  ;  (conj-item-once [:a :b] :c)
  ;  =>
  ;  [:a :b :c]
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
  ;  (conj-some [:a :b] :c)
  ;  =>
  ;  [:a :b :c]
  ;
  ; @example
  ;  (conj-some [:a :b] nil)
  ;  =>
  ;  [:a :b]
  ;
  ; @return (vector)
  [n x]
  (if x (conj-item n x)
        (return    n)))

(defn concat-items
  ; @param (list of vectors) abc
  ;
  ; @usage
  ;  (concat-items [:a :b] [:c :d] [:e :f])
  ;
  ; @return (vector)
  [& abc]
  (vec (apply concat abc)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dex-range
  ; @param (vector) n
  ;
  ; @example
  ;  (dex-range [:a :b :c])
  ;  =>
  ;  [0 1 2]
  ;
  ; @return (vector)
  [n]
  (-> n count range vec))

(defn dex-first?
  ; @param (integer) dex
  ;
  ; @usage
  ;  (dex-first? 1)
  ;
  ; @return (boolean)
  [dex]
  (= dex 0))

(defn dex-last?
  ; @param (vector) n
  ; @param (integer) dex
  ;
  ; @example
  ;  (dex-last? [:a :b :c] 2)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n dex]
  (= (inc   dex)
     (count n)))

(defn last-dex
  ; @param (vector) n
  ;
  ; @example
  ;  (last-dex [:a :b :c])
  ;  =>
  ;  2
  ;
  ; @return (integer)
  [n]
  (if (nonempty? n)
      (->        n count dec)))

(defn next-dex
  ; A vektor elemeinek szama alapjan meghatarozza, a dex utan kovetkezo indexet
  ;
  ; @param (vector) n
  ; @param (integer) dex
  ;
  ; @example
  ;  (next-dex [:a :b :c :d] 3)
  ;  =>
  ;  0
  ;
  ; @example
  ;  (next-dex [] 3)
  ;  =>
  ;  0
  ;
  ; @return (integer)
  [n dex]
  (if (nonempty?         n)
      (sequence/next-dex dex 0 (-> n count dec))
      (return            0)))

(defn inc-dex
  ; A vektor elemeinek szama alapjan meghatarozza, a dex utan kovetkezo indexet
  ; A legmagasabb erteknel megall
  ;
  ; @param (vector) n
  ; @param (integer) dex
  ;
  ; @example
  ;  (inc-dex [:a :b :c :d] 3)
  ;  =>
  ;  3
  ;
  ; @return (integer)
  [n dex]
  (if (dex-last? n dex)
      (return      dex)
      (inc         dex)))

(defn prev-dex
  ; A vektor elemeinek szama alapjan meghatarozza, a dex-et megelozo indexet
  ;
  ; @param (vector) n
  ; @param (integer) dex
  ;
  ; @example
  ;  (prev-dex [:a :b :c :d] 0)
  ;  =>
  ;  3
  ;
  ; @example
  ;  (prev-dex [] 3)
  ;  =>
  ;  0
  ;
  ; @return (integer)
  [n dex]
  (if (nonempty?         n)
      (sequence/prev-dex dex 0 (-> n count dec))
      (return            0)))

(defn dec-dex
  ; A vektor elemeinek szama alapjan meghatarozza, a dex-et megelozo indexet
  ; A legalacsonyabb erteknel (0) megall
  ;
  ; @param (vector) n
  ; @param (integer) dex
  ;
  ; @example
  ;  (dec-dex [:a :b :c :d] 0)
  ;  =>
  ;  0
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
  ;  (match-dex ["a" "b" "c"] 0)
  ;  =>
  ;  0
  ;
  ; @example
  ;  (match-dex ["a" "b" "c"] 1)
  ;  =>
  ;  1
  ;
  ; @example
  ;  (match-dex ["a" "b" "c"] 2)
  ;  =>
  ;  2
  ;
  ; @example
  ;  (match-dex ["a" "b" "c"] 3)
  ;  =>
  ;  0
  ;
  ; @example
  ;  (match-dex ["a" "b" "c"] 4)
  ;  =>
  ;  1
  ;
  ; @return (integer)
  [n dex]
  (let [n-count (count n)
        x       (-> dex (/ n-count)
                        (math/floor))
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
  ;  (last-item [:a :b :c])
  ;  =>
  ;  :c
  ;
  ; @return (*)
  [n]
  (last n))

(defn first-item
  ; @param (vector) n
  ;
  ; @example
  ;  (first-item [:a :b :c])
  ;  =>
  ;  :a
  ;
  ; @return (*)
  [n]
  (first n))

(defn item-last?
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (item-last? [:a :b] :b)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n x]
  (= (last n) x))

(defn item-first?
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (item-first? [:a :b] :a)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n x]
  (= (first n) x))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ranged-items
  ; @param (vector) n
  ; @param (integer) low
  ; @param (integer)(opt) high
  ;
  ; @example
  ;  (ranged-items [:a :b :c :d :e :f] 1 3)
  ;  =>
  ;  [:b :c]
  ;
  ; @example
  ;  (ranged-items [:a :b :c :d :e :f] 2)
  ;  =>
  ;  [:c :d :e :f]
  ;
  ; @return (vector)
  ([n low]
   (let [high (count n)]
        (ranged-items n low high)))

  ([n low high]
   (let [high (min high (count n))]
        (if (and (nonempty? n)
                 (<  low high)
                 (>= low 0))
            (subvec n low high)
            (return [])))))

(defn last-items
  ; @param (vector) n
  ; @param (integer) length
  ;
  ; @example
  ;  (last-items [:a :b :c :d :e] 2)
  ;  =>
  ;  [:d :e]
  ;
  ; @return (vector)
  [n length]
  (if (<= (count n) length)
      (return n)
      (subvec n (-> n count (- length)))))

(defn first-items
  ; @param (vector) n
  ; @param (integer) length
  ;
  ; @example
  ;  (first-items [:a :b :c :d :e] 3)
  ;  =>
  ;  [:a :b :c]
  ;
  ; @return (vector)
  [n length]
  (if (and (integer?     length)
           (<= (count n) length))
      (return n)
      (subvec n 0 length)))

(def trim first-items)

(defn shift-first-item
  ; @param (vector) n
  ;
  ; @example
  ;  (shift-first-item [:a :b :c :d :e])
  ;  =>
  ;  [:b :c :d :e]
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
  ;  (pop-last-item [:a :b :c :d :e])
  ;  =>
  ;  [:a :b :c :d]
  ;
  ; @return (vector)
  [n]
  (if (nonempty? n)
      (subvec    n 0 (-> n count dec))
      (return    [])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn count?
  ; @param (vector) n
  ; @param (integer) x
  ;
  ; @example
  ;  (count? [:a :b :c] 3)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n x]
  (or (and      (nonempty? n)
           (= x (count     n)))
      (and (= n [])
           (= x 0))))

(defn count!
  ; @param (vector) n
  ; @param (integer) x
  ;
  ; @example
  ;  (count! [:a :b :c] 3)
  ;  =>
  ;  [:a :b :c]
  ;
  ; @example
  ;  (count! [:a :b :c] 2)
  ;  =>
  ;  [:a :b]
  ;
  ; @example
  ;  (count! [:a :b :c] 5)
  ;  =>
  ;  [:a :b :c nil nil]
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
  ;  (remove-item [:a :b] :b)
  ;  =>
  ;  [:a]
  ;
  ; @example
  ;  (remove-item [:a :b :a] :a)
  ;  =>
  ;  [:b]
  ;
  ; @return (vector)
  [n x]
  (vec (remove #(= % x) n)))

(defn remove-item-once
  ; @param (vector) n
  ; @param (*) dex
  ;
  ; @example
  ;  (remove-item-once [:a :b :b] :b)
  ;  =>
  ;  [:a :b]
  ;
  ; @example
  ;  (remove-item-once [:a :b :a] :a)
  ;  =>
  ;  [:b :a]
  ;
  ; @return (vector)
  [n x]
  (let [count (count n)]
       (letfn [(f [o dex]
                  (cond (= x (nth n dex))
                        (if (dex-last? n dex)
                            (subvec n 0 dex)
                            (concat (subvec n 0 dex)
                                    (subvec n (dec dex) count)))
                        (dex-last? n dex) (return n)
                        :return (f o (inc dex))))]
              (if (nonempty? n)
                  (f [] 0)
                  (return n)))))

(defn remove-nth-item
  ; @param (vector) n
  ; @param (integer) dex
  ;
  ; @example
  ;  (remove-nth-item [:a :b :c :d :e] 2)
  ;  =>
  ;  [:a :b :d :e]
  ;
  ; @return (vector)
  [n dex]
  (concat-items (ranged-items n 0 dex)
                (ranged-items n (inc dex))))

(defn remove-nth-items
  ; @param (vector) n
  ; @param (integers in vector) dexes
  ;
  ; @example
  ;  (remove-nth-item [:a :b :c :d :e] [0 2])
  ;  =>
  ;  [:b :d :e]
  ;
  ; @return (vector)
  [n dexes]
  (letfn [(remove-nth-items-f [o dex x]
                              (if (contains-item? dexes dex)
                                  (return o)
                                  (conj   o x)))]
         (reduce-kv remove-nth-items-f [] n)))

(defn remove-items
  ; @param (vector) n
  ; @param (vector) xyz
  ;
  ; @example
  ;  (remove-items [:a :b :c] [:b :c])
  ;  =>
  ;  [:a]
  ;
  ; @example
  ;  (remove-items [:a :b :b :c ] [:b :c])
  ;  =>
  ;  [:a]
  ;
  ; @return (vector)
  [n xyz]
  (letfn [(remove-items-f [o x]
                          (if (contains-item? xyz x)
                              (return o)
                              (conj   o x)))]
         (reduce remove-items-f [] n)))

(defn remove-items-by
  ; @param (vector) n
  ; @param (function) f
  ;
  ; @example
  ;  (remove-items-by [:a :b :c] keyword?)
  ;  =>
  ;  []
  ;
  ; @return (vector)
  [n f]
  (letfn [(remove-items-f [o x]
                          (if (f      x)
                              (return o)
                              (conj   o x)))]
         (reduce remove-items-f [] n)))

(defn difference
  ; @param (vector) a
  ; @param (vector) b
  ;
  ; @example
  ;  (difference [:a :b :c] [:b :c])
  ;  =>
  ;  [:a]
  ;
  ; @return (vector)
  [a b]
  (remove-items a b))

(defn keep-items
  ; @param (vector) n
  ; @param (vector) xyz
  ;
  ; @example
  ;  (keep-items [:a :b :c :d] [:b :c])
  ;  =>
  ;  [:b :c]
  ;
  ; @return (vector)
  [n xyz]
  (letfn [(keep-items-f [o x]
                        (if (contains-item? xyz x)
                            (conj   o x)
                            (return o)))]
         (reduce keep-items-f [] n)))

(defn remove-items-kv
  ; @param (maps in vector) n
  ; @param (*) k
  ; @param (*) v
  ;
  ; @example
  ;  (remove-items-kv [{:a "a"} {:b "b"} {:c "c"}] :b "b")
  ;  =>
  ;  [{:a "a"} {:c "c"}]
  ;
  ; @return (maps in vector)
  [n k v]
  (vec (remove #(= (k %) v) n)))

(defn remove-last-item
  ; @param (vector) n
  ;
  ; @usage
  ;  (remove-last-item [:a :b :c])
  ;
  ; @return (vector)
  [n]
  (-> n drop-last vec))

(defn remove-first-item
  ; @param (vector) n
  ;
  ; @usage
  ;  (remove-first-item [:a :b :c])
  ;
  ; @return (vector)
  [n]
  (vec (drop 1 n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicate-nth-item
  ; @param (vector) n
  ; @param (integer) dex
  ;
  ; @example
  ;  (duplicate-nth-item [:a :b :c :d :e] 2)
  ;  =>
  ;  [:a :b :c :c :d :e]
  ;
  ; @return (vector)
  [n dex]
  (letfn [(f [result item-dex item]
             (if (= item-dex dex)
                 (conj result item item)
                 (conj result item)))]
         (reduce-kv f [] n)))

(defn duplicate-nth-items
  [])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn concat-items-once
  ; @param (list of vectors) abc
  ;
  ; @example
  ;  (concat-items-once [:a :b :c] [:c :d :e :a])
  ;  =>
  ;  [:b :c :d :e :a]
  ;
  ; @return (vector)
  [& abc]
  (-> (apply concat abc) set vec))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn move-item
  ; @param (vector) n
  ; @param (integer) from
  ; @param (integer) to
  ;
  ; @example
  ;  (move-item [:a :b :c :d :e :f :g :h] 2 2)
  ;  =>
  ;  [:a :b :c :d :e :f :g :h]
  ;
  ; @example
  ;  (move-item [:a :b :c :d :e :f :g :h] 2 5)
  ;  =>
  ;  [:a :b :d :e :c :f :g :h]
  ;
  ; @example
  ;  (move-item [:a :b :c :d :e :f :g :h] 5 2)
  ;  =>
  ;  [:a :b :f :c :d :e :g :h]
  ;
  ; @return (vector)
  [n from to]
  (if (dex-in-bounds? n from)
      (let [to (math/between! to 0 (count n))]
           (cond ; Stay in place
                 (= from to) (return n)
                 ; Move item fwd
                 (< from to) (concat-items (ranged-items n 0 from)
                                           (ranged-items n (inc from) (inc to))
                                           (ranged-items n from (inc from))
                                           (ranged-items n (inc to)))
                 ; Move item bwd
                 (> from to) (concat-items (ranged-items n 0 to)
                                           (ranged-items n from (inc from))
                                           (ranged-items n to from)
                                           (ranged-items n (inc from)))))
      (return n)))

(defn move-item-to-last
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (move-item-to-last [:a :b] :a)
  ;  =>
  ;  [:b :a]
  ;
  ; @example
  ;  (move-item-to-last [:b] :a)
  ;  =>
  ;  [:b :a]
  ;
  ; @return (vector)
  [n x]
  (conj (remove-item n x) x))

(defn move-item-to-first
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (move-item-to-first [:a :b] :b)
  ;  =>
  ;  [:b :a]
  ;
  ; @example
  ;  (move-item-to-first [:a] :b)
  ;  =>
  ;  [:b :a]
  ;
  ; @return (vector)
  [n x]
  (vec (cons x (remove-item n x))))

(defn change-item
  ; @param (vector) n
  ; @param (*) a
  ; @param (*) b
  ;
  ; @example
  ;  (change-item [:a :b :c :d :c] :c :x)
  ;  =>
  ;  [:a :b :x :d :x]
  ;
  ; @return (vector)
  [n a b]
  (letfn [(change-item-f [o x]
                         (if (= x a)
                             (conj-item o b)
                             (conj-item o x)))]
         (reduce change-item-f [] n)))

(defn replace-nth-item
  ; @param (vector) n
  ; @param (integer) dex
  ; @param (*) x
  ;
  ; @example
  ;  (replace-nth-item [:a :b :c :d] 1 :x)
  ;  =>
  ;  [:a :x :c :d]
  ;
  ; @example
  ;  (replace-nth-item [:a :b :c :d] 999 :x)
  ;  =>
  ;  [:a :b :c :d]
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
  ;  (inject-item [:a :b :c] 2 :d)
  ;  =>
  ;  [:a :b :d :c]
  ;
  ; @example
  ;  (inject-item [:a :b :c] 999 :d)
  ;  =>
  ;  [:a :b :d :c]
  ;
  ; @example
  ;  (inject-item nil 999 :d)
  ;  =>
  ;  [:d]
  ;
  ; @example
  ;  (inject-item {:a "b"} 1 :d)
  ;  =>
  ;  {:a "b"}
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
        :return n))

(defn toggle-item
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (toggle-item [:a :b] :c)
  ;  =>
  ;  [:a :b :c]
  ;
  ; @example
  ;  (toggle-item [:a :b :c] :c)
  ;  =>
  ;  [:a :b]
  ;
  ; @return (vector)
  [n x]
  (if (contains-item? n x)
      (remove-item    n x)
      (conj-item      n x)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-duplicates
  ; @param (vector) n
  ;
  ; @usage
  ;  (remove-duplicates [:a :b :c :a])
  ;
  ; @return (vector)
  [n]
  (letfn [(remove-duplicates-f [o x]
                               (if (contains-item? o x)
                                   (return         o)
                                   (conj-item      o x)))]
         (reduce remove-duplicates-f [] n)))

(defn similars
  ; @param (vector) a
  ; @param (vector) b
  ;
  ; @example
  ;  (similars [:a :b :c] [:c :d :e])
  ;  =>
  ;  [:c]
  ;
  ; @return (vector)
  [a b]
  (letfn [(similars-f [o x]
                      (if (contains-item? b x)
                          (conj-item      o x)
                          (return         o)))]
         (reduce similars-f [] a)))

(defn contains-similars?
  ; @param (vector) a
  ; @param (vector) b
  ;
  ; @example
  ;  (contains-similars? [:a :b :c] [:c :d :e])
  ;  =>
  ;  true
  ;
  ; @example
  ;  (contains-similars? [:a :b :c] [:d :e :f])
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [a b]
  (-> a (similars b)
        (empty?)
        (not)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-last-dex
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (item-last-dex [:a :b :a :b] :b)
  ;  =>
  ;  3
  ;
  ; @return (nil or integer)
  [n x]
  (when (and (vector?        n)
             (contains-item? n x))
        (reduce-kv #(if (= %3 x) %2 %1) nil n)))

(defn item-first-dex
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (item-first-dex [:a :b :a :b] :b)
  ;  =>
  ;  1
  ;
  ; @return (nil or integer)
  [n x]
  (when (and (vector?        n)
             (contains-item? n x))
        (some-indexed #(if (= %2 x) %1) n)))

(defn items-before-first-occurence
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (items-before-first-occurence [:a :b :c :d :d :e] :d)
  ;  =>
  ;  [:a :b :c]
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
  ;  (items-after-first-occurence [:a :b :c :d :d :e] :d)
  ;  =>
  ;  [:d :e]
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
  ;  (remove-first-occurence [:a :b :c :d :d :e] :d)
  ;  =>
  ;  [:a :b :c :d :e]
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
  ;  (move-first-occurence [:a :b :c :a :b :c] :b 3)
  ;  =>
  ;  [:a :c :b :a :b :c]
  ;
  ; @example
  ;  (move-first-occurence [:a :b :c :a :b :c] :b 1)
  ;  =>
  ;  [:a :b :c :a :b :c]
  ;
  ; @example
  ;  (move-first-occurence [:a :b :c :a :b :c] :b 20)
  ;  =>
  ;  [:a :c :a :b :c :b]
  ;
  ; @return (vector)
  [n x dex]
  (if-let [item-first-dex (item-first-dex n x)]
          (move-item n item-first-dex dex)
          (return    n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn prev-item
  ; A vektor elemei alapjan meghatarozza, az x-et megelozo elemet
  ;
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (prev-item [:a :b :c :d] :b)
  ;  =>
  ;  :a
  ;
  ; @example
  ;  (prev-item [:a :b :c :d] nil)
  ;  =>
  ;  :d
  ;
  ; @example
  ;  (prev-item [] :a)
  ;  =>
  ;  ?
  ;
  ; @return (*)
  [n x])

(defn next-item
  ; A vektor elemei alapjan meghatarozza, az x utan kovetkezo elemet
  ;
  ; @param (vector) n
  ; @param (*) x
  ;
  ; @example
  ;  (next-item [:a :b :c :d] :a)
  ;  =>
  ;  :b
  ;
  ; @example
  ;  (next-item [:a :b :c :d] nil)
  ;  =>
  ;  :a
  ;
  ; @example
  ;  (next-item [] :a)
  ;  =>
  ;  ?
  ;
  ; @return (*)
  [n x])
  ;(let [item-first-dex (item-first-dex n x)
  ;      next-item-dex  (next-dex       n item-first-dex)
  ;     (nth-item n next-item-dex)])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn filter-items
  ; @param (vector) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (filter-items [:a :b "c"] keyword?)
  ;  =>
  ;  [:a :b]
  ;
  ; @return (vector)
  [n test-f]
  (vec (filter test-f n)))

(defn first-filtered
  ; @param (vector) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (first-filtered ["a" :b "c" :d "e"] keyword?)
  ;  =>
  ;  :b
  ;
  ;  (first-filtered ["a" :b "c" :d "e"] #(string? %1))
  ;  =>
  ;  "a"
  ;
  ; @return (*)
  [n test-f]
  (some #(if (test-f %) %) n))

(defn last-filtered
  ; @param (vector) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (first-filtered ["a" :b "c" :d "e"] keyword?)
  ;  =>
  ;  :d
  ;
  ;  (first-filtered ["a" :b "c" :d "e"] #(string? %1))
  ;  =>
  ;  "e"
  ;
  ; @return (*)
  [n test-f]
  (reduce #(if (test-f %2) %2 %1) nil n))

(defn nth-filtered
  ; @param (vector) n
  ; @param (function) test-f
  ; @param (integer) dex
  ;
  ; @example
  ;  (nth-filtered ["a" :b "c" :d "e"] keyword? 2)
  ;  =>
  ;  :d
  ;
  ;  (nth-filtered ["a" :b "c" :d "e"] #(string? %1) 2)
  ;  =>
  ;  "c"
  ;
  ; @return (*)
  [n test-f dex]
  (letfn [(nth-filtered-f [x match-count f-dex]
                          (if (-> x test-f not)
                              ; If x is NOT matches ...
                              (if (dex-last? n f-dex)
                                  ; If NO more items in n ...
                                  (return nil)
                                  ; If more items in n ...
                                  (nth-filtered-f (get n (inc f-dex)) match-count (inc f-dex)))
                              ; If x is matches ...
                              (if (< match-count dex)
                                  ; If x is not the nth match ...
                                  (if (dex-last? n f-dex)
                                      ; If NO more items in n ...
                                      (return nil)
                                      ; If more items in n ...
                                      (nth-filtered-f (get n (inc f-dex)) (inc match-count) (inc f-dex)))
                                  ; If x is the nth match ...
                                  (return x))))]
         (nth-filtered-f (first n) 0 0)))

(defn filtered-count
  ; @param (vector) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (filtered-count [:a :b "c"] keyword?)
  ;  =>
  ;  2
  ;
  ; @return (integer)
  [n test-f]
  (count (filter test-f n)))

(defn filtered-count?
  ; @param (vector) n
  ; @param (function) test-f
  ; @param (integer) x
  ;
  ; @example
  ;  (filtered-count? [:a :b "c"] keyword? 2)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n test-f x]
  (= x (filtered-count n test-f)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn abc-items
  ; @param (vector) n
  ;
  ; @example
  ;  (abc-items [:b "b" :a "a" nil])
  ;  =>
  ;  [nil "a" "b" :a :b]
  ;
  ; @return (vector)
  [n]
  (letfn [(sort-item-f [o x] (cond (string?  x) (update o :string-items     conj x)
                                   (keyword? x) (update o :keyword-items    conj x)
                                   :return      (update o :unsortable-items conj x)))
          (sort-items-f [n] (reduce sort-item-f {} n))]
         (let [{:keys [string-items keyword-items unsortable-items]} (sort-items-f n)]
              ; (sort) cannot compare string to keyword!
              (vec (concat unsortable-items (sort string-items)
                                            (sort keyword-items))))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sort-items
  ; @param (vector) n
  ; @param (function)(opt) comparator-f
  ;
  ; @example
  ;  (sort-items ["a" "c" "b"] string/abc?)
  ;  =>
  ;  ["a" "b" "c"]
  ;
  ; @return (vector)
  ([n]
   (-> n sort vec))

  ([n comparator-f]
   (vec (sort comparator-f n))))

(defn items-sorted?
  ; @param (vector) n
  ; @param (function) comparator-f
  ;
  ; @example
  ;  (items-sorted? ["a" "c" "b"] string/abc?)
  ;  =>
  ;  false
  ;
  ; @example
  ;  (items-sorted? ["a" "b" "c"] string/abc?)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n comparator-f]
  (= n (sort-items n comparator-f)))

(defn sort-items-by
  ; @param (vector) n
  ; @param (function)(opt) comparator-f
  ; @param (function) value-f
  ;
  ; @example
  ;  (sort-items-by [{:a 3} {:a 2} {:a 1}] :a)
  ;  =>
  ;  [{:a 1} {:a 2} {:a 3}]
  ;
  ; @example
  ;  (sort-items-by [[1 2] [2 2] [2 3]] > first)
  ;  =>
  ;  [[2 2] [2 3] [1 2]]
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
  ;  (any-item-match? [:a :b :c] string?)
  ;  =>
  ;  false
  ;
  ; @example
  ;  (any-item-match? [:a "b" :c] string?)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n test-f]
  (boolean (some test-f n)))

(defn all-items-match?
  ; @param (vector) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (all-items-match? [:a "b" "c"] string?)
  ;  =>
  ;  false
  ;
  ; @example
  ;  (all-items-match? ["a" "b" "c"] string?)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n test-f]
  (every? test-f n))

(defn get-first-match-item
  ; @param (vector) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (get-first-match-item [:a :b :c] string?)
  ;  =>
  ;  nil
  ;
  ; @example
  ;  (get-first-match-item [:a "b" :c] string?)
  ;  =>
  ;  "b"
  ;
  ; @return (*)
  [n test-f]
  (some test-f n))

(defn get-first-match-item-dex
  ; @param (vector) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (get-first-match-item-dex [:a :b :c] string?)
  ;  =>
  ;  nil
  ;
  ; @example
  ;  (get-first-match-item-dex [:a "b" :c] string?)
  ;  =>
  ;  1
  ;
  ; @return (*)
  [n test-f]
  (some-indexed #(if (test-f %2) %1) n))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn compared-items-ordered?
  ; @param (vector) a
  ; @param (vector) b
  ; @param (function) comparator-f
  ;
  ; @example
  ;  (compared-items-ordered? [0 1 3] [0 1 2] <)
  ;  =>
  ;  false
  ;
  ; @example
  ;  (compared-items-ordered? [0 1 3] [0 1 4] <)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (compared-items-ordered? [0 1 3] [0 1 3] <)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (compared-items-ordered? [] [] <)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (compared-items-ordered? ["a" "b" "c"] ["d" "a"] string/abc?)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (compared-items-ordered? ["a" "b" "c"] ["a" "b"] string/abc?)
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [a b comparator-f]
  ; A compared-items-ordered? függvény összehasonlítja a és b vektor azonos indexű elemeit. ...
  ; ... Az első alkalommal, amikor két összehasonlított elem nem egyezik, visszatér
  ;     a (comparator-f x y) függvény kimenetével.
  ; ... Ha a és b vektor elemei és az elemek száma megegyeznek, a visszatérési érték true!
  ; ... Ha a vektor elemeinek száma és b vektor elemeinek száma nem egyenlő,
  ;     akkor az elemek összehasonlítása az alacsonyabb elemszámig történik!
  ;     (Ha az összehasonlított elemek megegyeznek, akkor a kisebb elemszámú vektor számít
  ;      a sorrendben hamarabbinak!)
  (let [max-count (min (count a) (count b))]
       (letfn [(compared-items-ordered-f [dex]
                                         (let [x (get a dex)
                                               y (get b dex)]
                                              (if (= x y)
                                                  ; If the compared items are equal ...
                                                  (if (= (inc dex) max-count)
                                                      ; If NO more items to compare (and NO difference found)...
                                                      (<= (count a) (count b))
                                                      ; If more items to compare ...
                                                      (compared-items-ordered-f (inc dex)))
                                                  ; If the compared items are NOT equal ...
                                                  (comparator-f x y))))]
              ; Ha a max-count értéke 0, akkor mind a két vektor üres.
              (case max-count 0 (return true)
                                (compared-items-ordered-f 0)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ->items
  ; @param (map) n
  ; @param (function) update-f
  ;
  ; @example
  ;  (->items [:a :b :c] name)
  ;  =>
  ;  ["a" "b" "c"]
  ;
  ; @return (vector)
  [n update-f]
  (reduce #(conj %1 (update-f %2)) [] n))

(defn ->>items
  ; @param (map) n
  ; @param (function) update-f
  ;
  ; @example
  ;  (->>items [:a :b :c [:d :e {:e :f}]] name)
  ;  =>
  ;  ["a" "b" "c" ["d" "e" {:e "f"}]]
  ;
  ; @return (vector)
  [n update-f]
  ; A rekurzió a térképek értékein is végrehajtja az update-f függvényt, mivel azok a vektorok elemeinek megfelelői!
  (letfn [(f [n] (cond (vector? n) (reduce    #(conj  %1    (f %2)) [] n)
                       (map?    n) (reduce-kv #(assoc %1 %2 (f %3)) {} n)
                       :return     (update-f n)))]
         (f n)))
