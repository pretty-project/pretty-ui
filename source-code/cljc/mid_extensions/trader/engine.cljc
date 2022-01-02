
(ns mid-extensions.trader.engine
    (:require [mid-fruits.time :as time]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def PUBLIC-API-ADDRESS "https://api.bybit.com/v2/public")

; @constant (string)
(def PUBLIC-TEST-API-ADDRESS "https://api-testnet.bybit.com/v2/public")

; @constant (string)
(def PRIVATE-API-ADDRESS "https://api.bybit.com/v2/private")

; @constant (string)
(def PRIVATE-TEST-API-ADDRESS "https://api-testnet.bybit.com/v2/private")



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
                 "360" 21600 "720" 43200 "D" 86400 "M" 1152000 "W" 6048200
                 ; default
                 0))

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
