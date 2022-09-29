
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.hiccup
    (:require [mid-fruits.candy   :refer [return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.random  :as random]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn hiccup?
  ; @param (*) n
  ;
  ; @return (boolean)
  [n]
  (and (-> n vector?)
       (-> n first keyword?)))

(defn explode
  ; @param (string) n
  ; @param (hiccup) container
  ;
  ; @example
  ;  (hiccup/explode "ab" [:div])
  ;  =>
  ;  [:div [:span "a"]
  ;        [:span "b"])
  ;
  ; @return (nil or hiccup)
  [n container]
  (if (and (string? n)
           (hiccup? container))
      (reduce #(conj %1 ^{:key (random/generate-uuid)} [:span %2])
               container n)))

(defn tag-name?
  ; @param (hiccup) n
  ; @param (keyword) tag-name
  ;
  ; @example
  ;  (hiccup/tag-name? [:div "Hello World!"] :div)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n tag-name]
  (= (first n) tag-name))

(defn to-string
  ; @param (hiccup) n
  ; @param (string)(opt) delimiter
  ;  Default: " "
  ;
  ; @example
  ;  (hiccup/to-string [:div "Hello" [:strong "World!"]])
  ;  =>
  ;  "Hello World!"
  ;
  ; @return (string)
  ([n]
   (to-string n " "))

  ([n delimiter]
   (letfn [(to-string-f [o x]
                        (cond (string? x) (str o delimiter  x)
                              (vector? x) (str o (to-string x))
                              :else o))]
          (reduce to-string-f "" n))))

(defn content-length
  ; @param (hiccup) n
  ;
  ; @example
  ;  (hiccup/content-length [:div "Hello World!"])
  ;  =>
  ;  12
  ;
  ; @return (integer)
  [n]
  (count (to-string n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn value
  ; @param (keyword or string) n
  ; @param (string)(opt) flag
  ;
  ; @example
  ;  (hiccup/value "my-namespace/my-value?")
  ;  =>
  ;  "my-namespace--my-value"
  ;
  ; @example
  ;  (hiccup/value :your-namespace/your-value!)
  ;  =>
  ;  "your-namespace--your-value"
  ;
  ; @example
  ;  (hiccup/value :our-namespace/our-value "420")
  ;  =>
  ;  "our-namespace--our-value--420"
  ;
  ; @return (string)
  [n & [flag]]
  ; A value függvény az n paraméterként átadott kulcsszó vagy string típusú
  ; értéket úgy alakítja át, hogy az megfeleljen egy HTML attribútum értékeként.
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
