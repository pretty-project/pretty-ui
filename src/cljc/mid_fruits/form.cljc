
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.09
; Description:
; Version: v0.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.form
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.mixed  :as mixed]
              [mid-fruits.string :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn string->valid-string
  ; WARNING! INCOMPLETE!
  ;
  ; @param (string) n
  ;
  ; @usage
  ;  (form/string->valid-string " abCd12 ")
  ;
  ; @return (string)
  [n]
  ; TODO Use SPEC!
  (string/trim (str n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn pin-valid?
  ; @param (string) n
  ;
  ; @usage
  ;  (form/pin-valid? "0000")
  ;
  ; @return (boolean)
  [n]
  (string/length? n 4))

(defn password-valid?
  ; Password must contain at least 6 characters, both uppercase
  ; and lowercase letters, and a number!
  ;
  ; @param (string) n
  ;
  ; @usage
  ;  (form/password-valid? "Abcde1")
  ;
  ; @return (boolean)
  [n]
  ; TODO Use SPEC!
  (boolean (and (string/min-length?                n 6)
                (string/contains-uppercase-letter? n)
                (mixed/mixed->contains-number?     n))))

(defn email-address-valid?
  ; @param (string) n
  ;
  ; @usage
  ;  (form/email-address-valid? "foo@bar.baz")
  ;
  ; @return (boolean)
  [n]
  ; TODO Use SPEC!
  (let [pattern #"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"]
       ; Ha n valid email cím, akkor a re-matches függvény visszatérési értéke n
       (boolean (and (string/nonempty? n)
                     (re-matches       pattern n)))))
