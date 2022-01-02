
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.08
; Description:
; Version: v0.6.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.json
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.string  :as string]
              [mid-fruits.map     :as map]
              [mid-fruits.vector  :as vector]))



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



;; -- Keywordize / unkeywordize / ... key -------------------------------------
;; ----------------------------------------------------------------------------

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
  (keyword/to-string n))

(defn underscore-key
  ; @param (*) n
  ;
  ; @example
  ;  (json/underscore-key :my-namespace/key)
  ;  =>
  ;  :my_namespace/key
  ;
  ; @return (*)
  [n]
  (cond (string?  n) (string/replace-part n "-" "_")
        (keyword? n) (-> n name underscore-key keyword)
        :else     n))

(defn hyphenize-key
  ; @param (*) n
  ;
  ; @example
  ;  (json/hyphenize-key :my_namespace/key)
  ;  =>
  ;  :my-namespace/key
  ;
  ; @return (*)
  [n]
  (cond (string?  n) (string/replace-part n "_" "-")
        (keyword? n) (-> n name hyphenize-key keyword)
        :else     n))



;; -- Keywordize / unkeywordize / ... value -----------------------------------
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
       (= KEYWORD-PREFIX (str (nth n 0)))
       (= ":"            (str (nth n 1)))))

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
      (->     n (subs 2) keyword)
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
  (if (keyword?           n)
      (str KEYWORD-PREFIX n)
      (return             n)))



;; -- Keywordize / unkeywordize / ... keys ------------------------------------
;; ----------------------------------------------------------------------------

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
  (cond (map?    n) (map/->>keys    n unkeywordize-keys)
        (vector? n) (vector/->items n unkeywordize-keys)
        ; Az unkeywordize-key függvény csak keyword típusokat módosít, ezért nincs szükség további
        ; típus-vizsgálatra!
        :else (unkeywordize-key n)))

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
  (cond (map?    n) (map/->>keys    n keywordize-keys)
        (vector? n) (vector/->items n keywordize-keys)
        :else       (keyword        n)))

(defn underscore-keys
  ; @param (*) n
  ;
  ; @example
  ;  (json/underscore-keys {:my-namespace/key :my-value})
  ;  =>
  ;  {:my_namespace/key :my-value}
  ;
  ; @return (*)
  [n]
  (cond (map?    n) (map/->>keys    n underscore-keys)
        (vector? n) (vector/->items n underscore-keys)
        :else       (underscore-key n)))

(defn hyphenize-keys
  ; @param (*) n
  ;
  ; @example
  ;  (json/hyphenize-keys {:my_namespace/key :my-value})
  ;  =>
  ;  {:my-namespace/key :my-value}
  ;
  ; @return (*)
  [n]
  (cond (map?    n) (map/->>keys    n hyphenize-keys)
        (vector? n) (vector/->items n hyphenize-keys)
        :else       (hyphenize-key  n)))



;; -- Keywordize / unkeywordize / ... values ----------------------------------
;; ----------------------------------------------------------------------------

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
  ;  (json/unkeywordize-values {:a :b :c [:d "e"] :f {:g "h" :i :j}})
  ;  =>
  ;  {:a "*:b" :c ["*:d" "e"] :f {:g "*h" :i "*:j"}}
  ;
  ; @return (*)
  [n]
  (cond (map?    n) (map/->>values  n unkeywordize-values)
        (vector? n) (vector/->items n unkeywordize-values)
        ; Az unkeywordize- függvény csak keyword típusokat módosít, ezért nincs szükség további
        ; típus-vizsgálatra!
        :else (unkeywordize-value n)))

(defn keywordize-values
  ; XXX#5914
  ;
  ; @param (*) n
  ;
  ; @example
  ;  (json/keywordize-values {:a "*:b" :c ["*:d" "e"] :f {:g "*h" :i "*:j"}})
  ;  =>
  ;  {:a :b :c [:d "e"] :f {:g "h" :i :j}}
  ;
  ; @return (*)
  [n]
  (cond (map?    n) (map/->>values  n keywordize-values)
        (vector? n) (vector/->items n keywordize-values)
        ; A keywordize-value függvény csak string típusokat módosít, ezért nincs szükség további
        ; típus-vizsgálatra!
        :else (keywordize-value n)))
