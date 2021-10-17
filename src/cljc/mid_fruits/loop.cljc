
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.10
; Description:
; Version: v0.8.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.loop
    (:require [mid-fruits.candy :refer [param return]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reduce+wormhole
  ; A reduce ciklus - a téridő különböző idősíkjaiban létező - iterációit egy
  ; féregjárattal köti össze, így egy-egy iterációból - a kimenettől független -
  ; adat juthat tovább a következő iterációba
  ;
  ; @param (function) f
  ; @param (*) initial
  ; @param (collection) coll
  ; @param (*) wormhole
  ;  A féregjárat kezdeti értéke
  ;
  ; Az f függvény visszatérési értéke egy vektor. Ennek a vektornak az első
  ; eleme az f függvény kimenete (ami a hagyományos reduce ciklusnál
  ; f függvény visszatérési értéke). A vektor második eleme pedig a féregjárat
  ; amit a soron következő iteráció a harmadik paramétereként fog majd megkapni
  ;
  ; @example
  ;  (reduce+wormhole (fn [%1 %2 %3]
  ;                       [(assoc %1 %2 %3)
  ;                        (inc %3)])
  ;                   {}
  ;                   [:a :b :c :d]
  ;                   10
  ;  => {:a 10 :b 11 :c 12 :d 13}
  ;
  ; @return (*)
  [f initial coll wormhole]
  (letfn
    [(index-out-of-bounds? [lap] (= (count coll) lap))
     (reduce+wormhole [[result wormhole] lap]
                      (if (index-out-of-bounds? lap)
                          (return result)
                          (reduce+wormhole (f result (nth coll lap) wormhole)
                                           (inc lap))))]
    ; reduce+wormhole
    (reduce+wormhole [initial wormhole] 0)))

(defn do-reduce-while
  ; @param (function) f
  ; @param (*) initial
  ; @param (collection) coll
  ; @param (*) test-f
  ;  A teszt-függvény, aminek ha igaz a kimenete, akkor a ciklus megáll
  ;
  ; @example
  ;  (do-reduce-while (fn [%1 %2] (conj %1 %2))
  ;                   [:a]
  ;                   [:b :c :d :e]
  ;                   (fn [%1 _ %3] (= (count %1) 3)))
  ;  => [:a :b :c :d]
  ;
  ; @return (*)
  [f initial coll test-f]
  (letfn
    [(index-out-of-bounds? [lap] (= (count coll) lap))
     (do-reduce-while [result lap]
                      (if (index-out-of-bounds? lap)
                          (return result)
                          (let [v (nth coll lap)]
                               (if (test-f result v)
                                   ; BUG#3451

                                   (f result v)
                                   (do-reduce-while (f result v)
                                                    (inc lap))))))]
    ; do-reduce-while
    (do-reduce-while initial 0)))

(defn reduce-while
  ; @param (function) f
  ; @param (*) initial
  ; @param (collection) coll
  ; @param (*) test-f
  ;  A teszt-függvény, aminek ha igaz a kimenete, akkor a ciklus megáll
  ;
  ; @example
  ;  (reduce-while (fn [%1 %2] (conj %1 %2))
  ;                [:a]
  ;                [:b :c :d :e]
  ;                (fn [%1 _] (= (count %1) 3)))
  ;  => [:a :b :c]
  ;
  ; @return (*)
  [f initial coll test-f]
  (letfn
    [(index-out-of-bounds? [lap] (= (count coll)
                                    (param lap)))
     (reduce-while [result lap]
                   (if (index-out-of-bounds? lap)
                       (return result)
                       (let [v (nth coll lap)]
                            (if (test-f result v)

                                ; A) (test-f result v)
                                ; B) (apply test-f [result v])
                                ;
                                ; BUG#3451
                                ; Egyik megoldás sem támogatja a test-f
                                ; #() rövid furmulával való használatát,
                                ; ha a test-f nem 2 db paramétert használ

                                (return result)
                                (reduce-while (f result v)
                                              (inc lap))))))]
    ; reduce-while
    (reduce-while initial 0)))

(defn reduce-while-indexed
  ; @param (function) f
  ; @param (*) initial
  ; @param (collection) coll
  ; @param (*) test-f
  ;  A teszt-függvény, aminek ha igaz a kimenete, akkor a ciklus megáll
  ;
  ; @example
  ;  (reduce-while-indexed (fn [%1 %2 dex] (conj %1 %2))
  ;                        [:a]
  ;                        [:b :c :d :e]
  ;                        (fn [%1 _ dex] (= (count %1) 3)))
  ;  => [:a :b :c]
  ;
  ; @return (*)
  [f initial coll test-f]
  (letfn
    [(index-out-of-bounds? [lap] (= (count coll)
                                    (param lap)))
     (reduce-while-indexed [result lap]
                           (if (index-out-of-bounds? lap)
                               (return result)
                               (let [v (nth coll lap)]
                                    (if (test-f result v lap)
                                        ; BUG#3451
                                        (return result)
                                        (reduce-while-indexed (f result v lap)
                                                              (inc lap))))))]
    ; reduce-while-indexed
    (reduce-while-indexed initial 0)))

(defn do-reduce-kv-while
  ; @param (function) f
  ; @param (*) initial
  ; @param (map) map
  ; @param (*) test-f
  ;  A teszt-függvény, aminek ha igaz a kimenete, akkor a ciklus megáll
  ;
  ; @example
  ;  (do-reduce-kv-while (fn [%1 %2 %3] (assoc %1 %2 %3))
  ;                      {:a "a"}
  ;                      {:b "b" :c "c" :d "d" :e "e"}
  ;                      (fn [%1 _ _] (= (count %1) 3)))
  ;  => {:a "a" :b "b" :c "c" :d "d"}
  ;
  ; XXX#7500
  ; TODO, WARNING
  ; Clojure környezetben az f és test-f függvényeket csak akkor lehet
  ; #() formában használni, ha pontosan annyi argumentumot használnak,
  ; mint amennyit a reduce-kv-while függvény átad nekik (f return k v)
  ;
  ; Ezt nem szereti:
  ; (do-reduce-kv-while #(conj %1 "a") ...)
  ;
  ; Ezt szereti:
  ; (do-reduce-kv-while (fn [%1 %2 %3] (conj %1 "a")) ...)
  ;
  ; @return (*)
  [f initial map test-f]
  (let [keys (keys map)]
       (letfn
         [(index-out-of-bounds? [lap] (= (count keys) lap))
          (do-reduce-kv-while [result lap]
                              (if (index-out-of-bounds? lap)
                                  (return result)
                                  (let [k (nth keys lap)
                                        v (get map k)]
                                       (if (test-f result k v)
                                           ; BUG#3451

                                           (f result k v)
                                           (do-reduce-kv-while (f result k v)
                                                               (inc lap))))))]
         ; do-reduce-kv-while
         (do-reduce-kv-while initial 0))))

(defn reduce-kv-while
  ; @param (function) f
  ; @param (*) initial
  ; @param (map) map
  ; @param (*) test-f
  ;  A teszt-függvény, aminek ha igaz a kimenete, akkor a ciklus megáll
  ;
  ; @example
  ;  (reduce-kv-while (fn [%1 %2 %3] (assoc %1 %2 %3))
  ;                   {:a "a"}
  ;                   {:b "b" :c "c" :d "d" :e "e"}
  ;                   (fn [%1 _ _] (= (count %1) 3)))
  ;  => {:a "a" :b "b" :c "c" :d "d"}
  ;
  ; XXX#7500
  ;
  ; @return (*)
  [f initial map test-f]
  (let [keys (keys map)]
       (letfn
         [(index-out-of-bounds? [lap] (= (count keys) lap))
          (reduce-kv-while [result lap]
                          (if (index-out-of-bounds? lap) result
                                (let [k (nth keys lap)
                                      v (get map k)]
                                     (if (test-f result k v)
                                         (return result)
                                         (reduce-kv-while (f result k v)
                                                          (inc lap))))))]
         ; reduce-kv-while
         (reduce-kv-while initial 0))))

(defn reduce-kv-indexed
  ; Az f függvény harmadik paraméterként megkapja az aktuális ciklus számát
  ;
  ; @param (function) f
  ; @param (*) initial
  ; @param (map) map
  ;
  ; @usage
  ;  (reduce-kv-indexed (fn [result k v dex]
  ;                     nil
  ;                     {})
  ;
  ; @return (*)
  [f initial map]
  (first (reduce-kv (fn [%1 %2 %3]
                        [(f (first %1) %2 %3 (second %1))
                         (inc (second %1))])
                    [initial 0]
                    (param map))))

(defn reduce-indexed
  ; Az f függvény harmadik paraméterként megkapja az aktuális ciklus számát
  ;
  ; @param (function) f
  ; @param (*) initial
  ; @param (collection) coll
  ;
  ; @usage
  ;  (reduce-indexed (fn [result v dex]
  ;                  nil
  ;                  [:a :b])
  ;
  ; @return (*)
  [f initial coll]
  (first (reduce (fn [%1 %2]
                     [(f (first %1) %2 (second %1))
                      (inc (second %1))])
                 [initial 0]
                 (param coll))))

(defn reduce-kv+first?+last?
  ; Az f függvény negyedik és ötödik paraméterként megkapja, hogy az aktuális
  ; ciklus az első illetve az utolsó-e?
  ;
  ; @param (function) f
  ; @param (*) initial
  ; @param (map) map
  ;
  ; @usage
  ;  (reduce-kv+first?+last? (fn [result k v first? last?])
  ;                          nil
  ;                          {})
  ;
  ; @return (*)
  [f initial map]
  (first (reduce-kv (fn [%1 %2 %3]
                        [(f (first %1) %2 %3
                            (= (second %1) 0)
                            (= (count map) (inc (second %1))))
                         (inc (second %1))])
                    [initial 0]
                    (param map))))

(defn reduce-kv+first?
  ; Az f függvény negyedik paraméterként megkapja, hogy az aktuális
  ; ciklus az első-e?
  ;
  ; @param (function) f
  ; @param (*) initial
  ; @param (map) map
  ;
  ; @usage
  ;  (reduce-kv+first? (fn [result k v first?])
  ;                    nil
  ;                    {})
  ;
  ; @return (*)
  [f initial map]
  (first (reduce-kv (fn [%1 %2 %3]
                        [(f (first %1) %2 %3
                            (= (second %1) 0))
                         (inc (second %1))])
                    [initial 0]
                    (param map))))

(defn reduce-kv+last?
  ; Az f függvény negyedik paraméterként megkapja, hogy az aktuális
  ; ciklus az utolsó-e?
  ;
  ; @param (function) f
  ; @param (*) initial
  ; @param (map) map
  ;
  ; @usage
  ;  (reduce-kv+last? (fn [result k v last?])
  ;                   nil
  ;                   {})
  ;
  ; @return (*)
  [f initial map]
  (first (reduce-kv (fn [%1 %2 %3]
                        [(f (first %1) %2 %3
                            (= (count map) (inc (second %1))))
                         (inc (second %1))])
                    [initial 0]
                    (param map))))

(defn reduce+first?+last?
  ; Az f függvény harmadik és negyedik paraméterként megkapja, hogy az aktuális
  ; ciklus az első illetve az utolsó-e?
  ;
  ; @param (function) f
  ; @param (*) initial
  ; @param (collection) coll
  ;
  ; @usage
  ;  (reduce+first?+last? (fn [result v first? last?])
  ;                       nil
  ;                       [:a :b])
  ;
  ; @return (*)
  [f initial coll]
  (first (reduce (fn [%1 %2]
                     [(f (first %1) %2
                         (= (second %1) 0)
                         (= (count coll) (inc (second %1))))
                      (inc (second %1))])
                 [initial 0]
                 (param coll))))

(defn reduce+first?
  ; Az f függvény harmadik paraméterként megkapja, hogy az aktuális ciklus
  ; az első-e?
  ;
  ; @param (function) f
  ; @param (*) initial
  ; @param (collection) coll
  ;
  ; @usage
  ;  (reduce+first? (fn [result v first?])
  ;                 nil
  ;                 [:a :b])
  ;
  ; @return (*)
  [f initial coll]
  (first (reduce (fn [%1 %2]
                     [(f (first %1) %2
                         (= (second %1) 0))
                      (inc (second %1))])
                 [initial 0]
                 (param coll))))

(defn reduce+last?
  ; Az f függvény harmadik paraméterként megkapja, hogy az aktuális ciklus
  ; az utolsó-e?
  ;
  ; @param (function) f
  ; @param (*) initial
  ; @param (collection) coll
  ;
  ; @usage
  ;  (reduce+last? (fn [result v last?])
  ;                nil
  ;                [:a :b])
  ;
  ; @return (*)
  [f initial coll]
  (first (reduce (fn [%1 %2]
                     [(f (first %1) %2
                         (= (count coll) (inc (second %1))))
                      (inc (second %1))])
                 [initial 0]
                 (param coll))))
 
(defn reduce-maptor
  ; A reduce ciklus maptor típuson végigiteráló változata
  ;
  ; @param (function) f
  ; @param (*) initial
  ; @param (maptor) maptor
  ;
  ; @usage
  ;  (reduce-maptor (fn [result k v])
  ;                 nil
  ;                 [[:a :b]])
  ;
  ; @return (*)
  [f initial maptor]
  (reduce (fn [%1 [%2 %3]]
              (f %1 %2 %3))
          (param initial)
          (param maptor)))

(defn reduce-maptor-indexed
  ; A reduce ciklus maptor típuson végigiteráló változata, ahol az f függvény
  ; negyedik paraméterként megkapja az aktuális ciklus számát
  ;
  ; @param (function) f
  ; @param (*) initial
  ; @param (maptor) maptor
  ;
  ; @usage
  ;  (reduce-maptor-indexed (fn [result k v dex]
  ;                         nil
  ;                         [[:a :b]])
  ;
  ; @return (*)
  [f initial maptor]
  (first (reduce-kv (fn [%1 [%2 %3]]
                        [(f (first %1) %2 %3 (second %1))
                         (inc (second %1))])
                    [initial 0]
                    (param maptor))))

(defn do-while
  ; @param (function) f
  ; @param (*) n
  ;  Az f függvény első paramétere
  ; @param (function) test-f
  ;  A teszt-függvény, aminek ha igaz a kimenete, akkor a ciklus megáll
  ;
  ; @example
  ;  (do-while (fn [{:keys [my-numbers x] :as n}]
  ;                (if (vector/contains-item? my-numbers x))
  ;                    (assoc  n :x (inc x))
  ;                    (update n :my-numbers vector/conj-item x))
  ;            {:my-numbers [0 1 2 4]
  ;             :x 0}
  ;            (fn [%] (= (count (:my-numbers %1)) 5))))
  ;  => {:my-numbers [0 1 2 4 3] :x 3}
  ;
  ; @return (*)
  [f n test-f]
  (let [result (f n)]
       (if (test-f result)
           (return result)
           (do-while f result test-f))))
