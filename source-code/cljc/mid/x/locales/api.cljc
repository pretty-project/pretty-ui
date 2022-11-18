
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.locales.api
    (:require [mid.x.locales.address-handler.helpers :as address-handler.helpers]
              [mid.x.locales.country-handler.config  :as country-handler.config]
              [mid.x.locales.country-handler.helpers :as country-handler.helpers]
              [mid.x.locales.currency-handler.config :as currency-handler.config]
              [mid.x.locales.name-handler.config     :as name-handler.config]
              [mid.x.locales.name-handler.helpers    :as name-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.locales.address-handler.helpers
(def address->ordered-address address-handler.helpers/address->ordered-address)

; mid.x.locales.country-handler.config
(def COUNTRY-NAMES    country-handler.config/COUNTRY-NAMES)
(def EU-COUNTRY-NAMES country-handler.config/EU-COUNTRY-NAMES)
(def COUNTRY-LIST     country-handler.config/COUNTRY-LIST)

; mid.x.locales.country-handler.helpers
(def country-capital-city country-handler.helpers/country-capital-city)
(def country-currencies   country-handler.helpers/country-currencies)
(def country-currency     country-handler.helpers/country-currency)
(def country-languages    country-handler.helpers/country-languages)
(def country-language     country-handler.helpers/country-language)
(def country-name         country-handler.helpers/country-name)
(def country-native-name  country-handler.helpers/country-native-name)

; mid.x.locales.currency-handler.config
(def CURRENCY-SYMBOLS currency-handler.config/CURRENCY-SYMBOLS)
(def CURRENCY-LIST    currency-handler.config/CURRENCY-LIST)

; mid.x.locales.name-handler.config
(def NAME-ORDERS name-handler.config/NAME-ORDERS)

; mid.x.locales.name-handler.helpers
(def name->ordered-name     name-handler.helpers/name->ordered-name)
(def name->ordered-initials name-handler.helpers/name->ordered-initials)