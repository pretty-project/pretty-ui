
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns forms.helpers
    (:require [mid.forms.helpers :as helpers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.forms.helpers
(def valid-string     helpers/valid-string)
(def pin?             helpers/pin?)
(def password?        helpers/password?)
(def email-address?   helpers/email-address?)
(def phone-number?    helpers/phone-number?)
(def items-different? helpers/items-different?)
