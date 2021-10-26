
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

(defn valid-string
  ; WARNING! INCOMPLETE! DO NOT USE!
  ;
  ; @param (string) n
  ;
  ; @usage
  ;  (form/string->valid-string " abCd12 ")
  ;
  ; @return (string)
  [n]
  (string/trim (str n)))

(defn valid-phone-number
  ; @param (string) n
  ;
  ; @return (string)
  [n]
  (-> n (string/filter-characters ["1" "2" "3" "4" "5" "6" "7" "8" "9" "0"])
        (string/starts-with!      "+")
        (string/not-ends-with!    "+")))



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
  (let [pattern #"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"]
       ; Ha n valid email cím, akkor a re-matches függvény visszatérési értéke n
       (boolean (and (string/nonempty? n)
                     (re-matches       pattern n)))))

;#"\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}.\d{3}\+\d{2}:\d{2}"

(defn phone-number-valid?
  ; @param (string) n
  ;
  ; @usage
  ;  (form/phone-number-valid? "+36301234567")
  ;
  ; @return (boolean)
  [n]
  ; Ha n valid telefonszám, akkor a re-matches függvény visszatérési értéke n
  (boolean (and (string/nonempty? n)
                (or (re-matches #"\+\d{10}" n)
                    (re-matches #"\+\d{11}" n)
                    (re-matches #"\+\d{12}" n)
                    (re-matches #"\+\d{13}" n)
                    (re-matches #"\+\d{14}" n)))))
