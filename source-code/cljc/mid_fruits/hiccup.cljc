
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
              [mid-fruits.css     :as css]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.random  :as random]
              [mid-fruits.string  :as string]
              [mid-fruits.vector  :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn hiccup?
  ; @param (*) n
  ;
  ; @return (boolean)
  [n]
  (and (-> n vector?)
       (-> n first keyword?)))

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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn walk
  ; @param (hiccup) n
  ; @param (function) f
  ;
  ; @example
  ;  (hiccup/walk [:td [:p {:style {:color "red"}}]]
  ;              #(conj % "420"))
  ;  =>
  ;  [:td [:p {:style {:color "red"}} "420"] "420"]
  ;
  ; @return (hiccup)
  [n f]
  (if (hiccup? n)
      (letfn [(walk-f [%1 %2] (conj %1 (walk %2 f)))]
             (reduce walk-f [] (f n)))
      (return n)))

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
      (letfn [(f [%1 %2] (conj %1 ^{:key (random/generate-uuid)} [:span %2]))]
             (reduce f container n))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-attributes
  ; @param (hiccup) n
  ;
  ; @example
  ;  (hiccup/to-string [:div {:style {:color "red"}} "Hello World!"])
  ;  =>
  ;  {:style {:color "red"}}
  ;
  ; @return (map)
  [n]
  (if (vector? n)
      (if-let [attributes (vector/nth-item n 1)]
              (if (map?   attributes)
                  (return attributes)))))

(defn get-style
  ; @param (hiccup) n
  ;
  ; @example
  ;  (hiccup/to-style [:div {:style {:color "red"}} "Hello World!"])
  ;  =>
  ;  {:color "red"}
  ;
  ; @return (map)
  [n]
  (if-let [attributes (get-attributes n)]
          (:style attributes)))

(defn set-style
  ; @param (hiccup) n
  ;
  ; @example
  ;  (hiccup/to-string [:div {:style {:color "red"}} "Hello World!"])
  ;  =>
  ;  {:style {:color "red"}}
  ;
  ; @return (map)
  [n style]
  (if (vector? n)
      (if-let [attributes (get-attributes n)]
              (assoc-in n [1 :style] style)
              (vector/inject-item n 1 {:style style}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
                              :return  o))]
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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn unparse-css
  ; @param (hiccup) n
  ;
  ; @example
  ;  (hiccup/unparse-css [:td [:p {:style {:color "red"}}]])
  ;  =>
  ;  [:td [:p {:style "color: red;"}]]
  ;
  ; @example
  ;  (hiccup/unparse-css [:td [:p {:style "color: red;"}]])
  ;  =>
  ;  [:td [:p {:style "color: red;"}]]
  ;
  ; @return (hiccup)
  [n]
  (letfn [(f [n] (let [style (get-style n)]
                      (if (map? style)
                          (set-style n (css/unparse style))
                          (return    n))))]
         (walk n f)))
