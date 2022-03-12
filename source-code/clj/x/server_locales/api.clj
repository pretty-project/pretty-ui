
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-locales.api
    (:require [x.server-locales.country-handler.config :as country-handler.config]
              [x.server-locales.country-handler.engine :as country-handler.engine]
              [x.server-locales.name-handler.config    :as name-handler.config]
              [x.server-locales.name-handler.engine    :as name-handler.engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-locales.country-handler.config
(def COUNTRY-NAMES    country-handler.config/COUNTRY-NAMES)
(def EU-COUNTRY-NAMES country-handler.config/EU-COUNTRY-NAMES)
(def COUNTRY-LIST     country-handler.config/COUNTRY-LIST)

; x.server-locales.country-handler.engine
(def country-capital-city country-handler.engine/country-capital-city)
(def country-currencies   country-handler.engine/country-currencies)
(def country-currency     country-handler.engine/country-currency)
(def country-languages    country-handler.engine/country-languages)
(def country-language     country-handler.engine/country-language)
(def country-name         country-handler.engine/country-name)
(def country-native-name  country-handler.engine/country-native-name)

; x.mid-locales.name-handler.config
(def NAME-ORDERS name-handler.config/NAME-ORDERS)

; x.mid-locales.name-handler.engine
(def name->ordered-name  name-handler.engine/name->ordered-name)
(def request->name-order name-handler.engine/request->name-order)
