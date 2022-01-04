
(ns server-extensions.trader.klines
    (:require [clj-http.client   :as client]
              [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map]
              [mid-fruits.reader :as reader]
              [mid-fruits.time   :as time]
              [mid-fruits.vector :as vector]
              [x.server-core.api :as a]
              [server-extensions.trader.engine :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-kline-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) kline-item
  ;  {:close (string)
  ;   :high (string)
  ;   :low (string)
  ;   :open (string)
  ;   :open_time (string)
  ;   :volume (string)}
  ;
  ; @return (map)
  ;  {:close (float or integer)
  ;   :close-timestamp (string)
  ;   :high (float or integer)
  ;   :low (float or integer)
  ;   :open (float or integer)
  ;   :open-timestamp (string)
  ;   :volume (float or integer)}
  [{:keys [close high interval low open open_time volume] :as kline-item}]
  (let [close_time (engine/close-time open_time interval)]
       (-> kline-item (dissoc :open_time :symbol)
                      (assoc  :open-timestamp  (time/epoch-s->timestamp-string open_time))
                      (assoc  :close-timestamp (time/epoch-s->timestamp-string close_time))
                      (assoc  :close           (reader/read-str close))
                      (assoc  :open            (reader/read-str open))
                      (assoc  :high            (reader/read-str high))
                      (assoc  :low             (reader/read-str low))
                      (assoc  :volume          (reader/read-str volume)))))

(defn update-kline-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ;
  ; @return (map)
  ;  {:kline-list (maps in vector)}
  [{:keys [kline-list] :as kline-data}]
  (letfn [(f [{:keys [total-high total-low] :as o} {:keys [close open] :as x}]
             (let [close (reader/read-str close)
                   open  (reader/read-str open)]
                  ; - A total-high és total-low értékek beállításakor az árfolyam nyitóértékét
                  ;   is figyelembe kell venni!
                  ; - A kliens-oldali árfolyam-diagram sávjai az egyes periódusok nyitó-
                  ;   és záró-pontja közötti értékeket rajzolják ki.
                  (cond-> o ; Set initial total-high ...
                            (nil? total-high) (assoc :total-high (max open close))
                            ; Set initial total-low ...
                            (nil? total-low)  (assoc :total-low  (min open close))
                            ; If close is higher than total-high ...
                            (and (some? total-high) (< total-high close))
                            (assoc :total-high close)
                            ; If total-low is higher than close ...
                            (and (some? total-low)  (> total-low  close))
                            (assoc :total-low close)
                            ; Update kline-item ...
                            :update-kline-item (update :kline-list vector/conj-item (update-kline-item x)))))]
         (reduce f (dissoc kline-data :kline-list) kline-list)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn response->kline-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) response
  ;
  ; @return (map)
  ;  {:kline-list (maps in vector)}
  [response]
  (-> response (engine/get-response->body)
               (map/rekey-item :result :kline-list)
               (update-kline-data)))

(defn query-kline-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) options
  ;  {:interval (string)
  ;    "1", "3", "5", "15", "30", "60", "120", "240", "360", "720", "D", "M", "W"
  ;   :limit (integer)
  ;    Min: 1
  ;    Max: 200
  ;   :symbol (string)
  ;    "ETHUSD", "BTCUSD"
  ;   :use-mainnet? (boolean)(opt)
  ;    Default: false}
  ;
  ; @return (map)
  ;  {:high (integer)
  ;   :kline-list (maps in vector)
  ;   :low (integer)
  ;   :time-now (integer)
  ;   :timestamp (string)
  ;   :uri (string)}
  [options]
  (let [uri      (engine/query-kline-uri options)
        response (client/get             uri)]
       (-> response (response->kline-data)
                    (assoc :uri uri))))
