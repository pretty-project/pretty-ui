
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-locales.api
    (:require [x.app-locales.country-list         :as country-list]
              [x.app-locales.currency-handler     :as currency-handler]
              [x.app-locales.name-handler.engine   :as name-handler.engine]
              [x.app-locales.name-handler.subs     :as name-handler.subs]
              [x.app-locales.name-handler.views    :as name-handler.views]
              [x.app-locales.language-handler.subs :as language-handler.subs]))



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

; x.app-locales.name-handler.engine
(def NAME-ORDERS        name-handler.engine/NAME-ORDERS)
(def name->ordered-name name-handler.engine/name->ordered-name)

; x.app-locales.name-handler.subs
(def get-name-order   name-handler.subs/get-name-order)
(def get-ordered-name name-handler.subs/get-ordered-name)

; x.app-locales.name-handler.views
(def name-order name-handler.views/name-order)

; x.app-locales.language-handler.subs
(def get-app-languages     language-handler.subs/get-app-languages)
(def app-multilingual?     language-handler.subs/app-multilingual?)
(def get-selected-language language-handler.subs/get-selected-language)
