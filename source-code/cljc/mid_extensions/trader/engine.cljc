
(ns mid-extensions.trader.engine
    (:require [mid-fruits.time :as time]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def API-ADDRESS "https://api.bybit.com/v2/public")

; @constant (string)
(def TEST-API-ADDRESS "https://api-testnet.bybit.com/v2/public")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

;https://api.bybit.com/v2/public/kline/list?symbol=ETHUSD&interval=5&limit=20&from=1639222495
(defn query-kline-url
  ; @param (map) query-props
  ;  {:interval (integer or string)
  ;    1, 3, 5, 15, 30, 60, 120, 240, 360, 720, "D", "M", "W"
  ;   :symbol (string)}
  [{:keys [interval symbol]}]
  (let [time-now ()]
       (str API-ADDRESS "/kline/list?"
            "symbol="    symbol
            "&interval=" interval
            "&limit="    200
            "&from=")))
