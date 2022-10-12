
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.css
    (:require [mid-fruits.string :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn join-class
  ; @param (list of keyword or keywords in vector)
  ;
  ; @example
  ;  (css/join-class :my-class [:your-class] :our-class)
  ;  =>
  ;  [:my-class :your-class :our-class]
  ;
  ; @return (keywords in vector)
  [& xyz]
  (letfn [(join-class-f [o x] (cond (vector?  x) (concat o x)
                                    (keyword? x) (conj   o x)
                                    :return   o))]
         (reduce join-class-f [] xyz)))

(defn calc
  ; @param (string) n
  ;
  ; @example
  ;  (css/calc "100% - 100px")
  ;  =>
  ;  "calc(100% - 100px)"
  ;
  ; @return (string)
  [n]
  (str "calc(" n ")"))

(defn ms
  ; @param (ms) n
  ;
  ; @example
  ;  (css/ms 500)
  ;  =>
  ;  "500ms"
  ;
  ; @return (string)
  [n]
  (str n "ms"))

(defn s
  ; @param (s) n
  ;
  ; @example
  ;  (css/s 3)
  ;  =>
  ;  "3s"
  ;
  ; @return (string)
  [n]
  (str n "s"))

(defn percent
  ; @param (percent) n
  ;
  ; @example
  ;  (css/percent 100)
  ;  =>
  ;  "100%"
  ;
  ; @return (string)
  [n]
  (str n "%"))

(defn px
  ; @param (string) n
  ;
  ; @example
  ;  (css/px "100")
  ;  =>
  ;  "100px"
  ;
  ; @return (string)
  [n]
  (str n "px"))

(defn rotate
  ; @param (string) n
  ;
  ; @example
  ;  (css/rotate "120")
  ;  =>
  ;  "rotate(120deg)"
  ;
  ; @return (string)
  [n]
  (str "rotate(" n "deg)"))

(defn rotate-x
  ; @param (string) n
  ;
  ; @example
  ;  (css/rotate-x "120")
  ;  =>
  ;  "rotateX(120deg)"
  ;
  ; @return (string)
  [n]
  (str "rotateX(" n "deg)"))

(defn rotate-y
  ; @param (string) n
  ;
  ; @example
  ;  (css/rotate-y "120")
  ;  =>
  ;  "rotateY(120deg)"
  ;
  ; @return (string)
  [n]
  (str "rotateY(" n "deg)"))

(defn rotate-z
  ; @param (string) n
  ;
  ; @example
  ;  (css/rotate-z "120")
  ;  =>
  ;  "rotateZ(120deg)"
  ;
  ; @return (string)
  [n]
  (str "rotateZ(" n "deg)"))

(defn scale
  ; @param (string) n
  ;
  ; @example
  ;  (css/scale "1.1")
  ;  =>
  ;  "scale(1.1)"
  ;
  ; @return (string)
  [n]
  (str "scale(" n ")"))

(defn translate
  ; @param (string) n
  ; @param (string)(opt) suffix
  ;
  ; @example
  ;  (css/translate "120" "px")
  ;  =>
  ;  "translate(120px)"
  ;
  ; @example
  ;  (css/translate "120px")
  ;  =>
  ;  "translate(120px)"
  ;
  ; @return (string)
  [n & [suffix]]
  (str "translate(" n suffix ")"))

(defn translate-x
  ; @param (string) n
  ; @param (string)(opt) suffix
  ;
  ; @example
  ;  (css/translate-x "120" "px")
  ;  =>
  ;  "translateX(120px)"
  ;
  ; @example
  ;  (css/translate-x "120px")
  ;  =>
  ;  "translateX(120px)"
  ;
  ; @return (string)
  [n & [suffix]]
  (str "translateX(" n suffix ")"))

(defn translate-y
  ; @param (string) n
  ; @param (string)(opt) suffix
  ;
  ; @example
  ;  (css/translate-y "120" "px")
  ;  =>
  ;  "translateY(120px)"
  ;
  ; @example
  ;  (css/translate-y "120px")
  ;  =>
  ;  "translateY(120px)"
  ;
  ; @return (string)
  [n & [suffix]]
  (str "translateY(" n suffix ")"))

(defn translate-z
  ; @param (string) n
  ; @param (string)(opt) suffix
  ;
  ; @example
  ;  (css/translate-z "120" "px")
  ;  =>
  ;  "translateZ(120px)"
  ;
  ; @example
  ;  (css/translate-z "120px")
  ;  =>
  ;  "translateZ(120px)"
  ;
  ; @return (string)
  [n & [suffix]]
  (str "translateZ(" n suffix ")"))

(defn url
  ; @param (string) n
  ;
  ; @example
  ;  (css/url "/my-file.ext")
  ;  =>
  ;  "url(/my-file.ext)"
  ;
  ; @return (string)
  [n]
  (str "url(" n ")"))

(defn value
  ; @param (string or integer) n
  ; @param (string) unit
  ;  "%", "px", "rem", ...
  ;
  ; @example
  ;  (css/value 180 "%")
  ;  =>
  ;  "180%"
  ;
  ; @return (string)
  [n unit]
  (str n unit))

(defn var
  ; @param (string) n
  ;
  ; @example
  ;  (css/var "my-var")
  ;  =>
  ;  "var( --my-var )"
  ;
  ; @return (string)
  [n]
  (str "var( --" n " )"))

(defn horizontal-padding
  ; @param (string) n
  ;
  ; @example
  ;  (css/horizontal-padding "12px")
  ;  =>
  ;  "12px 0"
  ;
  ; @return (string)
  [n]
  (str n " 0"))

(defn vertical-padding
  ; @param (string) n
  ;
  ; @example
  ;  (css/vertical-padding "12px")
  ;  =>
  ;  "0 12px"
  ;
  ; @return (string)
  [n]
  (str "0 " n))

(defn horizontal-margin
  ; @param (string) n
  ;
  ; @example
  ;  (css/horizontal-margin "12px")
  ;  =>
  ;  "12px 0"
  ;
  ; @return (string)
  [n]
  (str n " 0"))

(defn vertical-margin
  ; @param (string) n
  ;
  ; @example
  ;  (css/vertical-margin "12px")
  ;  =>
  ;  "0 12px"
  ;
  ; @return (string)
  [n]
  (str "0 " n))

(defn linear-gradient
  ; @param (string) direction
  ; @param (list of strings) color-stops
  ;
  ; @example
  ;  (css/linear-gradient "0deg" "red" "green" "blue")
  ;  =>
  ;  "linear-gradient(0deg, red, green, blue)"
  ;
  ; @return (string)
  [direction & color-stops]
  (let [color-stops (string/join color-stops ", ")]
       (str "linear-gradient("direction", "color-stops")")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn unparse
  ; @param (map) n
  ;
  ; @example
  ;  (css/unparse {:opacity 1 :width "100%"})
  ;  =>
  ;  "opacity: 1; width: 100%;"
  ;
  ; @return (string)
  [n]
  (letfn [(f [style k v] (str style (name k) ": " (if (keyword? v) (name v) v) "; "))]
         (string/trim (reduce-kv f "" n))))
