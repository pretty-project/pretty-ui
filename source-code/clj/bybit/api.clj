
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns bybit.api
    (:require [bybit.klines.helpers   :as klines.helpers]
              [bybit.klines.request   :as klines.request]
              [bybit.klines.uri       :as klines.uri]
              [bybit.response.helpers :as response.helpers]
              [bybit.response.errors  :as response.errors]
              [bybit.uri.config       :as uri.config]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; bybit.klines.helpers
(def interval-duration klines.helpers/interval-duration)
(def close-time        klines.helpers/close-time)
(def query-duration    klines.helpers/query-duration)
(def query-from        klines.helpers/query-from)

; bybit.klines.request
(def request-kline-data! klines.request/request-kline-data!)

; bybit.klines.uri
(def kline-data-uri      klines.uri/kline-data-uri)
(def kline-data-uri-list klines.uri/kline-data-uri-list)

; bybit.response.helpers
(def time_now->epoch-ms response.helpers/time_now->epoch-ms)

; bybit.response.errors
(def response->error?               bybit.response.errors/response->error?)
(def response->invalid-api-details? bybit.response.errors/response->invalid-api-details?)

; bybit.uri.config
(def PUBLIC-API-ADDRESS       uri.config/PUBLIC-API-ADDRESS)
(def PUBLIC-TEST-API-ADDRESS  uri.config/PUBLIC-TEST-API-ADDRESS)
(def PRIVATE-API-ADDRESS      uri.config/PRIVATE-API-ADDRESS)
(def PRIVATE-TEST-API-ADDRESS uri.config/PRIVATE-TEST-API-ADDRESS)
