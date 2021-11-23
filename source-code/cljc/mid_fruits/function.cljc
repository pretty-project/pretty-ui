
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.27
; Description:
; Version: v0.1.8



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
  ;  => "mid_fruits.vector.conj_item"
  ;
  ; @return (string)
  [var-name]
  #?(:cljs (-> (param var-name)
               (string/replace-part #"/" ".")
               (string/replace-part #"-" "_"))))

(defn invoke
  ; @param (string) function-name
  ; @param (list of *) args
  ;
  ; @example
  ;  (function/invoke "mid-fruits.vector/conj-item" [:a :b] :c)
  ;  => [:a :b :c]
  [function-name & args])
;  #?(:cljs (let [evaled (js/eval (->js function-name))]
;                (apply evaled args)]
;  #?(:clj  (apply (resolve (symbol function-name)) args)))
