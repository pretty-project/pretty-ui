
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.03.27
; Description:
; Version: v0.3.8
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.cache-handler)



;; ---------------------------------------------------------------------------
;; ---------------------------------------------------------------------------

; A cache-control-uri függvény a szerveren tárolt fájlok útvonalához csatolja
; az app-build aktuális értékét.
; Ha az app-build értéke megváltozik, akkor a cache-control-uri függvénnyel
; elért fájlok kötelezően frissülnek a kliens eszközén.



;; ---------------------------------------------------------------------------
;; ---------------------------------------------------------------------------

(defn cache-control-uri
  ; @param (string) uri
  ; @param (string) version
  ;
  ; @example
  ;  (cache-control-uri "example.com/style.css" "1.2.3")
  ;  =>
  ;  "example.com/style.css?v=1.2.3"
  ;
  ; @return (string)
  [uri version]
  (str uri "?v=" version))
