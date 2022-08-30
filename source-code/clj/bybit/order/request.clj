
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns bybit.klines.order
    (:require [bybit.klines.errors    :as klines.errors]
              [bybit.klines.helpers   :as klines.helpers]
              [bybit.klines.uri       :as klines.uri]
              [bybit.response.errors  :as response.errors]
              [bybit.response.helpers :as response.helpers]
              [clj-http.client        :as clj-http.client]
              [mid-fruits.vector      :as vector]
              [mid-fruits.time        :as time]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-order-create!
  ; @param (map) request-props
  ;  {:api-key (string)
  ;   :api-secret (string)
  ;   :quantity (USD)
  ;   :symbol (string)
  ;    "BTCUSD", "ETHUSD"
  ;   :use-mainnet? (boolean)(opt)
  ;    Default: false}
  ;
  ; @example
  ;  (bybit/request-order-create! {:api-key "..." :api-secret "..." :quantity 100 :symbol "ETHUSD"})
  ;  =>
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [{:keys [api-key api-secret quantity symbol use-mainnet?] :as request-props}]
  (let [uri         (order.uri/order-create-uri    {:use-mainnet?  use-mainnet?})
        form-params (request.sign/POST-form-params {:api-key       api-key
                                                    :api-secret    api-secret
                                                    :qty           quantity
                                                    :symbol        symbol
                                                    :order-type    "Limit"
                                                    :price         "3792"
                                                    :side          "Buy"
                                                    :time-in-force "GoodTillCancel"})
        response      (clj-http.client/post uri {:form-params form-params :as :x-www-form-urlencoded})
        response-body (response.helpers/POST-response->body response)]
       (if-not (response.errors/response-body->invalid-api-details? response-body)
               (println "Hello world!"))))
