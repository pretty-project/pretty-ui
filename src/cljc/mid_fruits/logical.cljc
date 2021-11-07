
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.12
; Description:
; Version: v0.3.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.logical)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn nonfalse?
  ; @param (*) n
  ;
  ; @example
  ;  (nonfalse? nil)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (not (= n false)))

(defn nontrue?
  ; @param (*) n
  ;
  ; @example
  ;  (nontrue? nil)
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (not (= n true)))

(defn =?
  ; @param (*) a
  ; @param (*) b
  ; @param (*) c
  ; @param (*) d
  ;
  ; @example
  ;  (=? "A" "B" "equal" "not equal")
  ;  =>
  ;  "not equal"
  ;
  ; @return (*)
  ;  Ha a egyenlő b, akkor c különben d
  [a b c & [d]]
  (if (= a b) c d))

(defn not=?
  ; @param (*) a
  ; @param (*) b
  ; @param (*) c
  ; @param (*) d
  ;
  ; @example
  ;  (not=? "A" "B" "not equal" "equal")
  ;  =>
  ;  "not equal"
  ;
  ; @return (*)
  ;  Ha a nem egyenlő b, akkor c különben d
  [a b c & [d]]
  (if-not (= a b) c d))

(defn if-or
  ; @param (*) a
  ; @param (*) b
  ; @param (*) c
  ; @param (*) d
  ;
  ; @example
  ;  (if-or true false "C" "D")
  ;  =>
  ;  "C"
  ;
  ; @return (*)
  ;  Ha a vagy b igaz, akkor c különben d
  [a b c & [d]]
  (if (or a b) c d))

(defn if-and
  ; @param (*) a
  ; @param (*) b
  ; @param (*) c
  ; @param (*) d
  ;
  ; @example
  ;  (if-and true false "C" "D")
  ;  =>
  ;  "D"
  ;
  ; @return (*)
  ;  Ha a és b igaz, akkor c különben d
  [a b c & [d]]
  (if (and a b) c d))
