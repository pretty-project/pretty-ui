
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns forms.helpers
    (:require [iso.forms.helpers :as helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; iso.forms.helpers
(def valid-string     helpers/valid-string)
(def pin?             helpers/pin?)
(def password?        helpers/password?)
(def email-address?   helpers/email-address?)
(def phone-number?    helpers/phone-number?)
(def items-different? helpers/items-different?)
