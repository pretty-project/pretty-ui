
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.seqable)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn nonseqable?
  ; @param (*) n
  ;
  ; @example
  ;  (seqable/nonseqable? 420)
  ;  =>
  ;  true
  ;
  ; @example
  ;  (seqable/nonseqable? "420")
  ;  =>
  ;  false
  ;
  ; @return (boolean)
  [n]
  (-> n seqable? not))

(defn nonempty?
  ; @param (*) n
  ;
  ; @example
  ;  (seqable/nonempty? 420)
  ;  =>
  ;  false
  ;
  ; @example
  ;  (seqable/nonempty? "420")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (and (-> n seqable?)
       (-> n empty? not)))
