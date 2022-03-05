
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns bybit.klines.engine
    (:require [mid-fruits.time :as time]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn interval-duration
  ; @param (string) interval
  ;  "1", "3", "5", "15", "30", "60", "120", "240", "360", "720", "D", "M", "W"
  ;
  ; @example
  ;  (bybit/interval-duration "1")
  ;  =>
  ;  60
  ;
  ; @return (s)
  [interval]
  (case interval "1" 60 "3" 180 "5" 300 "15" 900 "30" 1800 "60" 3600 "120" 7200 "240" 14400
                 "360" 21600 "720" 43200 "D" 86400 "M" 1152000 "W" 6048200 0)) ; 0 as default

(defn close-time
  ; @param (s) open-time
  ; @param (string) interval
  ;  "1", "3", "5", "15", "30", "60", "120", "240", "360", "720", "D", "M", "W"
  ;
  ; @example
  ;  (bybit/close-time 1580183600 "1")
  ;  =>
  ;  1580183660
  ;
  ; @return (s)
  [open-time interval]
  (+ open-time (interval-duration interval)))

(defn query-duration
  ; @param (string) interval
  ;  "1", "3", "5", "15", "30", "60", "120", "240", "360", "720", "D", "M", "W"
  ; @param (integer) limit
  ;
  ; @example
  ;  (bybit/query-duration "1" 60)
  ;  =>
  ;  3600
  ;
  ; @return (s)
  [interval limit]
  (* limit (interval-duration interval)))

(defn query-from
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) interval
  ;  "1", "3", "5", "15", "30", "60", "120", "240", "360", "720", "D", "M", "W"
  ; @param (integer) limit
  ; @param (s)(opt) epoch-s
  ;
  ; @example
  ;  (bybit/query-from "1" 60 1580183600)
  ;  =>
  ;  1580180000
  ;
  ; @return (s)
  ([interval limit]
   (- (time/epoch-s)
      (query-duration interval limit)))

  ([interval limit epoch-s]
   (- epoch-s (query-duration interval limit))))
