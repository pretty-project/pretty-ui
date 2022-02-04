
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.09
; Description:
; Version: v0.8.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.form
    (:require [mid-fruits.regex  :refer [re-match?]]
              [mid-fruits.string :as string]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
;  Minimum 8 characters, maximum 32 characters, at least one uppercase letter,
;  one lowercase letter and one number
(def PASSWORD-PATTERN #"^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,32}$")

; @constant (string)
(def EMAIL-PATTERN #"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")

; @constant (string)
(def PHONE-NUMBER-PATTERN #"\+\d{10,20}")



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn valid-string
  ; WARNING! INCOMPLETE! DO NOT USE!
  ;
  ; @param (string) n
  ;
  ; @usage
  ;  (form/valid-string " abCd12 ")
  ;  =>
  ;  "abCd12"
  ;
  ; @return (string)
  [n]
  (string/trim n))



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
  ; @param (string) n
  ;
  ; @usage
  ;  (form/password-valid? "Abcde1")
  ;
  ; @return (boolean)
  [n]
 ;(re-match? n PASSWORD-PATTERN)
  (string/nonempty? n))

(defn email-address-valid?
  ; @param (string) n
  ;
  ; @usage
  ;  (form/email-address-valid? "foo@bar.baz")
  ;
  ; @return (boolean)
  [n]
  (re-match? n EMAIL-PATTERN))

(defn phone-number-valid?
  ; @param (string) n
  ;
  ; @usage
  ;  (form/phone-number-valid? "+36301234567")
  ;
  ; @return (boolean)
  [n]
  (re-match? n PHONE-NUMBER-PATTERN))
