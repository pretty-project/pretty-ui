
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.27
; Description:
; Version: v0.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.format
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.string :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-number
  ; @warning
  ;  Nagy mennyiségben és gyakori frissítéssel megjelenített számok – group-number függvénnyel való
  ;  csoportosítával – megjelenítésekor a függvény további optimalizására is szükség lehet.
  ;
  ;  A számjegyek csoportosításánál használt elválasztó a white-space karakter (" "),
  ;  a csoportok mérete pedig 3 karakterben van rögzítve. Ezen értékek paraméterként
  ;  nem átadhatók, ezzel is csökkentve a függvény számításikapacitás-igényét.
  ;
  ; @param (number or string) n
  ;
  ; @example
  ;  (format/group-number 4200.5)
  ;  =>
  ;  "4 200.5"
  ;
  ; @return (string)
  [n]
       ;base:        az n string első (kizárólag) számjegyekből álló blokkja
       ;group-count: a base string hány darab három karakteres blokkra osztható
       ;offset:      a base string három karakteres blokkokra osztása után hány karakter marad ki (a base string elején)
  (let [base        (re-find #"\d+" n)
        group-count (quot (count base) 3)
        offset      (-    (count base) (* 3 group-count))]
            ; Abban az esetben, ha az offset értéke 0, mert a base karaktereinek száma hárommal osztható,
            ; szükséges a ciklus kimeneti értékének elejéről a felesleges elválasztó karaktert eltávolítani.
       (str (string/trim (reduce (fn [result dex]
                                     (let [x (+ offset (* 3 dex))]
                                          (str result " " (subs base x (+ x 3)))))
                                 (subs base 0 offset)
                                 (range group-count)))
            ; A csoportosítás kimenetéhez szükséges hozzáfűzni az n string nem csoportosított részét
            ; (a base string után következő részt)
            (subs n (count base)))))

(defn leading-zeros
  ; @param (integer or string) n
  ; @param (integer) length
  ;
  ; @example
  ;  (format/leading-zeros 7 3)
  ;  =>
  ;  "007"
  ;
  ; @return (string)
  [n length]
  (loop [x (str n)]
        (if (< (string/length x) length)
            (recur (str "0" x))
            (return x))))

(defn trailing-zeros
  ; @param (integer or string) n
  ; @param (integer)(opt) length
  ;
  ; @example
  ;  (format/trailing-zeros 7 3)
  ;  =>
  ;  "700"
  ;
  ; @return (string)
  [n length]
  (loop [x (str n)]
        (if (< (string/length x) length)
            (recur (str x "0"))
            (return x))))
