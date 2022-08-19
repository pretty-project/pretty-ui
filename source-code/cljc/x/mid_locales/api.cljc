
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-locales.api
    (:require [x.mid-locales.country-handler.config  :as country-handler.config]
              [x.mid-locales.country-handler.helpers :as country-handler.helpers]
              [x.mid-locales.name-handler.config     :as name-handler.config]
              [x.mid-locales.name-handler.helpers    :as name-handler.helpers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-locales.country-handler.config
(def COUNTRY-NAMES    country-handler.config/COUNTRY-NAMES)
(def EU-COUNTRY-NAMES country-handler.config/EU-COUNTRY-NAMES)
(def COUNTRY-LIST     country-handler.config/COUNTRY-LIST)

; x.mid-locales.country-handler.helpers
(def country-capital-city country-handler.helpers/country-capital-city)
(def country-currencies   country-handler.helpers/country-currencies)
(def country-currency     country-handler.helpers/country-currency)
(def country-languages    country-handler.helpers/country-languages)
(def country-language     country-handler.helpers/country-language)
(def country-name         country-handler.helpers/country-name)
(def country-native-name  country-handler.helpers/country-native-name)

; x.mid-locales.name-handler.config
(def NAME-ORDERS name-handler.config/NAME-ORDERS)

; x.mid-locales.name-handler.helpers
(def name->ordered-name name-handler.helpers/name->ordered-name)
