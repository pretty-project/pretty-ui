
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.03.27
; Description:
; Version: v0.3.0
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.cache-handler)



;; ---------------------------------------------------------------------------
;; ---------------------------------------------------------------------------

; A cache-control-uri függvény a szerveren tárolt fájlok útvonalához csatolja
; az applikáció aktuális verziószámát.
; Ha az applikáció verziószáma megváltozik, akkor a cache-control-uri
; függvénnyel elért fájlok kötelezően frissülnek a kliens eszközén.



;; ---------------------------------------------------------------------------
;; ---------------------------------------------------------------------------

(defn cache-control-uri
  ; @param (string) uri
  ; @param (string) version
  ;
  ; @example
  ;  (cache-control-uri "example.com/style.css" "1.2.3")
  ;  => "example.com/style.css?v=1.2.3"
  ;
  ; @return (string)
  [uri version]
  (str uri "?v=" version))
