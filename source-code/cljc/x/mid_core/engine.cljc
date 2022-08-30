
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.engine
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.random  :as random]))




;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dom-value
  ; @param (keyword or string) n
  ; @param (string)(opt) flag
  ;
  ; @example
  ;  (a/dom-value "my-namespace/my-value?")
  ;  =>
  ;  "my-namespace--my-value"
  ;
  ; @example
  ;  (a/dom-value :your-namespace/your-value!)
  ;  =>
  ;  "your-namespace--your-value"
  ;
  ; @example
  ;  (a/dom-value :our-namespace/our-value "420")
  ;  =>
  ;  "our-namespace--our-value--420"
  ;
  ; @return (string)
  [n & [flag]]
  (let [x (cond (keyword? n) (keyword/to-string n)
                (string?  n) (return            n))]
       (str (reduce #(case %2 "." (str %1 "--") "/" (str %1 "--") "?" %1 "!" %1 ">" %1 (str %1 %2)) nil x)
            (if flag (str "--" flag)))))

(defn id
  ; @param (keyword or nil)(opt) n
  ;
  ; @example
  ;  (a/id)
  ;  =>
  ;  :0ce14671-e916-43ab-b057-0939329d4c1b
  ;
  ; @example
  ;  (a/id :my-keyword)
  ;  =>
  ;  :my-keyword
  ;
  ; @example
  ;  (a/id "my-string")
  ;  =>
  ;  :0ce14671-e916-43ab-b057-0939329d4c1b
  ;
  ; @return (keyword)
  ([]   (random/generate-keyword))
  ([n] (if (keyword? n)
           (return   n)
           (random/generate-keyword))))

(defn get-namespace
  ; @param (namespaced keyword) sample
  ;
  ; @usage
  ;  (a/get-namespace ::this)
  ;
  ; @return (keyword)
  [sample]
  (keyword/get-namespace sample))
