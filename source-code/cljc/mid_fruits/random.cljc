
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.random
    (:require [mid-fruits.candy :refer [param]]
              [math.api         :as math]))



;; -- BUG#5570 ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A cljs.reader/read-string függvény nem szereti azokat a névteres kulcsszavakat,
; amelyekben a név első karaktere egy számjegy (pl. :namespace/0abc).
; Ezért a generált kulcsszavak nevének első karaktere egy betű kell legyen!
;
; @consant (string)
(def NAME-PREFIX "q")



;; -- Untyped generators ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn generate-boolean
  ; @usage
  ;  (generate-boolean)
  ;
  ; @param (boolean)
  []
  (-> 2 rand-int zero?))



;; -- Typed generators --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn generate-uuid
  ; @usage
  ;  (generate-uuid)
  ;
  ; @return (string)
  []
  #?(:cljs (str (random-uuid))
     :clj  (str (java.util.UUID/randomUUID))))

(defn generate-string
  ; @usage
  ;  (generate-string)
  ;
  ; @return (string)
  []
  (generate-uuid))

(defn generate-keyword
  ; @param (string)(opt) namespace
  ;
  ; @usage
  ;  (generate-keyword)
  ;
  ; @return (keyword)
  ([]
   ; BUG#5570
   (keyword (str NAME-PREFIX (generate-uuid))))

  ([namespace]
   ; BUG#5570
   (keyword (str namespace "/" (str NAME-PREFIX (generate-uuid))))))

(defn generate-namespaced-keyword
  ; @return (namespaced keyword)
  []
  ; BUG#5570
  (keyword (str (generate-uuid) "/" (str NAME-PREFIX (generate-uuid)))))

(defn generate-react-key
  ; @usage
  ;  (generate-react-key)
  ;
  ; @return (string)
  []
  (generate-uuid))

(defn generate-number
  ; @param (integer) digits
  ;
  ; @example
  ;  (generate-number 3)
  ;  =>
  ;  420
  ;
  ; @return (integer)
  [digits]
  ; A (-> 9 rand inc) függvény kimenete egy lebegőpontos érték 1 és 10 között
  ;
  ; A (generate-number n) fügvény visszatérési értéke egy 1 és 9.99 között érték szorozva
  ; 10 (n - 1) hatványával, integer típusra alakítva.
  (int (* (math/power 10 (dec digits)) (min 9.999 (-> 9 rand inc)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn pick-vector-item
  ; @param (vector) n
  ;
  ; @usage
  ;  (pick-vector-item [:a :b :c]
  ;  =>
  ;  :a
  ;
  ; @usage
  ;  (pick-vector-item [:a :b :c]
  ;  =>
  ;  :c
  ;
  ; @return (*)
  [n]
  (nth n (-> n count rand-int)))
