
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns bybit.api
    (:require [bybit.klines.engine   :as klines.engine]
              [bybit.klines.uri      :as klines.uri]
              [bybit.response.engine :as response.engine]
              [bybit.response.errors :as response.errors]
              [bybit.uri             :as uri]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; bybit.klines.engine
(def interval-duration klines.engine/interval-duration)
(def close-time        klines.engine/close-time)
(def query-duration    klines.engine/query-duration)
(def query-from        klines.engine/query-from)

; bybit.klines.uri
(def kline-data-uri      klines.uri/kline-data-uri)
(def kline-data-uri-list klines.uri/kline-data-uri-list)

; bybit.response.engine
(def time_now->epoch-ms response.engine/time_now->epoch-ms)

; bybit.response.errors
(def response->error?               bybit.response.errors/response->error?)
(def response->invalid-api-details? bybit.response.errors/response->invalid-api-details?)

; bybit.uri
(def PUBLIC-API-ADDRESS       uri/PUBLIC-API-ADDRESS)
(def PUBLIC-TEST-API-ADDRESS  uri/PUBLIC-TEST-API-ADDRESS)
(def PRIVATE-API-ADDRESS      uri/PRIVATE-API-ADDRESS)
(def PRIVATE-TEST-API-ADDRESS uri/PRIVATE-TEST-API-ADDRESS)
