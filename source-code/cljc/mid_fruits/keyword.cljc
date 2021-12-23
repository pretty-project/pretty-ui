
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.02.04
; Description:
; Version: v0.6.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.keyword
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.random :as random]
              [mid-fruits.string :as string]))



;; -- Type converters ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn to-string
  ; @param (keyword) n
  ;
  ; @example
  ;  (keyword/to-string :foo.bar/baz)
  ;  =>
  ;  "foo.bar/baz"
  ;
  ; @return (string)
  [n]
 ;(apply str (rest (str n)))
  (cond (keyword? n) (if-let [namespace (namespace n)]
                             (str namespace "/" (name n))
                             (name n))
        (string?  n)
        (return   n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn namespaced?
  ; @param (keyword) n
  ;
  ; @example
  ;  (keyword/namespaced :foo)
  ;  =>
  ;  false
  ;
  ; @example
  ;  (keyword/namespaced :foo/bar)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (boolean (and (keyword? n)
                (some? (namespace n)))))

(defn namespaced!
  ; @param (keyword) n
  ;
  ; @example
  ;  (keyword/namespaced! :foo/bar)
  ;  =>
  ;  :foo/bar
  ;
  ; @example
  ;  (keyword/namespaced! :bar)
  ;  =>
  ;  :ko4983l3-i8790-j93l3-lk8385u591o2/bar
  ;
  ; @return (keyword)
  [n]   ; If n is keyword and namespaced ...
  (cond (and (keyword? n)
             (some? (namespace n)))
        (return n)
        ; If n is keyword and NOT namespaced ...
        (and (keyword? n))
        (let [namespace (random/generate-string)]
             (keyword namespace (name n)))))
        ; If n is NOT keyword ...
        ; ... returning w/ nil

(defn split-namespace
  ; @param (keyword) n
  ;
  ; @example
  ;  (keyword/split-namespace :foo.bar/bar)
  ;  =>
  ;  [:foo :bar]
  ;
  ; @return (vector)
  [n]
  (if-not (and (keyword? n)
               (some? (namespace n)))
          (return [])
          (vec (reduce (fn [%1 %2] (conj %1 (keyword %2)))
                       [] (string/split (namespace n) #".")))))

(defn split-name
  ; @param (keyword) n
  ;
  ; @example
  ;  (keyword/split-name :foo.nat/bar.baz)
  ;  =>
  ;  [:bar :baz]
  ;
  ; @return (vector)
  [n]
  (if-not (keyword? n)
          (return   [])
          (vec (reduce (fn [%1 %2] (conj %1 (keyword %2)))
                       [] (string/split (name n) #".")))))

(defn split
  ; @param (keyword) n
  ;
  ; @example
  ;  (keyword/split :foo)
  ;  =>
  ;  [:foo]
  ;
  ; @example
  ;  (keyword/split :foo.bar)
  ;  =>
  ;  [:foo :bar]
  ;
  ; @example
  ;  (keyword/split :foo.bar/baz)
  ;  =>
  ;  [:foo :bar :baz]
  ;
  ; @return (vector)
  [n]
  (if-not (keyword? n)
          (return   [])
          (vec (concat (split-namespace n)
                       (split-name      n)))))

(defn join
  ; @param (keywords and/or strings) abc
  ;
  ; @example
  ;  (keyword/join :a :b "c" :d)
  ;  =>
  ;  :abcd
  ;
  ; @example
  ;  (keyword/join :x/a :x/b "c" :d)
  ;  =>
  ;  :abcd
  ;
  ; @return (keyword)
  [& abc]
  (keyword (reduce (fn [%1 %2]
                       (cond (keyword? %2) (str %1 (name %2))
                             (string?  %2) (str %1 %2)
                             (integer? %2) (str %1 %2)
                             :else         (return %1)))
                   (str   nil)
                   (param abc))))

(defn append
  ; @param (keyword) n
  ; @param (keyword) x
  ; @param (string)(opt) separator
  ;
  ; @example
  ;  (keyword/append :bar :baz)
  ;  =>
  ;  :barbaz
  ;
  ; @example
  ;  (keyword/append :foo/bar :baz)
  ;  =>
  ;  :foo/barbaz
  ;
  ; @example
  ;  (keyword/append :foo/bar :baz "--")
  ;  =>
  ;  :foo/bar--baz
  ;
  ; @return (keyword)
  [n x & [separator]]
  (if-let [namespace (namespace n)]
          (keyword namespace (str (name n)
                                  (param separator)
                                  (name x)))
          (keyword (str (name n)
                        (param separator)
                        (name x)))))

(defn add-namespace
  ; @param (keyword) x
  ; @param (keyword) n
  ;
  ; @example
  ;  (keyword/add-namespace :foo :bar)
  ;  =>
  ;  :foo/bar
  ;
  ; @return (keyword)
  [x n]
  (keyword (name x)
           (name n)))

(defn add-items-namespace
  ; @param (keyword) x
  ; @param (keywords in vector) abc
  ;
  ; @example
  ;  (keyword/add-items-namespace :foo [:bar :baz])
  ;  =>
  ;  [:foo/bar :foo/baz]
  ;
  ; @return (namespaced keywords in vector)
  [x abc]
  (vec (reduce (fn [result key]
                   (conj result (keyword (name x)
                                         (name key))))
               [] abc)))

(defn get-namespace
  ; @param (keyword) n
  ;
  ; @example
  ;  (keyword/get-namespace :bar)
  ;  =>
  ;  nil
  ;
  ; @example
  ;  (keyword/get-namespace :foo/bar)
  ;  =>
  ;  :foo
  ;
  ; @return (keyword or nil)
  [n]
  (if (keyword? n)
      (if-let [namespace (namespace n)]
              (keyword namespace))))

(defn get-name
  ; @param (keyword) n
  ;
  ; @example
  ;  (keyword/get-namespace :foo/bar)
  ;  =>
  ;  :bar
  ;
  ; @return (keyword)
  [n]
  (if (keyword? n)
      (keyword (name n))))

(defn before-last-occurence
  ; @param (keyword) n
  ; @param (keyword) x
  ;
  ; @example
  ;  (keyword/before-last-occurence :foo/bar :ar)
  ;  =>
  ;  :foo/b
  ;
  ; @return (keyword or nil)
  [n x]
  (keyword (string/before-last-occurence (to-string n)
                                         (to-string x))))

(defn after-last-occurence
  ; @param (keyword) n
  ; @param (keyword) x
  ;
  ; @example
  ;  (keyword/after-last-occurence :foo/bar :fo)
  ;  =>
  ;  :o/bar
  ;
  ; @return (keyword or nil)
  [n x]
  (keyword (string/after-last-occurence (to-string n)
                                        (to-string x))))

(defn starts-with?
  ; @param (keyword) n
  ; @param (keyword) x
  ;
  ; @example
  ;  (keyword/starts-with? :foo :fo)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (keyword/starts-with? :foo/bar :fo)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (keyword/starts-with? :foo/bar :ba)
  ;  =>
  ;  false
  ;
  ; @example
  ;  (keyword/starts-with? :foo/bar :foo/b)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n x]
  (string/starts-with? (str n)
                       (str x)))

(defn not-starts-with?
  ; @param (keyword) n
  ; @param (keyword) x
  ;
  ; @return (boolean)
  [n x]
  (not (starts-with? n x)))

(defn ends-with?
  ; @param (keyword) n
  ; @param (keyword) x
  ;
  ; @example
  ;  (keyword/ends-with? :foo :oo)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (keyword/ends-with? :foo/bar :ar)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (keyword/ends-with? :foo/bar :ba)
  ;  =>
  ;  false
  ;
  ; @example
  ;  (keyword/ends-with? :foo/bar :o/bar)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n x]
  (string/ends-with? (str n)
                     (str x)))

(defn not-ends-with?
  ; @param (keyword) n
  ; @param (keyword) x
  ;
  ; @return (boolean)
  [n x]
  (not (ends-with? n x)))
