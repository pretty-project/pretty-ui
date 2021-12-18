
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v0.2.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-locales.api
    (:require [x.app-locales.country-list     :as country-list]
              [x.app-locales.currency-handler :as currency-handler]
              [x.app-locales.name-handler     :as name-handler]
              [x.app-locales.language-handler :as language-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-locales.country-list
(def COUNTRY-NAMES        country-list/COUNTRY-NAMES)
(def EU-COUNTRY-NAMES     country-list/EU-COUNTRY-NAMES)
(def COUNTRY-LIST         country-list/COUNTRY-LIST)
(def country-capital-city country-list/country-capital-city)
(def country-currencies   country-list/country-currencies)
(def country-currency     country-list/country-currency)
(def country-languages    country-list/country-languages)
(def country-language     country-list/country-language)
(def country-name         country-list/country-name)
(def country-native-name  country-list/country-native-name)

; x.app-locales.currency-handler
; ...

; x.app-locales.name-handler
(def NAME-ORDERS        name-handler/NAME-ORDERS)
(def name->ordered-name name-handler/name->ordered-name)
(def get-name-order     name-handler/get-name-order)
(def get-ordered-name   name-handler/get-ordered-name)
(def name-order         name-handler/name-order)

; x.app-locales.language-handler
(def get-app-languages      language-handler/get-app-languages)
(def app-multilingual?      language-handler/app-multilingual?)
(def get-selected-language  language-handler/get-selected-language)
(def get-multilingual-item  language-handler/get-multilingual-item)
(def translate              language-handler/translate)
(def set-multilingual-item! language-handler/set-multilingual-item!)
(def translated             language-handler/translated)
