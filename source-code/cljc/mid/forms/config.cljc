
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.forms.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
;  Minimum 8 characters, maximum 32 characters, at least one uppercase letter,
;  one lowercase letter and one number
(def PASSWORD-PATTERN #"^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,32}$")

; @constant (string)
(def EMAIL-PATTERN #"[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?")

; @constant (string)
(def PHONE-NUMBER-PATTERN #"\+\d{4,20}")
