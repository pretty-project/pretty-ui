
(ns server-extensions.trader.monitor
    (:require [clj-http.client    :as client]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.json    :as json]
              [mid-fruits.map     :as map]
              [mid-fruits.reader  :as reader]
              [mid-fruits.string  :as string]
              [mid-fruits.time    :as time]
              [mid-fruits.vector  :as vector]
              [server-fruits.http :as http]
              [x.server-core.api  :as a]
              [server-extensions.trader.engine :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-kline-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [close interval open open_time] :as kline-item}]
  (let [close_time (engine/close-time open_time interval)]
       (-> kline-item (assoc :open_timestamp  (time/epoch-s->timestamp-string open_time))
                      (assoc :close_timestamp (time/epoch-s->timestamp-string close_time))
                      (assoc :close_time      (param           close_time))
                      (assoc :close           (reader/read-str close))
                      (assoc :open            (reader/read-str open)))))

(defn update-kline-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [time_now] :as kline-data}]
  (assoc kline-data :timestamp (-> time_now (string/before-last-occurence ".")
                                            (reader/read-str)
                                            (time/epoch-s->timestamp-string))))

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
                             (nil? price-max) (assoc  :price-max (max open close))
                             ; Set initial price-min ...
                             (nil? price-min) (assoc  :price-min (min open close))
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

(defn response->kline-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [response]
  (-> response (get :body)
               ; A reader nem szereti így: {\"my-key\":\"My value\", ...}
               ; Így már tetszik neki:     {\"my-key\" \"My value\", ...}
               (string/replace-part #"\":" "\" ")
               (reader/string->mixed)
               (json/keywordize-keys)
               (map/rekey-item :result :kline-list)
               (update-kline-data)
               (update-kline-list)))

(defn query-kline-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (let [limit    (http/request->param request :limit)
        symbol   (http/request->param request :symbol)
        interval (http/request->param request :interval)
        uri      (engine/query-kline-uri {:interval interval :limit limit :symbol symbol})
        response (client/get uri)]
       (println "trader: get data from" uri)
       (-> response (response->kline-data)
                    (assoc :uri uri))))

(defn download-kline-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (http/map-wrap {:body (query-kline-data! request)}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:router/add-route! ::route
                                    {:route-template "/trader/query-kline-data"
                                     :post {:handler download-kline-data}}]})
