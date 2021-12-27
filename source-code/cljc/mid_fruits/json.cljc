
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.08
; Description:
; Version: v0.4.8



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

(defn unkeywordized-value?
  ; @param (*) n
  ;
  ; @example
  ;  (json/unkeywordized-value? "*:apple")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (and (string? n)
       (= (str (nth n 0))
          (param KEYWORD-PREFIX))
       (= (str (nth n 1))
          (param ":"))))

(defn keywordize-value
  ; @param (*) n
  ;
  ; @example
  ;  (json/keywordize-value "*:my-value")
  ;  =>
  ;  :my-value
  ;
  ; @return (*)
  [n]
  (if (unkeywordized-value? n)
      (-> n (subs 2) keyword)
      (return n)))

(defn unkeywordize-value
  ; @param (*) n
  ;
  ; @example
  ;  (json/unkeywordize-keyword :my-value)
  ;  =>
  ;  "*:my-value"
  ;
  ; @return (*)
  [n]
  (if (keyword?)
      (str KEYWORD-PREFIX n)
      (return n)))

(defn unkeywordize-key
  ; @param (*) n
  ;
  ; @example
  ;  (json/unkeywordize-key :my-namespace/key)
  ;  =>
  ;  "my-namespace/key"
  ;
  ; @return (*)
  [n]
  (if (keyword?)
      (apply str (rest (str n)))
      (return n)))

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
  (cond (map?    n) (map/->values       n unkeywordize-values)
        (vector? n) (vector/->items     n unkeywordize-values)
        :else       (unkeywordize-value n)))

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
  (cond (map?    n) (map/->values     n keywordize-values)
        (vector? n) (vector/->items   n keywordize-values)
        :else       (keywordize-value n)))

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
  (cond (map?    n) (map/->values     n unkeywordize-keys)
        (vector? n) (vector/->items   n unkeywordize-keys)
        :else       (unkeywordize-key n)))

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
  (cond (map?    n) (map/->values   n keywordize-keys)
        (vector? n) (vector/->items n keywordize-keys)
        :else       (keyword        n)))
