
(ns mid-extensions.trader.engine
    (:require [mid-fruits.time :as time]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def PUBLIC-API-ADDRESS "https://api.bybit.com/v2/public")

; @constant (string)
(def PUBLIC-TEST-API-ADDRESS "https://api-testnet.bybit.com/v2/public")

; @constant (string)
(def PRIVATE-API-ADDRESS "https://api-testnet.bybit.com/v2/private")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn interval-duration
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) interval
  ;  "1", "3", "5", "15", "30", "60", "120", "240", "360", "720", "D", "M", "W"
  ;
  ; @return (s)
  [interval]
  (case interval "1" 60 "3" 180 "5" 300 "15" 900 "30" 1800 "60" 3600 "120" 7200 "240" 14400
                 "360" 21600 "720" 43200 "D" 86400 "M" 1152000 "W" 6048200))

(defn close-time
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (s) open-time
  ; @param (string) interval
  ;  "1", "3", "5", "15", "30", "60", "120", "240", "360", "720", "D", "M", "W"
  ;
  ; @return (s)
  [open-time interval]
  (+ open-time (interval-duration interval)))

(defn query-duration
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) interval
  ;  "1", "3", "5", "15", "30", "60", "120", "240", "360", "720", "D", "M", "W"
  ; @param (integer) limit
  ;  Min: 1
  ;  Max: 200
  ;
  ; @return (s)
  [interval limit]
  (* (interval-duration interval) limit))

(defn query-from
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) interval
  ;  "1", "3", "5", "15", "30", "60", "120", "240", "360", "720", "D", "M", "W"
  ; @param (integer) limit
  ;  Min: 1
  ;  Max: 200
  ;
  ; @return (s)
  [interval limit]
  (- (time/epoch-s)
     (query-duration interval limit)))

;https://api.bybit.com/v2/public/kline/list?symbol=ETHUSD&interval=5&limit=20&from=1639222495
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

; "https://api-testnet.bybit.com/v2/private/account/api-key?api_key={api_key}&timestamp={timestamp}&sign={sign}"
(defn api-key-info-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [api-key]}]
  ; Please make sure that your timestamp is in sync with our server time. You can use the Server Time endpoint.
  (let [timestamp (time/epoch-ms)]
       (str PRIVATE-API-ADDRESS "/account/api-key"
            "?api-key="   api-key
            "&timestamp=" timestamp)))

(defn signed-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uri]
  (let [sign ""]
       (str uri "&sign=" sign)))
