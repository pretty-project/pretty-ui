
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.16
; Description:
; Version: v0.2.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.geometry
    (:require [mid-fruits.math :as math]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn columns-count
  ; Kiszámítja hány oszlopban fér el a megadott számú elem.
  ; - Szükség szerint csökkenti az oszlopok számát aszerint, hogy az ne haladja
  ;   meg a max-columns-count paraméterként átadott értéket.
  ; - Szükség szerint csökkenti az oszlopok számát aszerint, hogy az oszlopok
  ;   összeadott szélessége ne haladja meg a max-width paraméterként átadott értéket.
  ;
  ; @param (integer) items-count
  ; @param (px) column-width
  ; @param (integer) max-columns-count
  ; @param (px) max-width
  ;
  ; @example
  ;  (geometry/columns-count 13 200 8 1980)
  ;  => 8
  ;
  ; @example
  ;  (geometry/columns-count 13 200 8 1240)
  ;  => 6
  ;
  ; @example
  ;  (geometry/columns-count 2 200 8 1980)
  ;  => 2
  ;
  ; @example
  ;  (geometry/columns-count 0 200 8 1980)
  ;  => 0
  ;
  ; @return (integer)
  [items-count column-width max-columns-count max-width]
  (let [max-columns-fit   (math/floor (/ max-width column-width))
        max-columns-count (math/minimum max-columns-count max-columns-fit)]
       (math/between! items-count 0 max-columns-count)))

(defn columns-width
  ; Kiszámítja, hogy a (geometry/columns-count ...) függvény által meghatározott
  ; számú oszlopnak mennyi az összeadott szélessége.
  ;
  ; @param (integer) items-count
  ; @param (px) column-width
  ; @param (integer) max-columns-count
  ; @param (px) max-width
  ;
  ; @example
  ;  (geometry/columns-width 13 200 8 1980)
  ;  => 1600
  ;
  ; @example
  ;  (geometry/columns-width 13 200 8 1240)
  ;  => 1200
  ;
  ; @example
  ;  (geometry/columns-width 2 200 8 1980)
  ;  => 400
  ;
  ; @example
  ;  (geometry/columns-width 0 200 8 1980)
  ;  => 0
  ;
  ; @return (integer)
  [items-count column-width max-columns-count max-width]
  (let [columns-count (columns-count items-count column-width max-columns-count max-width)]
       (* column-width columns-count)))
