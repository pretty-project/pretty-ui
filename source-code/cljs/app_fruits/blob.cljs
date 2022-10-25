
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-fruits.blob)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn to-object-url
  ; @param (object) blob
  ;
  ; @usage
  ;  (to-object-url ...) 
  ;
  ; @return (object)
  [blob]
  (.createObjectURL js/URL blob))
