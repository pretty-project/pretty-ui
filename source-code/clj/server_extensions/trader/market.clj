
(ns server-extensions.trader.market
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.time   :as time]
              [mid-fruits.vector :as vector]
              [x.server-core.api :as a :refer [r]]
              [server-extensions.trader.engine :as engine]
              [server-extensions.trader.klines :as klines]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (s)
;  86400s = 24h
(def MARKET-DATA-INTERVAL 86400)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn query-market-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) options
  ;  {:symbol (string)
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
  [{:keys [symbol use-mainnet?]}]
  (let [uri (engine/query-kline-uri {:from         (time/epoch-s)
                                     :interval     "3"
                                     :limit        "200"
                                     :symbol       symbol
                                     :use-mainnet? use-mainnet?})]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-market-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (get-in db [:trader :market]))

(defn get-kline-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (get-in db [:trader :market :kline-list]))

(defn listener-active?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (boolean (get-in db [:trader :listener :active?])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :trader/update-market-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if-let [kline-list (r get-kline-list db)]
              (let [; Az utolsó elem nem biztos, hogy egy befejezett periódus adatait tartalmazza!
                    kline-list (vector/pop-last-item kline-list)
                    ; Az utolsó előtti elem lett a záró elem ...
                    last-kline (vector/last-item kline-list)
                    list-closed-at (get last-kline :close-time)
                    ;
                    epoch-s (time/epoch-s)]

                   (println (str "time-now: " epoch-s)))
              (let [; If kline-list is empty ...
                    epoch-s (time/epoch-s)]
                   (println (str "time-now: " epoch-s))))

      (println (str "update-market-data!"))
      (println (str        (keys (get-in db [:x.mid-core.lifecycle-handler/lifes :data-items]))))
      (println (str (count (keys (get-in db [:x.mid-core.lifecycle-handler/lifes :data-items])))))))


(a/reg-event-fx
  :trader/resolve-market-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (println (str "resolve-market-data!"))
      (println (str        (keys (get-in db [:x.mid-core.lifecycle-handler/lifes :data-items]))))
      (println (str (count (keys (get-in db [:x.mid-core.lifecycle-handler/lifes :data-items])))))
      (if (r listener-active? db)
          {:trader/run-listener! nil})))
