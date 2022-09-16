
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.value
    (:require [mid-fruits.candy   :refer [return]]
              [mid-fruits.keyword :as keyword]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn value
  ; @param (keyword or string) n
  ; @param (string)(opt) flag
  ;
  ; @example
  ;  (dom/value "my-namespace/my-value?")
  ;  =>
  ;  "my-namespace--my-value"
  ;
  ; @example
  ;  (dom/value :your-namespace/your-value!)
  ;  =>
  ;  "your-namespace--your-value"
  ;
  ; @example
  ;  (dom/value :our-namespace/our-value "420")
  ;  =>
  ;  "our-namespace--our-value--420"
  ;
  ; @return (string)
  [n & [flag]]
  ; A value függvény az n paraméterként átadott kulcsszó vagy string típusú
  ; értéket úgy alakítja át, hogy az megfeleljen a HTML attribute
  (let [x (cond (keyword? n) (keyword/to-string n)
                (string?  n) (return            n))]
       (letfn [(f [result tag] (case tag "." (str result "--")
                                         "/" (str result "--")
                                         "?" result
                                         "!" result
                                         ">" result
                                             (str result tag)))]
              (str (reduce f nil x)
                   (if flag (str "--" flag))))))
