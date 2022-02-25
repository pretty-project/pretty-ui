
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.hiccup
    (:require [mid-fruits.random :as random]))



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
  ;
  ; @example
  ;  (hiccup/to-string [:div "Hello " [:strong "World!"]])
  ;  =>
  ;  "Hello World!"
  ;
  ; @return (string)
  [n]
  (letfn [(to-string-f [o x]
                       (cond (string? x) (str o x)
                             (vector? x) (str o (to-string x))
                             :else o))]
         (reduce to-string-f "" n)))

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
