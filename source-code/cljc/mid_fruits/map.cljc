
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.10
; Description:
; Version: v1.3.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.map
    (:require [clojure.data     :as data]
              [mid-fruits.candy :refer [param return]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn to-vector
  ; @param (map) n
  ;
  ; @example
  ;  (map/map->vector {0 :x 1 :y 2 :z})
  ;  =>
  ;  [:x :y :z]
  ;
  ; @return (vector)
  [n]
  (reduce-kv #(conj %1 %3) [] n))

(defn nonempty?
  ; @param (map) n
  ;
  ; @usage
  ;  (map/nonempty? {})
  ;
  ; @return (boolean)
  [n]
  (and      (map?   n)
       (not (empty? n))))

(defn get-keys
  ; @param (map) n
  ;
  ; @example
  ;  (map/get-keys {:a {:b "a/b"}})
  ;  =>
  ;  [:a]
  ;
  ; @return (vector)
  [n]
  (vec (keys n)))

(defn get-first-key
  ; @param (map) n
  ;
  ; @example
  ;  (map/get-first-key {:a {:b "a/b"} :c "xyz"})
  ;  =>
  ;  :a
  ;
  ; @return (*)
  [n]
  (first (keys n)))

(defn get-values
  ; @param (map) n
  ;
  ; @usage
  ;  (map/get-values {:a {:b "a/b"}})
  ;
  ; @return (vector)
  [n]
  (vec (vals n)))

(defn get-first-value
  ; WARNING! Clojure maps are an unordered data structure.
  ;
  ; @param (map) n
  ;
  ; @example
  ;  (map/get-first-value {:a "abc" :c "xyz"})
  ;  =>
  ;  "abc"
  ;
  ; @return (*)
  [n]
  (let [values (vals n)]
       (first values)))

(defn assoc-ns
  ; @param (map) n
  ; @param (keyword) k
  ; @param (*) v
  ;
  ; @example
  ;  (map/assoc-ns {:fruit/apple "red"} :banana "yellow")
  ;  =>
  ;  {:fruit/apple "red" :fruit/banana "yellow"}
  ;
  ; @return (map)
  [n k v])
  ; TODO ...

(defn assoc-in-ns
  ; @param (map) n
  ; @param (vector) ks
  ; @param (*) v
  ;
  ; @return (map)
  [n ks v])
  ; TODO ...

(defn deep-merge
  ; @param (map) n
  ; @param (list of maps) xyz
  ;
  ; @usage
  ;  (deep-merge {:a {:b "a/b"}} {:c {:d "c/d"}})
  ;
  ; @usage
  ;  (map/deep-merge {:a {:b "a/b"}} {:c {:d "c/d"}})
  ;
  ; @return (*)
  [n & xyz]
  (letfn [(deep-merge-f [o x]
                        (if (and (map? o)
                                 (map? x))
                            (merge-with deep-merge-f o x)
                            (return x)))]
         (if (some         identity xyz)
             (reduce deep-merge-f n xyz)
             (return n))))

(defn reverse-merge
  ; @param (list of maps) xyz
  ;
  ; @example
  ;  (map/reverse-merge {:a "1"} {:a "2"})
  ;  =>
  ;  {:a "1"}
  ;
  ; @example
  ;  (map/reverse-merge {:a "1"} {:a "2"} {:a "3"})
  ;  =>
  ;  {:a "1"}
  ;
  ; @return (map)
  [& xyz]
  (apply merge (reverse xyz)))

(defn remove-items
  ; @param (map) n
  ; @param (vector) keys
  ;
  ; @example
  ;  (map/remove-items {:a "A" :b "B" :c "C"} [:a :c])
  ;  =>
  ;  {:b "B"}
  ;
  ; @return (map)
  [n keys]
  (reduce dissoc n keys))

(defn difference
  ; @param (map) a
  ; @param (map) b
  ;
  ; @example
  ;  (map/difference {:a "a" :b "b"} {:a "a"})
  ;  =>
  ;  {:b "b"}
  ;
  ; @return (map)
  ;  Things only in a
  [a b]
  (first (data/diff a b)))

(defn swap
  ; @param (map) n
  ;
  ; @example
  ;  (map/swap {:a "a" :b "b"})
  ;  =>
  ;  {"a" :a "b" :b}
  ;
  ; @return (map)
  [n]
  (zipmap (vals n)
          (keys n)))

(defn dissoc-in
  ; Original: re-frame.utils/dissoc-in
  ;
  ; Dissociates an entry from a nested associative structure returning a new
  ; nested structure. keys is a sequence of keys. Any empty maps that result
  ; will not be present in the new structure.
  ; The key thing is that 'm' remains identical? to istelf if the path was
  ; never present
  ;
  ; @param (map) n
  ; @param (vector) value-path
  ;
  ; @usage
  ;  (dissoc-in {:a {:b "a/b"}} [:a])
  ;
  ; @usage
  ;  (map/dissoc-in {:a {:b "a/b"}} [:a])
  ;
  ; @return (map)
  [n [key & keys :as value-path]]
  (if keys (if-let [next-n (get n key)]
                   (let [new-n (dissoc-in next-n keys)]
                        (if (seq new-n)
                            (assoc  n key new-n)
                            (dissoc n key)))
                   (return n))
           (dissoc n key)))

(defn dissoc-items
  ; @param (map) n
  ; @param (* in vector) keys
  ;
  ; @example
  ;  (dissoc-items {:a "A" :b "B" :c "C"} [:a :b])
  ;  =>
  ;  {:c "C"}
  ;
  ; @return (map)
  [n keys]
  (apply dissoc n keys))

(defn inject-in
  ; @param (map) n
  ; @param (vector) parent-path
  ; @param (*) key
  ; @param (*) value
  ;
  ; @example
  ;  (map/inject-in {} [:a :b] :c "x")
  ;  =>
  ;  {:a {:b {:c "x"}}}
  ;
  ; @return (*)
  [n parent-path key value]
  (assoc-in n (conj parent-path key) value))

(defn contains-key?
  ; @param (map) n
  ; @param (*) x
  ;
  ; @example
  ;  (map/contains-key? {:a {:b "a/b"}} :a)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n x]
  (contains? n x))

(defn contains-of-keys?
  ; @param (map) n
  ; @param (* in vector) xyz
  ;
  ; @example
  ;  (map/contains-of-keys? {:a {:b "a/b"}} [:a])
  ;  =>
  ;  true
  ;
  ; @example
  ;  (map/contains-of-keys? {:a {:b "a/b"}} [:a :b :c])
  ;  =>
  ;  true
  ;
  ; @example
  ;  (map/contains-of-keys? {:a {:b "a/b"}} [:b :c :d])
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n xyz]
  (boolean (some #(contains? n %) xyz)))

(defn contains-value?
  ; @param (map) n
  ; @param (*) x
  ;
  ; @example
  ;  (map/contains-value? {} "x")
  ;  =>
  ;  false
  ;
  ;
  ; @example
  ;  (map/contains-value? {:x "x"} "x")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n x]
  (some #(= x (val %)) n))

(defn values-equal?
  ; @param (map) n
  ; @param (vector) a-path
  ; @param (vector) b-path
  ;
  ; @example
  ;  (map/values-equal? {:a {:b "FOO"}
  ;                      :c {:d "FOO"}}
  ;                     [:a :b] [:c :d])
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n a-path b-path]
  (= (get-in n a-path)
     (get-in n b-path)))

(defn rekey-item
  ; @param (map) n
  ; @param (*) from
  ; @param (*) to
  ;
  ; @example
  ;  (map/rekey-item {:a "x"} :a :b)
  ;  =>
  ;  {:b "x"}
  ;
  ; @return (map)
  [n from to]
  (dissoc (assoc n to (get n from)) from))

(defn rekey-items
  ; @param (map) n
  ; @param (list of *) ks
  ;
  ; @example
  ;  (map/rekey-items {:a "x" :c "y"} :a :b :c :d)
  ;  =>
  ;  {:b "x" :d "y"}
  ;
  ; @return (map)
  [n & ks]
  (letfn [(f [o dex x] (if (even? dex)
                           (dissoc (assoc o (nth ks (inc dex)) (get o x)) x)
                           (return o)))]
         (reduce-kv f n (vec ks))))

(defn update-some
  ; Update n map if v is something
  ;
  ; @param (map) n
  ; @param (*) x
  ; @param (function) f
  ; @param (*) v
  ;
  ; @example
  ;  (map/update-some {:a [:x :y]} :a vector/conj-item :z)
  ;  =>
  ;  {:a [:x :y :z]}
  ;
  ; @example
  ;  (map/update-some {:a [:x :y]} :a vector/conj-item nil)
  ;  =>
  ;  {:a [:x :y]}
  ;
  ; @return (map)
  [n x f v]
  (if v (update n x f v)
        (return n)))

(defn update-in-some
  ; Update-in n map if v is something
  ;
  ; @param (map) n
  ; @param (vector) value-path
  ; @param (function) update-f
  ; @param (*) value
  ;
  ; @example
  ;  (map/update-in-some {:a {:b [:x :y]}} [:a :b] vector/conj-item :z)
  ;  =>
  ;  {:a {:b [:x :y :z]}}
  ;
  ; @example
  ;  (map/update-in-some {:a {:b [:x :y]}} [:a :b] vector/conj-item nil)
  ;  =>
  ;  {:a {:b [:x :y]}}
  ;
  ; @return (map)
  [n value-path update-f value]
  (if value (update-in n value-path update-f value)
            (return    n)))

(defn assoc-some
  ; Assoc value to n map if value is something
  ;
  ; @param (map) n
  ; @param (*) key
  ; @param (*) value
  ;
  ; @example
  ;  (map/assoc-some {:a [:x :y]} :b :z)
  ;  =>
  ;  {:a [:x :y] :b :z}
  ;
  ; @example
  ;  (map/assoc-some {:a [:x :y]} :b nil)
  ;  =>
  ;  {:a [:x :y]}
  ;
  ; @return (map)
  [n key value]
  (if value (assoc  n key value)
            (return n)))

(defn assoc-in-some
  ; Assoc-in value to n map if value is something
  ;
  ; @param (map) n
  ; @param (vector) value-path
  ; @param (*) value
  ;
  ; @example
  ;  (map/assoc-in-some {:a [:x :y]} [:b :c] :z)
  ;  =>
  ;  {:a [:x :y] :b {:c :z}}
  ;
  ; @example
  ;  (map/assoc-in-some {:a [:x :y]} [:b :c] nil)
  ;  =>
  ;  {:a [:x :y]}
  ;
  ; @return (map)
  [n value-path value]
  (if value (assoc-in n value-path value)
            (return   n)))

(defn assoc-or
  ; Assoc value to n map if key's value is nil
  ;
  ; @param (map) n
  ; @param (*) key
  ; @param (*) value
  ;
  ; @example
  ;  (map/assoc-or {:a :b} :a :c)
  ;  =>
  ;  {:a :b}
  ;
  ; @example
  ;  (map/assoc-or {:a nil} :a :c)
  ;  =>
  ;  {:a :c}
  ;
  ; @return (map)
  [n key value]
  (assoc n key (or (get n key) value)))

(defn assoc-in-or
  ; Assoc-in value to n map if value-path's value is nil
  ;
  ; @param (map) n
  ; @param (vector) value-path
  ; @param (*) value
  ;
  ; @example
  ;  (map/assoc-in-or {:a {:b :c}} [:a :b] :d)
  ;  =>
  ;  {:a {:b :c}}
  ;
  ; @example
  ;  (map/assoc-in-or {:a {:b nil}} [:a :b] :d)
  ;  =>
  ;  {:a {:b :d}}
  ;
  ; @return (map)
  [n value-path value]
  (assoc-in n value-path (or (get n value-path) value)))

(defn match-pattern?
  ; @param (map) ns
  ; @param (map) pattern
  ; @param (map)(opt) options
  ;  {:strict-matching? (boolean)(opt)
  ;    Default: false}
  ;
  ; @example
  ;  (map/match-pattern? {:a "a" :b "b"} {:a "a"})
  ;  =>
  ;  true
  ;
  ; @example
  ;  (map/match-pattern? {:a "a" :b "b"} {:a "a" :c "c"})
  ;  =>
  ;  false
  ;
  ; @example
  ;  (map/match-pattern? {:a "a" :b "b"} {:a "a"} {:strict-matching? true})
  ;  =>
  ;  false
  ;
  ; @example
  ;  (map/match-pattern? {:a "a" :b "b"} {:a "a" :b "b"} {:strict-matching? true})
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  ([n pattern]
   (match-pattern? n pattern {}))

  ([n pattern {:keys [strict-matching?]}]
   (let [difference (difference n pattern)]
        (or ; If strict-matching? is true ...
            (and (not strict-matching?)
                 (= (count n)
                    (+ (count difference)
                       (count pattern))))
            ; If strict-matching? is false ...
            (and (boolean strict-matching?)
                 (= (count n)
                    (count pattern))
                 (empty? difference))))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-keys-by
  ; @param (map) n
  ; @param (function) f
  ;
  ; @example
  ;  (map/get-keys-by {:a "a" :b :b :c :c} string?)
  ;  =>
  ;  [:a]
  ;
  ; @return (vector)
  [n f]
  (reduce-kv #(if (f %3) (conj %1 %2) %1) [] n))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn filter-values
  ; @param (map) n
  ; @param (function) filter-f
  ;
  ; @example
  ;  (map/filter-values {:a 0 :b 1 :c 2} even?)
  ;  =>
  ;  {:a 0 :c 2}
  ;
  ; @example
  ;  (map/filter-values {:a "abc" :b "def" :c "ghi"} #(string/starts-with? % "a"))
  ;  =>
  ;  {:a "abc"}
  ;
  ; @return (map)
  [n filter-f]
  (reduce-kv #(if (filter-f %3) (assoc %1 %2 %3) %1) {} n))

(defn filter-values-by
  ; @param (map) n
  ; @param (function) filter-f
  ; @param (function) value-f
  ;
  ; @example
  ;  (map/filter-values-by {:a {:value "abc"} :b {:value "def"}}
  ;                        #(string/starts-with? % "a") :value)
  ;  =>
  ;  {:a {:value "abc"}}
  ;
  ; @example
  ;  (map/filter-values-by {:a {:value "abc"} :b {:value "def"}}
  ;                        #(string/starts-with? % "a") (get % :value))
  ;  =>
  ;  {:a {:value "abc"}}
  ;
  ; @return (map)
  [n filter-f value-f]
  (reduce-kv #(if (filter-f (value-f %3)) (assoc %1 %2 %3) %1) {} n))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn any-key-match?
  ; @param (map) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (map/any-key-match? {:a 1 :b 2} string?)
  ;  =>
  ;  false
  ;
  ; @example
  ;  (map/any-key-match? {:a 1 "b" 2} string?)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n test-f]
  (boolean (some #(test-f (first %)) n)))

(defn any-value-match?
  ; @param (map) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (map/any-value-match? {:a 1 :b 2} string?)
  ;  =>
  ;  false
  ;
  ; @example
  ;  (map/any-value-match? {:a 1 :b "2"} string?)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n test-f]
  (boolean (some #(test-f (second %)) n)))

(defn all-values-match?
  ; @param (map) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (map/all-values-match? {:a 1 :b "2"} string?)
  ;  =>
  ;  false
  ;
  ; @example
  ;  (map/all-values-match? {:a "1" :b "2"} string?)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n test-f]
  (every? #(test-f (second %)) n))

(defn get-first-match-key
  ; @param (map) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (map/get-first-match-key {:a 1 :b 2} string?)
  ;  =>
  ;  nil
  ;
  ; @example
  ;  (map/get-first-match-key {:a 1 "b" 2} string?)
  ;  =>
  ;  "b"
  ;
  ; @return (*)
  [n test-f]
  (some #(if (test-f (second %)) (first %)) n))

(defn get-first-match-value
  ; @param (map) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (map/get-first-match-value {:a 1 :b 2} string?)
  ;  =>
  ;  nil
  ;
  ; @example
  ;  (map/get-first-match-value {:a 1 :b "2"} string?)
  ;  =>
  ;  "2"
  ;
  ; @example
  ;  (map/get-first-match-value {:a {:id "apple"} :b {:id "banana"}} #(= "apple" (:id %)))
  ;  =>
  ;  {:id "apple"}
  ;
  ; @return (*)
  [n test-f]
  (some #(if (test-f (second %)) (second %)) n))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ->keys
  ; @param (map) n
  ; @param (function) update-f
  ;
  ; @example
  ;  (map/->keys {:a "A" :b "B"} name)
  ;  =>
  ;  {"a" "1" "b" "2"}
  ;
  ; @return (map)
  [n update-f]
  (reduce-kv #(assoc %1 (update-f %2) %3) {} n))

(defn ->>keys
  ; @param (map) n
  ; @param (function) update-f
  ;
  ; @example
  ;  (map/->>keys {:a "A" :b "B" :c [{:d "D"}]} name)
  ;  =>
  ;  {"a" "1" "b" "2" "c" [{"d" "D"}]}
  ;
  ; @return (map)
  [n update-f]
  ; A rekurzió a vektorok elemein NEM hajtja végre az update-f függvényt, mivel azok a térképek értékeinek megfelelői!
  (letfn [(f [n] (cond (vector? n) (reduce    #(conj  %1               (f %2)) [] n)
                       (map?    n) (reduce-kv #(assoc %1 (update-f %2) (f %3)) {} n)
                       :else    n))]
         (f n)))

(defn ->values
  ; @param (map) n
  ; @param (function) update-f
  ;
  ; @example
  ;  (map/->values {:a "A" :b "B"} keyword)
  ;  =>
  ;  {:a :A :b :B}
  ;
  ; @return (map)
  [n update-f]
  (reduce-kv #(assoc %1 %2 (update-f %3)) {} n))

(defn ->>values
  ; @param (map) n
  ; @param (function) update-f
  ;
  ; @example
  ;  (map/->>values {:a "A" :b "B" :c [:x "y" {:d "D"}]} keyword)
  ;  =>
  ;  {:a :A :b :B :c [:x :y {:d :D}]}
  ;
  ; @return (map)
  [n update-f]
  ; A rekurzió a vektorok elemein is végrehajtja az update-f függvényt, mivel azok a térképek értékeinek megfelelői!
  (letfn [(f [n] (cond (vector? n) (reduce    #(conj  %1    (f %2)) [] n)
                       (map?    n) (reduce-kv #(assoc %1 %2 (f %3)) {} n)
                       :else       (update-f n)))]
         (f n)))

(defn ->kv
  ; @param (map) n
  ; @param (function) k-f
  ; @param (function) v-f
  ;
  ; @example
  ;  (map/->keys {:a 1 :b 2} name inc)
  ;  =>
  ;  {"a" 2 "b" 3}
  ;
  ; @return (map)
  [n k-f v-f]
  (reduce-kv #(assoc %1 (k-f %2) (v-f %3)) {} n))

(defn ->>kv
  ; @param (map) n
  ; @param (function) k-f
  ; @param (function) v-f
  ;
  ; @example
  ;  (map/->>kv {"a" "A" "b" "B" "c" ["x" "y" {"d" "D"}]} keyword keyword)
  ;  =>
  ;  {:a :A :b :B :c [:x :y {:d :D}]}
  ;
  ; @return (map)
  [n k-f v-f]
  ; A rekurzió a vektorok elemein is végrehajtja az v-f függvényt, mivel azok a térképek értékeinek megfelelői!
  (letfn [(f [n] (cond (map?    n) (reduce-kv #(assoc %1 (k-f %2) (f %3)) {} n)
                       (vector? n) (reduce    #(conj  %1          (f %2)) [] n)
                       :else       (v-f n)))]
         (f n)))
