
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.02.04
; Description:
; Version: v0.8.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.keyword
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.random :as random]
              [mid-fruits.string :as string]))



;; ----------------------------------------------------------------------------
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
  (if (keyword? n)
      (if-let [namespace (namespace n)]
              (str namespace "/" (name n))
              (name n))
      (return n)))



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
  (and (keyword? n)
       (some? (namespace n))))

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
  [n]
  (if (keyword? n)
      (if (namespace n)
          ; If n is keyword and namespaced ...
          (return n)
          ; If n is keyword and NOT namespaced ...
          (keyword (random/generate-uuid) (name n)))))
      ; If n is NOT keyword ...
      ; ... returning w/ nil

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
  (letfn [(join-f [o x] (if (keyword? x) (str o (name x))
                                         (str o x)))]
         (keyword (reduce join-f abc))))

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
  ([n x]
   (if-let [namespace (namespace n)]
           ; If n is namespaced ...
           (keyword namespace (str (name n) (name x)))
           ; If n is NOT namespaced ...
           (keyword (str (name n) (name x)))))

  ([n x separator]
   (if-let [namespace (namespace n)]
           ; If n is namespaced ...
           (keyword namespace (str (name n) separator (name x)))
           ; If n is NOT namespaced ...
           (keyword (str (name n) separator (name x))))))

(defn add-namespace
  ; @param (keyword) namespace
  ; @param (keyword) n
  ;
  ; @example
  ;  (keyword/add-namespace :foo :bar)
  ;  =>
  ;  :foo/bar
  ;
  ; @return (keyword)
  [namespace n]
  (keyword (name namespace) (name n)))

(defn add-items-namespace
  ; @param (keyword) namespace
  ; @param (keywords in vector) abc
  ;
  ; @example
  ;  (keyword/add-items-namespace :foo [:bar :baz])
  ;  =>
  ;  [:foo/bar :foo/baz]
  ;
  ; @return (namespaced keywords in vector)
  [namespace abc]
  (letfn [(add-items-namespace-f [o x] (conj o (keyword (name namespace)
                                                        (name x))))]
         (reduce add-items-namespace-f [] abc)))

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
  (string/not-starts-with? (str n)
                           (str x)))

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
  (string/not-ends-with? (str n)
                         (str x)))
