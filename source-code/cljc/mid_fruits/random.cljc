
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.11
; Description:
; Version: v0.2.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.random
    (:require [mid-fruits.candy :refer [param]]))



;; -- Untyped generators ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn generate-boolean
  ; @usage
  ;  (generate-boolean)
  ;  (random/generate-boolean)
  ;
  ; @param (boolean)
  []
  (= (rand-int 2)
     (param    0)))



;; -- Typed generators --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- generate-uuid
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  []
  #?(:cljs (str (random-uuid))
     :clj  (str (java.util.UUID/randomUUID))))

(defn generate-string
  ; @usage
  ;  (generate-string)
  ;  (random/generate-string)
  ;
  ; @return (string)
  []
  (generate-uuid))

(defn generate-strings
  ; @param (integer) n
  ;
  ; @example
  ;  (random/generate-strings 2)
  ;  =>
  ;  ["a991876d-6887-4f04-8525-328f090cde5" "78f1ff75-e91f-41a8-ba00-be949f9c2bed"]
  ;
  ; @return (strings in vector)
  [n]
  (reduce (fn [generated-strings _]
              (let [generated-string (generate-string)]
                   (vec (conj generated-strings generated-string))))
          (param [])
          (range n)))

(defn generate-keyword
  ; @param (string)(opt) namespace
  ;
  ; @usage
  ;  (generate-keyword)
  ;  (random/generate-keyword)
  ;
  ; @return (keyword)
  ([]
   (keyword (generate-string)))

  ([namespace]
   (keyword (str namespace "/" (generate-string)))))

(defn generate-keywords
  ; @param (integer) n
  ;
  ; @example
  ;  (random/generate-strings 2)
  ;  =>
  ;  [:a991876d-6887-4f04-8525-328f090cde5 :78f1ff75-e91f-41a8-ba00-be949f9c2bed]
  ;
  ; @return (keywords in vector)
  [n]
  (reduce (fn [generated-keywords _]
              (let [generated-keyword (generate-keyword)]
                   (vec (conj generated-keywords generated-keyword))))
          (param [])
          (range n)))

(defn generate-namespaced-keyword
  ; @return (namespaced keyword)
  []
  (keyword (str (generate-string) "/" (generate-string))))

(defn generate-react-key
  ; @usage
  ;  (generate-react-key)
  ;  (random/generate-react-key)
  ;
  ; @return (string)
  []
  (generate-string))
