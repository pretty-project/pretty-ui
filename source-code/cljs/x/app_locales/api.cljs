
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-locales.api
    (:require [x.app-locales.country-handler.config :as country-handler.config]
              [x.app-locales.country-handler.engine :as country-handler.engine]
              [x.app-locales.name-handler.config    :as name-handler.config]
              [x.app-locales.name-handler.engine    :as name-handler.engine]
              [x.app-locales.name-handler.subs      :as name-handler.subs]
              [x.app-locales.name-handler.views     :as name-handler.views]
              [x.app-locales.language-handler.subs  :as language-handler.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-locales.country-handler.config
(def COUNTRY-NAMES    country-handler.config/COUNTRY-NAMES)
(def EU-COUNTRY-NAMES country-handler.config/EU-COUNTRY-NAMES)
(def COUNTRY-LIST     country-handler.config/COUNTRY-LIST)

; x.app-locales.country-handler.engine
(def country-capital-city country-handler.engine/country-capital-city)
(def country-currencies   country-handler.engine/country-currencies)
(def country-currency     country-handler.engine/country-currency)
(def country-languages    country-handler.engine/country-languages)
(def country-language     country-handler.engine/country-language)
(def country-name         country-handler.engine/country-name)
(def country-native-name  country-handler.engine/country-native-name)

; x.app-locales.name-handler.config
(def NAME-ORDERS name-handler.config/NAME-ORDERS)

; x.app-locales.name-handler.engine
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
