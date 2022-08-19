
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-locales.country-handler.helpers
    (:require [mid-fruits.string                    :as string]
              [x.mid-locales.country-handler.config :as country-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn country-capital-city
  ; @param (keyword)
  ;
  ; @example
  ;  (locales/country-native-name :hu)
  ;  =>
  ;  "Budapest"
  ;
  ; @return (string)
  [country-code]
  (get-in country-handler.config/COUNTRY-LIST [country-code :capital]))

(defn country-currencies
  ; @param (keyword)
  ;
  ; @example
  ;  (locales/country-currencies :hu)
  ;  =>
  ;  "HUF"
  ;
  ; @return (string)
  [country-code]
  (get-in country-handler.config/COUNTRY-LIST [country-code :currency]))

(defn country-currency
  ; @param (keyword)
  ;
  ; @example
  ;  (locales/country-currency :hu)
  ;  =>
  ;  "HUF"
  ;
  ; @return (string)
  [country-code]
  (let [country-currencies (country-currencies country-code)]
       (string/before-first-occurence country-currencies ",")))

(defn country-languages
  ; @param (keyword)
  ;
  ; @example
  ;  (locales/country-languages :hu)
  ;  =>
  ;  ["hu"]
  ;
  ; @return (vector)
  [country-code]
  (get-in country-handler.config/COUNTRY-LIST [country-code :languages]))

(defn country-language
  ; @param (keyword)
  ;
  ; @example
  ;  (locales/country-language :hu)
  ;  =>
  ;  "hu"
  ;
  ; @return (string)
  [country-code]
  (let [country-languages (country-languages country-code)]
       (first country-languages)))

(defn country-name
  ; @param (keyword)
  ;
  ; @example
  ;  (locales/country-name :hu)
  ;  =>
  ;  "Magyarország"
  ;
  ; @return (string)
  [country-code]
  (get-in country-handler.config/COUNTRY-LIST [country-code :name]))

(defn country-native-name
  ; @param (keyword)
  ;
  ; @example
  ;  (locales/country-native-name :hu)
  ;  =>
  ;  "Magyarország"
  ;
  ; @return (string)
  [country-code]
  (get-in country-handler.config/COUNTRY-LIST [country-code :native]))
