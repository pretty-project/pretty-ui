
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.04
; Description:
; Version: v0.2.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.regex)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn re-match?
  ; @param (string) n
  ; @param (regex pattern) pattern
  ;
  ; @return (boolean)
  [n pattern]
  (and (string? n)
       (some? ; Returns the match, if any, of string to pattern ...
              (re-matches pattern n))))

(defn re-mismatch?
  ; @param (string) n
  ; @param (regex pattern) pattern
  ;
  ; @return (boolean)
  [n pattern]
  (or (not (string? n))
      (nil? ; Returns the match, if any, of string to pattern ...
            (re-matches pattern n))))
