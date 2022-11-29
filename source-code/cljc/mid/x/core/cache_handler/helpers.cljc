
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
; a build-version aktuális értékét.
;
; Ha az uri paraméterként átadott útvonal nem "/" karakterrel kezdődik, akkor
; a függvény visszatérési értéke a változtatás nélküli uri paraméter



;; ---------------------------------------------------------------------------
;; ---------------------------------------------------------------------------

(defn cache-control-uri
  ; @param (string) uri
  ; @param (string) build-version
  ;
  ; @usage
  ;  (cache-control-uri "my-style.css" "0.4.2.0")
  ;
  ; @example
  ;  (cache-control-uri "my-style.css" "0.4.2.0")
  ;  =>
  ;  "my-style.css?v=0.4.2.0"
  ;
  ; @example
  ;  (cache-control-uri "https://example.com/style.css" "0.4.2.0")
  ;  =>
  ;  "example.com/style.css"
  ;
  ; @return (string)
  [uri build-version]
  (case (-> uri first str) "/" (str    uri "?v=" build-version)
                               (return uri)))
