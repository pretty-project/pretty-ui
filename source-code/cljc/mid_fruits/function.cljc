

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.function
    (:require [mid-fruits.candy  :refer [param]]
              [mid-fruits.string :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ->js
  ; @param (string) var-name
  ;
  ; @example
  ;  (function/->js "mid-fruits.vector/conj-item")
  ;  =>
  ;  "mid_fruits.vector.conj_item"
  ;
  ; @return (string)
  [var-name]
  #?(:cljs (-> var-name (string/replace-part #"/" ".")
                        (string/replace-part #"-" "_"))))

(defn invoke
  ; @param (string) function-name
  ; @param (list of *) args
  ;
  ; @example
  ;  (function/invoke "mid-fruits.vector/conj-item" [:a :b] :c)
  ;  =>
  ;  [:a :b :c]
  [function-name & args])
;  #?(:cljs (let [evaled (js/eval (->js function-name))]
;                (apply evaled args)]
;  #?(:clj  (apply (resolve (symbol function-name)) args)))
