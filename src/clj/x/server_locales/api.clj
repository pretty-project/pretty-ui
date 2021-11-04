
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v0.2.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-locales.api
    (:require [x.server-locales.country-list :as country-list]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-locales.country-list
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
