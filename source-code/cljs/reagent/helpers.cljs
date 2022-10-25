
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns reagent.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component?
  ; @param (*)
  ;
  ; @example
  ;  (component? [:div "..."])
  ;  =>
  ;  false
  ;
  ; @example
  ;  (component? [my-component "..."])
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (and (-> n vector?)
       (-> n first fn?)))
