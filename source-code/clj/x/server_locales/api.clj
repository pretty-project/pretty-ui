
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-locales.api
    (:require [x.server-locales.address-handler.helpers :as address-handler.helpers]
              [x.server-locales.address-handler.subs    :as address-handler.subs]
              [x.server-locales.country-handler.config  :as country-handler.config]
              [x.server-locales.country-handler.helpers :as country-handler.helpers]
              [x.server-locales.currency-handler.config :as currency-handler.config]
              [x.server-locales.name-handler.config     :as name-handler.config]
              [x.server-locales.name-handler.helpers    :as name-handler.helpers]
              [x.server-locales.name-handler.subs       :as name-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-locales.address-handler.helpers
(def address->ordered-address address-handler.helpers/address->ordered-address)

; x.server-locales.address-handler.subs
(def get-ordered-address address-handler.subs/get-ordered-address)

; x.server-locales.country-handler.config
(def COUNTRY-NAMES    country-handler.config/COUNTRY-NAMES)
(def EU-COUNTRY-NAMES country-handler.config/EU-COUNTRY-NAMES)
(def COUNTRY-LIST     country-handler.config/COUNTRY-LIST)

; x.server-locales.country-handler.helpers
(def country-capital-city country-handler.helpers/country-capital-city)
(def country-currencies   country-handler.helpers/country-currencies)
(def country-currency     country-handler.helpers/country-currency)
(def country-languages    country-handler.helpers/country-languages)
(def country-language     country-handler.helpers/country-language)
(def country-name         country-handler.helpers/country-name)
(def country-native-name  country-handler.helpers/country-native-name)

; x.server-locales.currency-handler.config
(def CURRENCY-SYMBOLS currency-handler.config/CURRENCY-SYMBOLS)
(def CURRENCY-LIST    currency-handler.config/CURRENCY-LIST)

; x.server-locales.name-handler.config
(def NAME-ORDERS name-handler.config/NAME-ORDERS)

; x.server-locales.name-handler.helpers
(def name->ordered-name         name-handler.helpers/name->ordered-name)
(def request->name-order        name-handler.helpers/request->name-order)
(def request->ordered-user-name name-handler.helpers/request->ordered-user-name)

; x.server-locales.name-handler.subs
(def get-name-order   name-handler.subs/get-name-order)
(def get-ordered-name name-handler.subs/get-ordered-name)
