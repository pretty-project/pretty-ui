
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



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
(def EMAIL-PATTERN #"[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?")

; @constant (string)
(def PHONE-NUMBER-PATTERN #"\+\d{10,20}")

; @constant (string)
(def ORDERED-LABEL-PATTERN #".*\#\d$")



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
  ;  (form/pin? "0000")
  ;
  ; @return (boolean)
  [n]
  (string/length? n 4))

(defn password?
  ; @param (string) n
  ;
  ; @usage
  ;  (form/password? "Abcde1")
  ;
  ; @return (boolean)
  [n]
  (re-match? n PASSWORD-PATTERN))
  ;(string/nonempty? n))

(defn email-address?
  ; @param (string) n
  ;
  ; @usage
  ;  (form/email-address? "foo@bar.baz")
  ;
  ; @return (boolean)
  [n]
  (re-match? n EMAIL-PATTERN))

(defn phone-number?
  ; @param (string) n
  ;
  ; @usage
  ;  (form/phone-number? "+36301234567")
  ;
  ; @return (boolean)
  [n]
  (re-match? n PHONE-NUMBER-PATTERN))

(defn ordered-label?
  ; @param (string) n
  ;
  ; @usage
  ;  (form/ordered-label? "My item #3")
  ;
  ; @return (boolean)
  [n]
  (re-match? n ORDERED-LABEL-PATTERN))
