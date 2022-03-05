
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns bybit.klines.request
    (:require [bybit.klines.uri  :as klines.uri]
              [clj-http.client   :as clj-http.client]
              [mid-fruits.vector :as vector]
              [mid-fruits.time   :as time]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-kline-data!
  ; @param (map) request-props
  ;  {:interval (string)
  ;    "1", "3", "5", "15", "30", "60", "120", "240", "360", "720", "D", "M", "W"
  ;   :limit (integer)
  ;   :symbol (string)
  ;    "BTCUSD", "ETHUSD"
  ;   :use-mainnet? (boolean)(opt)
  ;    Default: false}
  ;
  ; @example
  ;  (bybit/request-kline-data! {:interval "1" :limit 60 :symbol "ETHUSD"})
  ;  =>
  ;  {:high 2420 :low 2160 :symbol "ETHUSD" :time-now ... :kline-list [{...} {...}] :uri-list ["..." "..."]}
  ;
  ; @return (map)
  ;  {:high (integer)
  ;   :kline-list (maps in vector)
  ;   :low (integer)
  ;   :symbol (string)
  ;   :time-now (integer)
  ;   :uri-list (strings in vector)}
  [{:keys [symbol] :as request-props}]
  ; Az api.bybit.com szerver által elfogadott maximális limit érték 200, ezért az annál több
  ; periódust igénylő lekéréseket több részletben küldi el, majd ... dolgozza fel a válaszokat.
  (letfn [(f [result uri] (let [response   (-> uri clj-http.client/get)]))]
;                                kline-list (-> response engine/GET-response->body :result)]
;                               (-> result (update :kline-list vector/concat-items kline-list))))]
         (let [uri-list   (klines.uri/kline-data-uri-list request-props)
               kline-data {:symbol symbol :uri-list uri-list :time-now (time/epoch-s)}]
              (-> (reduce f kline-data uri-list)))))
;                  (receive-kline-data)
;                  (check-kline-data request-props)))))
