
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.core.cache-handler.helpers
    (:require [candy.api :refer [param return]]))



;; ---------------------------------------------------------------------------
;; ---------------------------------------------------------------------------

; A cache-control-uri függvény a szerveren tárolt fájlok útvonalához csatolja
; az app-build aktuális értékét.
;
; Ha az uri paraméterként átadott útvonal nem "/" karakterrel kezdődik, akkor
; a függvény visszatérési értéke a változtatás nélküli uri paraméter



;; ---------------------------------------------------------------------------
;; ---------------------------------------------------------------------------

(defn cache-control-uri
  ; @param (string) uri
  ; @param (string) version
  ;
  ; @example
  ;  (cache-control-uri "https://example.com/style.css" "1.2.3")
  ;  =>
  ;  "example.com/style.css"
  ;
  ; @example
  ;  (cache-control-uri "my-style.css" "1.2.3")
  ;  =>
  ;  "my-style.css?v=1.2.3"
  ;
  ; @return (string)
  [uri version]
  (case (-> uri first str) "/" (str    uri "?v=" version)
                               (return uri)))
