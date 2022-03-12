
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-locales.country-handler.engine
    (:require [x.mid-locales.country-handler.engine :as country-handler.engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-locales.country-handler.engine
(def country-capital-city country-handler.engine/country-capital-city)
(def country-currencies   country-handler.engine/country-currencies)
(def country-currency     country-handler.engine/country-currency)
(def country-languages    country-handler.engine/country-languages)
(def country-language     country-handler.engine/country-language)
(def country-name         country-handler.engine/country-name)
(def country-native-name  country-handler.engine/country-native-name)
