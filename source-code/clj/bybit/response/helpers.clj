
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns bybit.response.helpers
    (:require [mid-fruits.reader :as reader]
              [mid-fruits.string :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn time_now->epoch-ms
  ; @param (string) n
  ;
  ; @example
  ;  (bybit/time_now->epoch-ms "1645550000.123456")
  ;  =>
  ;  1645550000123
  ;
  ; @return (integer)
  [n]
  (let [s  (string/before-first-occurence n ".")
        ms (string/after-first-occurence  n ".")]
       (reader/read-str (str s (subs ms 0 3)))))
