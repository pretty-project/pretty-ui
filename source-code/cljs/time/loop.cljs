
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns time.loop
    (:require [time.schedule :as schedule]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reduce-interval
  ; @param (function) f
  ; @param (collection) coll
  ; @param (integer) interval
  ;
  ; @example
  ;  (time/reduce-interval my-function [:a :b :c] 500)
  ;  =>
  ;  (time/set-timeout! #(my-function :a)    0)
  ;  (time/set-timeout! #(my-function :b)  500)
  ;  (time/set-timeout! #(my-function :c) 1000)
  ;
  ; @return (*)
  [f coll interval]
  (letfn [(reduce-interval-f [lap item]
                             (schedule/set-timeout! #(f item)
                                                     (* lap interval))
                             (inc lap))]
         (reduce reduce-interval-f 0 coll)))
