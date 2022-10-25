
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.integer)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn to-keyword
  ; @param (integer) n
  ;
  ; @example
  ;  (to-keyword 2)
  ;  =>
  ;  :2
  ;
  ; @return (keyword)
  [n]
  (keyword (str n)))
