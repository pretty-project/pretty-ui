
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.json
    (:require [mid-fruits.candy   :refer [return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.mixed   :as mixed]
              [mid-fruits.string  :as string]
              [mid-fruits.map     :as map]
              [mid-fruits.vector  :as vector]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
; XXX#5914
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

(defn keywordize-key
  ; @param (*) n
  ;
  ; @example
  ;  (json/keywordize-key "my-namespace/key")
  ;  =>
  ;  :my-namespace/key
  ;
  ; @return (*)
  [n]
  (keyword n))

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
        :return   n))

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
        :return   n))



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
  (and    (string? n)
       (> (count   n) 2)
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
  ;  (json/unkeywordize-value :my-value)
  ;  =>
  ;  "*:my-value"
  ;
  ; @return (*)
  [n]
  (if (keyword?           n)
      (str KEYWORD-PREFIX n)
      (return             n)))



;; -- Trim ... value ----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn trim-value
  ; @param (*) n
  ;
  ; @example
  ;  (json/trim-value " My value ")
  ;  =>
  ;  "My value"
  ;
  ; @return (*)
  [n]
  (if (string?     n)
      (string/trim n)
      (return      n)))



;; -- Parseint ... value ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn parseint-value
  ; @param (*) n
  ;
  ; @example
  ;  (json/parseint-value "420")
  ;  =>
  ;  420
  ;
  ; @return (*)
  [n]
  (mixed/parse-whole-number n))



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
  ; Az unkeywordize-key függvény csak keyword típusokat módosít, ezért nincs szükség további
  ; típus-vizsgálatra!
  (cond (map?    n) (map/->>keys      n unkeywordize-keys)
        (vector? n) (vector/->items   n unkeywordize-keys)
        :return     (unkeywordize-key n)))

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
        :return     (keywordize-key n)))

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
        :return     (underscore-key n)))

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
        :return     (hyphenize-key  n)))



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
  ; Az unkeywordize- függvény csak keyword típusokat módosít, ezért nincs szükség további
  ; típus-vizsgálatra!
  (cond (map?    n) (map/->>values      n unkeywordize-values)
        (vector? n) (vector/->items     n unkeywordize-values)
        :return     (unkeywordize-value n)))

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
  ; A keywordize-value függvény csak string típusokat módosít, ezért nincs szükség további
  ; típus-vizsgálatra!
  (cond (map?    n) (map/->>values    n keywordize-values)
        (vector? n) (vector/->items   n keywordize-values)
        :return     (keywordize-value n)))



;; -- Trim ... values ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn trim-values
  ; @param (*) n
  ;
  ; @example
  ;  (json/trim-values {:a "b " :c [" d " "e"] :f {:g " h"}})
  ;  =>
  ;  {:a "b" :c ["d" "e"] :f {:g "h"}}
  ;
  ; @return (*)
  [n]
  (cond (map?    n) (map/->>values  n trim-values)
        (vector? n) (vector/->items n trim-values)
        :return     (trim-value     n)))



;; -- Parseint ... values -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn parseint-values
  ; @param (*) n
  ;
  ; @example
  ;  (json/parseint-values {:a "0" :c ["1"] :f {:g "2"}})
  ;  =>
  ;  {:a 0 :c [1] :f {:g 2}}
  ;
  ; @return (*)
  [n]
  (cond (map?    n) (map/->>values  n parseint-values)
        (vector? n) (vector/->items n parseint-values)
        :return     (parseint-value n)))



;; -- Remove blank ... values -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-blank-values
  ; @param (*) n
  ;
  ; @example
  ;  (json/remove-blank-values {:a "" :c [] :f {:g nil}})
  ;  =>
  ;  {}
  ;
  ; @return (*)
  [n]
  ; A remove-blank-values függvény az üres értékek eltávolítása után rekurzívan
  ; meghívja önmagát addig, amíg már nem okoz újabb változást az n értékében,
  ; így biztosítva, hogy ne hagyjon maga után üres értékeket.
  ; Pl.: Ha az n térkép egyik értéke egy vektor, amiben egy üres térkép van,
  ;      akkor a rekurzió első iterációjakor a vektor még nem üres,
  ;      de az üres térkép eltávolítása után az azt tartalmazó vektor is üressé
  ;      válik és ezért a következő iterációban már eltávolítható.
  (letfn [(r-f [x] (vector/contains-item? [{} [] () nil ""] x))]
         (let [result (map/->>remove-values-by n r-f)]
              (if (=                 n result)
                  (return              result)
                  (remove-blank-values result)))))
