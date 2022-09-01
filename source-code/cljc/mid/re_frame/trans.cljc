
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.re-frame.trans
    (:require [mid-fruits.vector :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; - A Re-Frame események függvényei a db paraméter után egy paraméter-vektort fogadnak,
;   amelynek az első eleme az esemény azonosítója, ami szinte kivétel nélkül soha
;   nincs használva.
; - A Re-Frame függvényeket a következő formula szerint lehetséges használni:
;   (db/trim-partition! db [_ partition-id])
; - Egyszerűbb, ha nem kell ezzel a vektorral baszakodni, ezért létezik
;   az r nevű függvény, amelynek az egyetlen feladata, hogy egy másik
;   formula szerint is használhatóvá teszi a Re-frame események függvényeit:
;   (r db/trim-partition! db partition-id)
(defn r
  ; @param (function) f
  ; @param (*) params
  ;
  ; @example
  ;  (r db/trim-partition! db partition-id)
  ;  =>
  ;  (db/trim-partition! db [_ partition-id])
  ;
  ; @return (*)
  [f & params]
  (let [context      (first params)
        event-vector (vector/cons-item (rest params) nil)]
       (f context event-vector)))
