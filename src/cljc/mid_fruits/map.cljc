
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.10
; Description:
; Version: v0.9.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.map
    (:require [clojure.data      :as data]
              [mid-fruits.candy  :refer [param return]]
              [mid-fruits.loop   :refer [reduce-indexed reduce-while reduce-kv-while]]
              [mid-fruits.vector :as vector]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(def empty-map {})



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn map->vector
  ; @param (vector) n
  ;
  ; @example
  ;  (map/map->vector {0 :x 1 :y 2 :z})
  ;  => [:x :y :z]
  ;
  ; @return (map)
  [n]
  (reduce-kv (fn [%1 _ %3] (vector/conj-item %1 %3))
             (param [])
             (param n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn nonempty?
  ; @param (map) n
  ;
  ; @usage
  ;  (map/nonempty? {})
  ;
  ; @return (boolean)
  [n]
  (boolean (and (map? n)
                (not (empty? n)))))

(defn get-keys
  ; @param (map) n
  ;
  ; @example
  ;  (map/get-keys {:a {:b "a/b"}})
  ;  => [:a]
  ;
  ; @return (vector)
  [n]
  (vec (keys n)))

(defn get-first-key
  ; @param (map) n
  ;
  ; @example
  ;  (map/get-first-key {:a {:b "a/b"} :c "xyz"})
  ;  => :a
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
  ;  => "abc"
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
  ;  => {:fruit/apple "red" :fruit/banana "yellow"}
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
  (letfn [(deep-merge
            ; @param (map) a
            ; @param (map) b
            ;
            ; @return (map)
            [a b]
            (if (and (map? a)
                     (map? b))
                (merge-with deep-merge a b)
                (return b)))]
         ; deep-merge
         (if (some identity xyz)
             (reduce (fn [%1 %2] (deep-merge %1 %2))
                     (param n)
                     (param xyz))
             (return n))))

(defn difference
  ; Things only in a
  ;
  ; @param (map) a
  ; @param (map) b
  ;
  ; @example
  ;  (map/difference {:a "a" :b "b"} {:a "a"})
  ;  => {:b "b"}
  ;
  ; @return (map)
  [a b]
  (first (data/diff a b)))

(defn inherit
  ; @param (map) n
  ; @param (vector) keys
  ;
  ; @example
  ;  (map/inherit {:a "a" :b "b" :c "c"} [:a :c :d])
  ;  => {:a "a" :c "c"}
  ;
  ; @return (map)
  [n keys]
; (reduce (fn [%1 %2]
;             (if-let [inherited-value (get n %2)]
;                     (assoc  %1 %2 inherited-value)
;                     (return %1))
;         (param {})
;         (param keys))
  (select-keys n keys))

(defn swap
  ; @param (map) n
  ;
  ; @example
  ;  (map/swap {:a "a" :b "b"})
  ;  => {"a" :a "b" :b}
  ;
  ; @return (map)
  [n]
  (zipmap (get-values n)
          (get-keys   n)))

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
  ; @return (*)
  [n [key & keys :as value-path]]
  (if (some? keys)
      (if-let [next-n (get n key)]
              (let [new-n (dissoc-in next-n keys)]
                   (if (seq new-n)
                       (assoc  n key new-n)
                       (dissoc n key)))
              (return n))
      (dissoc n key)))

(defn inject-in
  ; @param (map) n
  ; @param (vector) parent-path
  ; @param (*) key
  ; @param (*) value
  ;
  ; @example
  ;  (map/inject-in {} [:a :b] :c "x")
  ;  => {:a {:b {:c "x"}}}
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
  ;  => true
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
  ;  => true
  ;
  ; @example
  ;  (map/contains-of-keys? {:a {:b "a/b"}} [:a :b :c])
  ;  => true
  ;
  ; @example
  ;  (map/contains-of-keys? {:a {:b "a/b"}} [:b :c :d])
  ;  => false
  ;
  ; @return (boolean)
  [n xyz]
  (reduce-while (fn [_ %2] (contains? n %2))
                (param false)
                (param xyz)
                (fn [%1 _] (true? %1))))

(defn contains-value?
  ; @param (map) n
  ; @param (*) x
  ;
  ; @example
  ;  (map/contains-value? {} "x")
  ;  => false
  ;
  ;
  ; @example
  ;  (map/contains-value? {:x "x"} "x")
  ;  => true
  ;
  ; @return (boolean)
  [n x]
  (some #(= x (val %))
         (param n)))

(defn values-equal?
  ; @param (map) n
  ; @param (vector) a-path
  ; @param (vector) b-path
  ;
  ; @example
  ;  (map/values-equal? {:a {:b "FOO"}
  ;                     :c {:d "FOO"}}
  ;                     [:a :b] [:c :d])
  ;  => true
  ;
  ; @return (boolean)
  [n a-path b-path]
  (= (get-in n a-path)
     (get-in n b-path)))

(defn rekey-value
  ; @param (map) n
  ; @param (*) from
  ; @param (*) to
  ;
  ; @example
  ;  (map/rekey-value {:a "x"} :a :b)
  ;  => {:b "x"}
  ;
  ; @return (*)
  [n from to]
  (dissoc (assoc n to (get n from)) from))

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
  ;  => {:a [:x :y :z]}
  ;
  ; @example
  ;  (map/update-some {:a [:x :y]} :a vector/conj-item nil)
  ;  => {:a [:x :y]}
  ;
  ; @return (map)
  [n x f v]
  (if (some? v)
      (update n x f v)
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
  ;  => {:a {:b [:x :y :z]}}
  ;
  ; @example
  ;  (map/update-in-some {:a {:b [:x :y]}} [:a :b] vector/conj-item nil)
  ;  => {:a {:b [:x :y]}}
  ;
  ; @return (map)
  [n value-path update-f value]
  (if (some?     value)
      (update-in n value-path update-f value)
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
  ;  => {:a [:x :y] :b :z}
  ;
  ; @example
  ;  (map/assoc-some {:a [:x :y]} :b nil)
  ;  => {:a [:x :y]}
  ;
  ; @return (map)
  [n key value]
  (if (some?  value)
      (assoc  n key value)
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
  ;  => {:a [:x :y] :b {:c :z}}
  ;
  ; @example
  ;  (map/assoc-in-some {:a [:x :y]} [:b :c] nil)
  ;  => {:a [:x :y]}
  ;
  ; @return (map)
  [n value-path value]
  (if (some?    value)
      (assoc-in n value-path value)
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
  ;  => {:a :b}
  ;
  ; @example
  ;  (map/assoc-or {:a nil} :a :c)
  ;  => {:a :c}
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
  ;  => {:a {:b :c}}
  ;
  ; @example
  ;  (map/assoc-in-or {:a {:b nil}} [:a :b] :d)
  ;  => {:a {:b :d}}
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
  ;  => true
  ;
  ; @example
  ;  (map/match-pattern? {:a "a" :b "b"} {:a "a" :c "c"})
  ;  => false
  ;
  ; @example
  ;  (map/match-pattern? {:a "a" :b "b"} {:a "a"} {:strict-matching? true})
  ;  => false
  ;
  ; @example
  ;  (map/match-pattern? {:a "a" :b "b"} {:a "a" :b "b"} {:strict-matching? true})
  ;  => true
  ;
  ; @return (boolean)
  ([n pattern]
   (match-pattern? n pattern nil))

  ([n pattern {:keys [strict-matching?]}]
   (let [difference (difference n pattern)]
                     ; Non-strict matching
        (boolean (or (and (not strict-matching?)
                          (= (count n)
                             (+ (count difference)
                                (count pattern))))
                     ; Strict matching
                     (and (param strict-matching?)
                          (= (count n)
                             (count pattern))
                          (empty? difference)))))))

(defn keywordize
  ; @param (map) n
  ;
  ; @example
  ;  (map/keywordize {"my-key" "my-value"})
  ;  => {:my-key "my-value"}
  ;
  ; @return (map)
  [n]
  (reduce-kv (fn [%1 %2 %3]
                 (assoc %1 (keyword %2) %3))
             (param {})
             (param n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-ordered-keys
  ; @param (map) n
  ; @param (function) comparator-f
  ;
  ; @example
  ;  (map/get-ordered-keys {:a "abc" :g "ghi" :d "def"} string/abc?)
  ;  =>
  ;  [:a :d :g]
  ;
  ; @example
  ;  (map/get-ordered-keys {:a 1 :g 3 :d 2} <)
  ;  =>
  ;  [:a :d :g]
  ;
  ; @return (vector)
  [n comparator-f]
  (reduce-kv (fn [result k v]
                 (if (empty? result)
                     [k]
                     (reduce-indexed (fn [subresult x dex]
                                         (let [lap         (inc dex)
                                               k-lower?    (comparator-f v (x n))
                                               k-injected? (vector/count? subresult dex)
                                               last-lap?   (vector/count? result    lap)]
                                              (cond (and k-lower?  k-injected?) (vector/concat-items subresult [k x])
                                                    (and last-lap? k-injected?) (vector/concat-items subresult [x k])
                                                    :else                       (vector/conj-item    subresult x))))
                                     (param [])
                                     (param result))))
             (param [])
             (param n)))

(defn get-ordered-keys-by
  ; @param (map) n
  ; @param (function) comparator-f
  ; @param (function) value-f
  ;
  ; @example
  ;  (map/get-ordered-keys-by {:a {:value "abc"} :g {:value "ghi"} :d {:value "def"}}
  ;                           string/abc? :value)
  ;  =>
  ;  [:a :d :g]
  ;
  ; @example
  ;  WARNING! NOT TESTED!
  ;  (map/get-ordered-keys-by {:a {:value "abc"} :g {:value "ghi"} :d {:value "def"}}
  ;                           string/abc? #(get % :value))
  ;  =>
  ;  [:a :d :g]
  ;
  ; @return (vector)
  [n comparator-f value-f]
  (reduce-kv (fn [result k v]
                 (if (empty? result)
                     [k]
                     (reduce-indexed (fn [subresult x dex]
                                         (let [lap         (inc dex)
                                               k-lower?    (comparator-f (value-f v) (value-f (x n)))
                                               k-injected? (vector/count? subresult dex)
                                               last-lap?   (vector/count? result    lap)]
                                              (cond (and k-lower?  k-injected?) (vector/concat-items subresult [k x])
                                                    (and last-lap? k-injected?) (vector/concat-items subresult [x k])
                                                    :else                       (vector/conj-item    subresult x))))
                                     (param [])
                                     (param result))))
             (param [])
             (param n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn filter-values
  ; @param (map) n
  ; @param (function) filter-f
  ;
  ; @example
  ;  (map/filter-values {:a 0 :b 1 :c 2} even?)
  ;  => {:a 0 :c 2}
  ;
  ; @example
  ;  (map/filter-values {:a "abc" :b "def" :c "ghi"} #(string/starts-with? % "a"))
  ;  => {:a "abc"}
  ;
  ; @return (map)
  [n filter-f]
  (reduce-kv (fn [result k v]
                 (if (filter-f v)
                     (assoc    result k v)
                     (return   result)))
             (param {})
             (param n)))

(defn filter-values-by
  ; @param (map) n
  ; @param (function) filter-f
  ; @param (function) value-f
  ;
  ; @example
  ;  (map/filter-values-by {:a {:value "abc"} :b {:value "def"}}
  ;                        #(string/starts-with? % "a") :value)
  ;  => {:a {:value "abc"}}
  ;
  ; @example
  ;  (map/filter-values-by {:a {:value "abc"} :b {:value "def"}}
  ;                        #(string/starts-with? % "a") (get % :value))
  ;  => {:a {:value "abc"}}
  ;
  ; @return (map)
  [n filter-f value-f]
  (reduce-kv (fn [result k v]
                 (if (filter-f (value-f v))
                     (assoc    result k v)
                     (return   result)))
             (param {})
             (param n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn any-key-match?
  ; @param (map) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (map/any-key-match? {:a 1 :b 2} string?)
  ;  => false
  ;
  ; @example
  ;  (map/any-key-match? {:a 1 "b" 2} string?)
  ;  => true
  ;
  ; @return (boolean)
  [n test-f]
  (reduce-kv-while (fn [_ %2 _] (test-f %2))
                   (param false)
                   (param n)
                   (fn [%1 _ _] (boolean %1))))

(defn any-value-match?
  ; @param (map) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (map/any-value-match? {:a 1 :b 2} string?)
  ;  => false
  ;
  ; @example
  ;  (map/any-value-match? {:a 1 :b "2"} string?)
  ;  => true
  ;
  ; @return (boolean)
  [n test-f]
  (reduce-kv-while (fn [_ _ %3] (test-f %3))
                   (param false)
                   (param n)
                   (fn [%1 _ _] (boolean %1))))

(defn get-first-match-key
  ; @param (map) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (map/get-first-match-key {:a 1 :b 2} string?)
  ;  => nil
  ;
  ; @example
  ;  (map/get-first-match-key {:a 1 "b" 2} string?)
  ;  => "b"
  ;
  ; @return (*)
  [n test-f]
  (reduce-kv-while (fn [_ %2 _] (if (test-f %2)
                                    (return %2)))
                   (param nil)
                   (param n)
                   (fn [%1 _ _] (some? %1))))

(defn get-first-match-value
  ; @param (map) n
  ; @param (function) test-f
  ;
  ; @example
  ;  (map/get-first-match-value {:a 1 :b 2} string?)
  ;  => nil
  ;
  ; @example
  ;  (map/get-first-match-value {:a 1 :b "2"} string?)
  ;  => "2"
  ;
  ; @example
  ;  (map/get-first-match-value {:a {:id "apple"} :b {:id "banana"}} #(= "apple" (:id %)))
  ;  => {:id "apple"}
  ;
  ; @return (*)
  [n test-f]
  (reduce-kv-while (fn [_ _ %3] (if (test-f %3)
                                    (return %3)))
                   (param nil)
                   (param n)
                   (fn [%1 _ _] (some? %1))))
