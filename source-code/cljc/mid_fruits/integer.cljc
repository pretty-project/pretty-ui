

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.integer)



;; -- Type converters ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn to-keyword
  ; @param (integer) n
  ;
  ; @example
  ;  (integer/to-keyword 2)
  ;  =>
  ;  :2
  ;
  ; @return (keyword)
  [n]
  (keyword (str n)))
