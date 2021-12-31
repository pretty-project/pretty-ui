
(ns server-extensions.trader.monitor
    (:require [clj-http.client    :as client]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.map     :as map]
              [mid-fruits.reader  :as reader]
              [mid-fruits.string  :as string]
              [mid-fruits.time    :as time]
              [mid-fruits.vector  :as vector]
              [mongo-db.api       :as mongo-db]
              [pathom.api         :as pathom]
              [x.server-core.api  :as a]
              [server-extensions.trader.engine       :as engine]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-kline-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [close high interval low open open_time] :as kline-item}]
  (let [close_time (engine/close-time open_time interval)]
       (-> kline-item (dissoc :open_time :turnover :symbol :volume)
                      (assoc  :open-timestamp  (time/epoch-s->timestamp-string open_time))
                      (assoc  :close-timestamp (time/epoch-s->timestamp-string close_time))
                      (assoc  :close           (reader/read-str close))
                      (assoc  :open            (reader/read-str open))
                      (assoc  :high            (reader/read-str high))
                      (assoc  :low             (reader/read-str low)))))

(defn update-kline-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [kline-list] :as kline-data}]
  (reduce (fn [{:keys [price-max price-min] :as o} {:keys [close open] :as x}]
              (let [close (reader/read-str close)
                    open  (reader/read-str open)]
                   ; Az első iterációban, a price-max és price-min beállításakor
                   ; az árfolyam nyitóértékét is figyelembe kell venni, mert a kliens-oldali
                   ; pont-diagram első sávja az első kline nyitó- és záró-pontja közötti
                   ; értékeket rajzolja ki, majd pedig a többi sáv a záró- és a következő
                   ; záró-pont közötti értékeket
                   (cond-> o ; Set initial price-max ...
                             (nil? price-max) (assoc :price-max (max open close))
                             ; Set initial price-min ...
                             (nil? price-min) (assoc :price-min (min open close))
                             ; If close is higher than price-max ...
                             (and (some? price-max) (< price-max close))
                             (assoc :price-max close)
                             ; If price-min is higher than close ...
                             (and (some? price-min) (> price-min close))
                             (assoc :price-min close)
                             ; Update kline-item ...
                             :update-kline-item (update :kline-list vector/conj-item (update-kline-item x)))))
          (dissoc kline-data :kline-list)
          (param  kline-list)))

(defn update-time
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [time_now] :as kline-data}]
  (let [epoch-ms (engine/time_now->epoch-ms time_now)]
       (-> kline-data (assoc :timestamp (time/epoch-ms->timestamp-string epoch-ms)
                             :time-now  (param epoch-ms))
                      (dissoc :time_now))))

(defn response->kline-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [response]
  (-> response (engine/response->body)
               (select-keys   [:result :time_now])
               (map/rekey-item :result :kline-list)
               (update-time)
               (update-kline-list)))

(defresolver get-kline-data
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env _]
             {:trader/get-kline-data (let [uri (engine/query-kline-uri {:interval (pathom/env->param env :interval)
                                                                        :limit    (pathom/env->param env :limit)
                                                                        :symbol   (pathom/env->param env :symbol)})
                                           response (client/get uri)]
                                          (println ":trader/monitor" uri)
                                          (-> response (response->kline-data)
                                                       (assoc :uri uri)))})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def HANDLERS [get-kline-data])

(pathom/reg-handlers! :trader/monitor HANDLERS)
