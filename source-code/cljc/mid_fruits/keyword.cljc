
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



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
  ;  (to-string :foo.bar/baz)
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
  ;  (namespaced :foo)
  ;  =>
  ;  false
  ;
  ; @example
  ;  (namespaced :foo/bar)
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
  ;  (namespaced! :foo/bar)
  ;  =>
  ;  :foo/bar
  ;
  ; @example
  ;  (namespaced! :bar)
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
  ;  (join :a :b "c" :d)
  ;  =>
  ;  :abcd
  ;
  ; @example
  ;  (join :x/a :x/b "c" :d)
  ;  =>
  ;  :abcd
  ;
  ; @return (keyword)
  [& abc]
  (letfn [(f [result x] (if (keyword? x) (str result (name x))
                                         (str result x)))]
         (keyword (reduce f abc))))

(defn append
  ; @param (keyword) n
  ; @param (keyword) x
  ; @param (string)(opt) separator
  ;
  ; @example
  ;  (append :bar :baz)
  ;  =>
  ;  :barbaz
  ;
  ; @example
  ;  (append :foo/bar :baz)
  ;  =>
  ;  :foo/barbaz
  ;
  ; @example
  ;  (append :foo/bar :baz "--")
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
  ;  (add-namespace :foo :bar)
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
  ;  (add-items-namespace :foo [:bar :baz])
  ;  =>
  ;  [:foo/bar :foo/baz]
  ;
  ; @return (namespaced keywords in vector)
  [namespace abc]
  (letfn [(f [result x] (conj result (keyword (name namespace)
                                              (name x))))]
         (reduce f [] abc)))

(defn get-namespace
  ; @param (keyword) n
  ;
  ; @example
  ;  (get-namespace :bar)
  ;  =>
  ;  nil
  ;
  ; @example
  ;  (get-namespace :foo/bar)
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
  ;  (get-namespace :foo/bar)
  ;  =>
  ;  :bar
  ;
  ; @return (keyword)
  [n]
  (if (keyword? n)
      (keyword (name n))))
