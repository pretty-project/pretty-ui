
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.geometry
    (:require [mid-fruits.math :as math]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-count
  ; Kiszámítja hány oszlopban fér el a megadott számú elem.
  ; - Szükség szerint csökkenti az oszlopok számát aszerint, hogy az ne haladja
  ;   meg a max-column-count paraméterként átadott értéket.
  ; - Szükség szerint csökkenti az oszlopok számát aszerint, hogy az oszlopok
  ;   összeadott szélessége ne haladja meg a max-width paraméterként átadott értéket.
  ;
  ; @param (integer) item-count
  ; @param (px) column-width
  ; @param (integer) max-column-count
  ; @param (px) max-width
  ;
  ; @example
  ;  (geometry/column-count 13 200 8 1980)
  ;  =>
  ;  8
  ;
  ; @example
  ;  (geometry/column-count 13 200 8 1240)
  ;  =>
  ;  6
  ;
  ; @example
  ;  (geometry/column-count 2 200 8 1980)
  ;  =>
  ;  2
  ;
  ; @example
  ;  (geometry/column-count 0 200 8 1980)
  ;  =>
  ;  0
  ;
  ; @return (integer)
  [item-count column-width max-column-count max-width]
  (let [max-columns-fit  (math/floor (/ max-width column-width))
        max-column-count (math/minimum max-column-count max-columns-fit)]
       (math/between! item-count 0 max-column-count)))

(defn columns-width
  ; Kiszámítja, hogy a (geometry/column-count ...) függvény által meghatározott
  ; számú oszlopnak mennyi az összeadott szélessége.
  ;
  ; @param (integer) item-count
  ; @param (px) column-width
  ; @param (integer) max-column-count
  ; @param (px) max-width
  ;
  ; @example
  ;  (geometry/columns-width 13 200 8 1980)
  ;  =>
  ;  1600
  ;
  ; @example
  ;  (geometry/columns-width 13 200 8 1240)
  ;  =>
  ;  1200
  ;
  ; @example
  ;  (geometry/columns-width 2 200 8 1980)
  ;  =>
  ;  400
  ;
  ; @example
  ;  (geometry/columns-width 0 200 8 1980)
  ;  =>
  ;  0
  ;
  ; @return (integer)
  [item-count column-width max-column-count max-width]
  (let [column-count (column-count item-count column-width max-column-count max-width)]
       (* column-width column-count)))
