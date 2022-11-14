
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.locales.api
    (:require [x.locales.address-handler.helpers :as address-handler.helpers]
              [x.locales.address-handler.subs    :as address-handler.subs]
              [x.locales.country-handler.config  :as country-handler.config]
              [x.locales.country-handler.helpers :as country-handler.helpers]
              [x.locales.currency-handler.config :as currency-handler.config]
              [x.locales.name-handler.config     :as name-handler.config]
              [x.locales.name-handler.helpers    :as name-handler.helpers]
              [x.locales.name-handler.subs       :as name-handler.subs]
              [x.locales.name-handler.views      :as name-handler.views]
              [x.locales.language-handler.events :as language-handler.events]
              [x.locales.language-handler.subs   :as language-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.locales.address-handler.helpers
(def address->ordered-address address-handler.helpers/address->ordered-address)

; x.locales.address-handler.subs
(def get-ordered-address address-handler.subs/get-ordered-address)

; x.locales.country-handler.config
(def COUNTRY-NAMES    country-handler.config/COUNTRY-NAMES)
(def EU-COUNTRY-NAMES country-handler.config/EU-COUNTRY-NAMES)
(def COUNTRY-LIST     country-handler.config/COUNTRY-LIST)

; x.locales.country-handler.helpers
(def country-capital-city country-handler.helpers/country-capital-city)
(def country-currencies   country-handler.helpers/country-currencies)
(def country-currency     country-handler.helpers/country-currency)
(def country-languages    country-handler.helpers/country-languages)
(def country-language     country-handler.helpers/country-language)
(def country-name         country-handler.helpers/country-name)
(def country-native-name  country-handler.helpers/country-native-name)

; x.locales.currency-handler.config
(def CURRENCY-SYMBOLS currency-handler.config/CURRENCY-SYMBOLS)
(def CURRENCY-LIST    currency-handler.config/CURRENCY-LIST)

; x.locales.name-handler.config
(def NAME-ORDERS name-handler.config/NAME-ORDERS)

; x.locales.name-handler.helpers
(def name->ordered-name     name-handler.helpers/name->ordered-name)
(def name->ordered-initials name-handler.helpers/name->ordered-initials)

; x.locales.name-handler.subs
(def get-name-order       name-handler.subs/get-name-order)
(def get-ordered-name     name-handler.subs/get-ordered-name)
(def get-ordered-initials name-handler.subs/get-ordered-initials)

; x.locales.name-handler.views
(def name-order name-handler.views/name-order)

; x.locales.language-handler.events
(def select-language! language-handler.events/select-language!)

; x.locales.language-handler.subs
(def get-app-languages     language-handler.subs/get-app-languages)
(def app-multilingual?     language-handler.subs/app-multilingual?)
(def get-selected-language language-handler.subs/get-selected-language)
