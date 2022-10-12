
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-locales.api
    (:require [x.app-locales.address-handler.helpers :as address-handler.helpers]
              [x.app-locales.address-handler.subs    :as address-handler.subs]
              [x.app-locales.country-handler.config  :as country-handler.config]
              [x.app-locales.country-handler.helpers :as country-handler.helpers]
              [x.app-locales.currency-handler.config :as currency-handler.config]
              [x.app-locales.name-handler.config     :as name-handler.config]
              [x.app-locales.name-handler.helpers    :as name-handler.helpers]
              [x.app-locales.name-handler.subs       :as name-handler.subs]
              [x.app-locales.name-handler.views      :as name-handler.views]
              [x.app-locales.language-handler.subs   :as language-handler.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-locales.address-handler.helpers
(def address->ordered-address address-handler.helpers/address->ordered-address)

; x.app-locales.address-handler.subs
(def get-ordered-address address-handler.subs/get-ordered-address)

; x.app-locales.country-handler.config
(def COUNTRY-NAMES    country-handler.config/COUNTRY-NAMES)
(def EU-COUNTRY-NAMES country-handler.config/EU-COUNTRY-NAMES)
(def COUNTRY-LIST     country-handler.config/COUNTRY-LIST)

; x.app-locales.country-handler.helpers
(def country-capital-city country-handler.helpers/country-capital-city)
(def country-currencies   country-handler.helpers/country-currencies)
(def country-currency     country-handler.helpers/country-currency)
(def country-languages    country-handler.helpers/country-languages)
(def country-language     country-handler.helpers/country-language)
(def country-name         country-handler.helpers/country-name)
(def country-native-name  country-handler.helpers/country-native-name)

; x.app-locales.currency-handler.config
(def CURRENCY-SYMBOLS currency-handler.config/CURRENCY-SYMBOLS)
(def CURRENCY-LIST    currency-handler.config/CURRENCY-LIST)

; x.app-locales.name-handler.config
(def NAME-ORDERS name-handler.config/NAME-ORDERS)

; x.app-locales.name-handler.helpers
(def name->ordered-name name-handler.helpers/name->ordered-name)

; x.app-locales.name-handler.subs
(def get-name-order   name-handler.subs/get-name-order)
(def get-ordered-name name-handler.subs/get-ordered-name)

; x.app-locales.name-handler.views
(def name-order name-handler.views/name-order)

; x.app-locales.language-handler.subs
(def get-app-languages     language-handler.subs/get-app-languages)
(def app-multilingual?     language-handler.subs/app-multilingual?)
(def get-selected-language language-handler.subs/get-selected-language)
