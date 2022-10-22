
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns forms.api
    (:require [forms.attributes :as attributes]
              [forms.helpers    :as helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; forms.attributes
(def form-block-attributes  attributes/form-block-attributes)
(def form-row-attributes    attributes/form-row-attributes)
(def form-column-attributes attributes/form-column-attributes)

; forms.helpers
(def valid-string     helpers/valid-string)
(def pin?             helpers/pin?)
(def password?        helpers/password?)
(def email-address?   helpers/email-address?)
(def phone-number?    helpers/phone-number?)
(def items-different? helpers/items-different?)
