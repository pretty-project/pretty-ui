
(ns server-extensions.trader.klines
    (:require [clj-http.client                 :as client]
              [mid-extensions.trader.klines    :as klines]
              [mid-fruits.candy                :refer [param return]]
              [mid-fruits.map                  :as map]
              [mid-fruits.reader               :as reader]
              [mid-fruits.time                 :as time]
              [mid-fruits.vector               :as vector]
              [server-extensions.trader.engine :as engine]
              [x.server-core.api               :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-extensions.trader.klines
(def kline-data-repetition-error klines/kline-data-repetition-error)
(def kline-data-time-error       klines/kline-data-time-error)
(def kline-data-limit-error      klines/kline-data-limit-error)
(def kline-data-error            klines/kline-data-error)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn receive-kline-item
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
  ;   :close-time (integer)
  ;   :close-timestamp (string)
  ;   :high (float or integer)
  ;   :low (float or integer)
  ;   :open (float or integer)
  ;   :open-time (integer)
  ;   :open-timestamp (string)
  ;   :volume (float or integer)}
  [{:keys [close high interval low open open_time volume] :as kline-item}]
  (let [; WARNING! Az aktuális (éppen történő) periódus close-time értéke egy jövőbeni időpontra mutat!
        close_time (engine/close-time open_time interval)]
       (-> kline-item (dissoc :open_time :symbol)
                      (assoc  :close-time      (param close_time))
                      (assoc  :open-time       (param open_time))
                      (assoc  :open-timestamp  (time/epoch-s->timestamp-string open_time))
                      (assoc  :close-timestamp (time/epoch-s->timestamp-string close_time))
                      (assoc  :close           (reader/read-str close))
                      (assoc  :open            (reader/read-str open))
                      (assoc  :high            (reader/read-str high))
                      (assoc  :low             (reader/read-str low))
                      (assoc  :volume          (reader/read-str volume)))))

(defn receive-kline-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) kline-data
  ;  {:kline-list (maps in vector)}
  ;
  ; @return (map)
  ;  {:kline-list (maps in vector)
  ;   :total-high (integer)
  ;   :total-low (integer)}
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
                            :update-kline-item (update :kline-list vector/conj-item (receive-kline-item x)))))]
         (reduce f (dissoc kline-data :kline-list) kline-list)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn check-kline-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) kline-data
  ; @param (map) options
  ;
  ; @return (map)
  ;  {:error (namespaced keyword)}
  [kline-data options]
  (if-let [error (kline-data-error kline-data options)]
          (assoc  kline-data :error error)
          (return kline-data)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-kline-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) options
  ;  {:interval (string)
  ;    "1", "3", "5", "15", "30", "60", "120", "240", "360", "720", "D", "M", "W"
  ;   :limit (integer)
  ;   :symbol (string)
  ;    "BTCUSD", "ETHUSD"
  ;   :use-mainnet? (boolean)(opt)
  ;    Default: false}
  ;
  ; @return (map)
  ;  {:high (integer)
  ;   :kline-list (maps in vector)
  ;   :low (integer)
  ;   :symbol (string)
  ;   :time-now (integer)
  ;   :uri-list (strings in vector)}
  [{:keys [symbol] :as options}]
  ; Az api.bybit.com szerver által elfogadott maximális limit érték 200, ezért az annál több
  ; periódust igénylő lekéréseket több részletben küldi el, majd dolgozza fel a válaszokat.
  (let [uri-list   (engine/kline-data-uri-list options)
        kline-data {:symbol symbol :uri-list uri-list :time-now (time/epoch-s)}]
       (letfn [(f [o uri]
                  (let [response   (-> uri      client/get)
                        kline-list (-> response engine/GET-response->body :result)]
                       (-> o (update :kline-list vector/concat-items kline-list))))]
              (-> (reduce f kline-data uri-list)
                  (receive-kline-data)
                  (check-kline-data options)))))
