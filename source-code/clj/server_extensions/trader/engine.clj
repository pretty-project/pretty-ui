
(ns server-extensions.trader.engine
    (:require [mid-fruits.json    :as json]
              [mid-fruits.reader  :as reader]
              [mid-fruits.string  :as string]
              [mid-fruits.time    :as time]
              [server-fruits.hash :as hash]
              [mid-extensions.trader.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-extensions.trader.engine
(def PUBLIC-API-ADDRESS       engine/PUBLIC-API-ADDRESS)
(def PUBLIC-TEST-API-ADDRESS  engine/PUBLIC-TEST-API-ADDRESS)
(def PRIVATE-API-ADDRESS      engine/PRIVATE-API-ADDRESS)
(def PRIVATE-TEST-API-ADDRESS engine/PRIVATE-TEST-API-ADDRESS)
(def interval-duration        engine/interval-duration)
(def close-time               engine/close-time)
(def query-duration           engine/query-duration)
(def query-from               engine/query-from)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn signed-query-string
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [query-string api-secret]
  (let [sign (hash/hmac-sha256 query-string api-secret)]
       (str query-string "&sign=" sign)))

(defn time_now->epoch-ms
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) n
  ;
  ; @example
  ;  (engine/time_now->epoch-ms "1645550000.123456")
  ;  =>
  ;  1645550000123
  ;
  ; @return (integer)
  [n]
  (let [s  (string/before-first-occurence n ".")
        ms (string/after-first-occurence  n ".")]
       (reader/read-str (str s (subs ms 0 3)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; https://api.bybit.com/v2/public/kline/list?symbol=ETHUSD&interval=5&limit=20&from=1639222495
(defn query-kline-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) query-props
  ;  {:interval (string)
  ;    "1", "3", "5", "15", "30", "60", "120", "240", "360", "720", "D", "M", "W"
  ;   :limit (integer)
  ;    Min: 1
  ;    Max: 200
  ;   :symbol (string)}
  [{:keys [interval limit symbol]}]
  (let [query-from (query-from interval limit)]
       (str PUBLIC-API-ADDRESS "/kline/list"
                               "?symbol="   symbol
                               "&interval=" interval
                               "&limit="    limit
                               "&from="     query-from)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; https://api-testnet.bybit.com/v2/private/account/api-key?api_key={api_key}&timestamp={timestamp}&sign={sign}
(defn api-key-info-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [api-key api-secret]}]
  ; Please make sure that your timestamp is in sync with our server time. You can use the Server Time endpoint.
  (let [timestamp    (time/epoch-ms)
        query-string (str "api_key="    api-key
                          "&timestamp=" timestamp)]
       (str PRIVATE-API-ADDRESS "/account/api-key?" (signed-query-string query-string api-secret))))

; https://api-testnet.bybit.com/v2/private/wallet/balance?api_key={api_key}&coin=BTC&timestamp={timestamp}&sign={sign}
(defn wallet-balance-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [api-key api-secret]}]
  (let [timestamp    (time/epoch-ms)
        query-string (str "api_key="    api-key
                          "&timestamp=" timestamp)]
       (str PRIVATE-API-ADDRESS "/wallet/balance?" (signed-query-string query-string api-secret))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; https://api-testnet.bybit.com/v2/private/order/create
(defn create-order-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (str PRIVATE-API-ADDRESS "/order/create")
  ; TEMP
  (str PRIVATE-TEST-API-ADDRESS "/order/create"))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn response->body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [response]
  (-> response (get :body)
               ; A reader nem szereti így: {\"my-key\":\"My value\", ...}
               ; Így már tetszik neki:     {\"my-key\" \"My value\", ...}
               (string/replace-part #"\":" "\" ")
               (reader/string->mixed)
               (json/keywordize-keys)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-string
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [n]
  (-> n (json/unkeywordize-keys)
        (str)
        (string/replace-part #"\" " "\":")
        (string/replace-part #", "  ",")))

(defn header-string
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  "Content-Type: application/json")
