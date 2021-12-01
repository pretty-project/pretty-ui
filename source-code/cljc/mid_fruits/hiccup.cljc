
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.06
; Description:
; Version: v0.3.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.hiccup
    (:require [mid-fruits.candy  :refer [param]]
              [mid-fruits.random :as random]
              [mid-fruits.string :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn hiccup?
  ; @param (*) n
  ;
  ; @return (boolean)
  [n]
  (boolean (and (vector? n)
                (keyword? (first n)))))

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
      (reduce (fn [%1 %2]
                  (vec (conj %1 ^{:key (random/generate-react-key)}[:span %2])))
              (param container)
              (param n))))

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
  (= (first n)
     (param tag-name)))

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
  (reduce (fn [%1 %2]
              (cond (string? %2) (str %1 %2)
                    (vector? %2) (str %1 (to-string %2))
                    :else        (str %1)))
          (param string/empty-string)
          (param n)))

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
