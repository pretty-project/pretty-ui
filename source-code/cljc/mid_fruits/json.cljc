
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.08
; Description:
; Version: v0.2.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.json
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map]
              [mid-fruits.vector :as vector]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def KEYWORD-PREFIX "*")



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn json->clj
  ; @param (json) n
  ;
  ; @return (map)
  [n]
  #?(:cljs (js->clj n :keywordize-keys true)))



;; -- Keywordize / unkeywordize -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn unkeywordized-string?
  ; @param (*) n
  ;
  ; @example
  ;  (def KEYWORD-PREFIX "*")
  ;  (json/unkeywordized-string? "*:apple")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (boolean (and (string? n)
                (= (str (nth n 0))
                   (param KEYWORD-PREFIX))
                (= (str (nth n 1))
                   (param ":")))))

(defn unkeywordize-values
  ; XXX#5914
  ; Az unkeywordize-values függvény string típussá alakítja az átadott adatszerkezetben
  ; található kulcsszó típusú értékeket.
  ; A téves át- és visszaalakítások elkerülése érdekében biztonsági prefixummal
  ; látja el a string típussá alakított értékeket.
  ;
  ; @param (*) n
  ;
  ; @example
  ;  (def KEYWORD-PREFIX "*")
  ;  (json/unkeywordize-values {:a :b :c [:d "e"] :f {:g "h" :i :j}})
  ;  =>
  ;  {:a "*:b" :c ["*:d" "e"] :f {:g "*h" :i "*:j"}}
  ;
  ; @return (*)
  [n]
  (letfn [(reduce-map
            ; @param (map) n
            ;
            ; @return (map)
            [n]
            (reduce-kv (fn [result k v]
                           (let [v (unkeywordize-values v)]
                                (assoc result k v)))
                       (param {})
                       (param n)))

          (reduce-vector
            ; @param (vector) n
            ;
            ; @return (vector)
            [n]
            (reduce (fn [result x]
                        (let [x (unkeywordize-values x)]
                             (vector/conj-item result x)))
                    (param [])
                    (param n)))

          (unkeywordize-keyword
            ; @param (keyword) n
            ;
            ; @return (string)
            [n]
            (str KEYWORD-PREFIX n))]

         ; unkeywordize
         (cond (keyword? n) (unkeywordize-keyword n)
               (map?     n) (reduce-map           n)
               (vector?  n) (reduce-vector        n)
               :else        (return               n))))

(defn keywordize-values
  ; XXX#5914
  ;
  ; @param (*) n
  ;
  ; @example
  ;  (def KEYWORD-PREFIX "*")
  ;  (json/keywordize-values {:a "*:b" :c ["*:d" "e"] :f {:g "*h" :i "*:j"}})
  ;  =>
  ;  {:a :b :c [:d "e"] :f {:g "h" :i :j}}
  ;
  ; @return (*)
  [n]
  (letfn [(reduce-map
            ; @param (map) n
            ;
            ; @return (map)
            [n]
            (reduce-kv (fn [result k v]
                           (let [v (keywordize-values v)]
                                (assoc result k v)))
                       (param {})
                       (param n)))

          (reduce-vector
            ; @param (vector) n
            ;
            ; @return (vector)
            [n]
            (reduce (fn [result x]
                        (let [x (keywordize-values x)]
                             (vector/conj-item result x)))
                    (param [])
                    (param n)))

          (keywordize-string
            ; @param (string) n
            ;
            ; @example
            ;  (keywordize-string "*:my-value")
            ;  =>
            ;  :my-value
            ;
            ; @return (keyword)
            [n]
            (if (unkeywordized-string? n)
                (let [n (subs n 2)]
                     (keyword n))
                (return n)))]

         ; keywordize-values
         (cond (string? n) (keywordize-string n)
               (map?    n) (reduce-map        n)
               (vector? n) (reduce-vector     n)
               :else       (return            n))))

(defn unkeywordize-keys
  ; @param (*) n
  ;
  ; @example
  ;  (json/unkeywordize-keys {:my-namespace/key :my-value})
  ;  =>
  ;  {"my-namespace/key" :my-value}
  ;
  ; @return (*)
  [n]
  (letfn [(reduce-map
            ; @param (map) n
            ;
            ; @return (map)
            [n]
            (reduce-kv (fn [result k v]
                           (let [k (unkeywordize-keys k)]
                                (assoc result k v)))
                       (param {})
                       (param n)))

          (reduce-vector
            ; @param (vector) n
            ;
            ; @return (vector)
            [n]
            (reduce (fn [result x]
                        (let [x (unkeywordize-keys x)]
                             (vector/conj-item result x)))
                    (param [])
                    (param n)))

          (unkeywordize-keyword
            ; @param (keyword) n
            ;
            ; @example
            ;  (unkeywordize-keyword :my-namespace/key)
            ;  =>
            ;  "my-namespace/key"
            ;
            ; @return (string)
            [n]
            (apply str (rest (str n))))]

         ; unkeywordize-keys
         (cond (keyword? n) (unkeywordize-keyword n)
               (map?     n) (reduce-map           n)
               (vector?  n) (reduce-vector        n)
               :else        (return               n))))

(defn keywordize-keys
  ; @param (*) n
  ;
  ; @example
  ;  (json/keywordize-keys {"my-namespace/key" :my-value})
  ;  =>
  ;  {:my-namespace/key :my-value}
  ;
  ; @return (*)
  [n]
  (letfn [(reduce-map
            ; @param (map) n
            ;
            ; @return (map)
            [n]
            (reduce-kv (fn [result k v]
                           (let [k (keywordize-keys k)]
                                (assoc result k v)))
                       (param {})
                       (param n)))

          (reduce-vector
            ; @param (vector) n
            ;
            ; @return (vector)
            [n]
            (reduce (fn [result x]
                        (let [x (keywordize-keys x)]
                             (vector/conj-item result x)))
                    (param [])
                    (param n)))

          (keywordize-string
            ; @param (string) n
            ;
            ; @example
            ;  (keywordize-string "my-namespace/key")
            ;  =>
            ;  :my-namespace/key
            ;
            ; @return (keyword)
            [n]
            (keyword n))]

         ; keywordize-keys
         (cond (string? n) (keywordize-string n)
               (map?    n) (reduce-map        n)
               (vector? n) (reduce-vector     n)
               :else       (return            n))))
