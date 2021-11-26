
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.08.05
; Description:
; Version: v0.4.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.href
    (:require [mid-fruits.candy  :refer [param]]
              [mid-fruits.string :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn email-address->href
  ; @param (string) n
  ;
  ; @example
  ;  (href/email-address->href "Hello@my-site.com")
  ;  =>
  ;  "mailto:hello@my-site.com"
  ;
  ; @return (string)
  [n]
  (str (param "mailto:")
       (string/lowercase n)))

(defn phone-number->href
  ; @param (string) n
  ;
  ; @example
  ;  (href/phone-number->href "+3630 / 123 - 4567")
  ;  =>
  ;  "tel:+36301234567"
  ;
  ; @return (string)
  [n]
  (str (param "tel:")
       (string/filter-characters n ["+" "1" "2" "3" "4" "5" "6" "7" "8" "9" "0"])))
