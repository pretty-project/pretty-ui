
(ns server-extensions.trader.market
    (:require [clj-http.client    :as client]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.time    :as time]
              [mid-fruits.vector  :as vector]
              [server-fruits.http :as http]
              [x.server-core.api  :as a :refer [r]]
              [server-extensions.trader.account  :as account]
              [server-extensions.trader.engine   :as engine]
              [server-extensions.trader.klines   :as klines]
              [server-extensions.trader.settings :as settings]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :debug
  (fn [{:keys [db]} _]
      (println (str        (get-in db [:x.server-router.route-handler/client-routes])))
      (println (str        (get-in db [:x.mid-core.lifecycle-handler/lifes :data-items]))); :x.server-router.system-routes/lifecycles]))))
      (println (str (count (keys (get-in db [:x.mid-core.lifecycle-handler/lifes :data-items]))))))); :x.server-router.system-routes/lifecycles])))))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (s)
;  86400s = 24h
(def MARKET-DATA-INTERVAL 86400)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn kline-data-uri-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) uri-props
  ;  {}
  ;
  ; @return (strings in vector)
  [uri-props]
  (letfn [(f [uri-list {:keys [limit] :as uri-props}]
             (if (> limit 200)
                 (f (vec (cons uri-list (str (engine/kline-data-uri (merge uri-props {:limit 200})))))
                    (assoc uri-props :limit (- limit 200)))
                 (vec (cons uri-list (str (engine/kline-data-uri uri-props))))))]
         (f [] uri-props)))

(defn request-market-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:high (integer)
  ;   :kline-list (maps in vector)
  ;   :low (integer)
  ;   :time-now (integer)
  ;   :timestamp (string)
  ;   :uri (string)}
  [_]
  (println "request-market-data!")



  (let [uri-a (engine/kline-data-uri {:interval     "3"
                                      :limit        "200"
                                      :symbol       (-> :symbol       settings/get-settings-item :value)
                                      :use-mainnet? (-> :use-mainnet? account/get-api-details-item)
                                      :from         (engine/query-from "3" 200)})
        uri-b (engine/kline-data-uri {:interval     "3"
                                      :limit        "200"
                                      :symbol       (-> :symbol       settings/get-settings-item :value)
                                      :use-mainnet? (-> :use-mainnet? account/get-api-details-item)
                                      :from         (- (engine/query-from "3" 200) 36000)})
        uri-c (engine/kline-data-uri {:interval     "3"
                                      :limit        "80"
                                      :symbol       (-> :symbol       settings/get-settings-item :value)
                                      :use-mainnet? (-> :use-mainnet? account/get-api-details-item)
                                      :from         (- (engine/query-from "3" 200) 36000 14400)})
        response-a (client/get uri-a)
        response-b (client/get uri-b)
        response-c (client/get uri-c)]
       ;(println (str response-a))
       (a/dispatch [:db/set-item! [:trader :market]
                                  {:a (-> response-a (klines/response->kline-data))
                                   :b (-> response-b (klines/response->kline-data))
                                   :c (-> response-c (klines/response->kline-data))}])))

(a/reg-fx :trader/request-market-data! request-market-data!)



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
  (boolean (get-in db [:trader :listener :listener-active?])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :trader/update-market-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (println (str "update-market-data!"))
      {:trader/request-market-data! nil}))

(a/reg-event-fx
  :trader/resolve-market-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (println (str "resolve-market-data!"))
      ;(println (str (get-in db [:trader :market])))
      ;(println (str (get-in db [:x.server-router.route-handler/server-routes :data-items])))
      ;(println (str (keys (get-in db [:x.server-router.route-handler/server-routes :data-items]))))
      (if (r listener-active? db)
          {:trader/run-listener! nil})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- print-kline-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [kline-list]}]
  (reduce (fn [o x] (str o "\n" x))
          nil kline-list))

(defn- print-market-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [market-data]
  (str "Last updated: "
       "\n"
       (print-kline-list market-data)))



(defn- download-market-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (println "")
  (println (str (kline-data-uri-list {:interval "3" :limit 480
                                      :symbol "ETHUSD"
                                      :use-mainnet? true})))
  (println "")
  (let [market-data-a (a/subscribed [:db/get-item [:trader :market :a]])
        market-data-b (a/subscribed [:db/get-item [:trader :market :b]])
        market-data-c (a/subscribed [:db/get-item [:trader :market :c]])]
       (http/text-wrap {:body
                              (str (count (:kline-list market-data-c))
                                   (print-market-data market-data-c)
                                   "\n\n\n"
                                   (count (:kline-list market-data-b))
                                   (print-market-data market-data-b)
                                   "\n\n\n"
                                   (count (:kline-list market-data-a))
                                   (print-market-data market-data-a))})))

(a/reg-lifecycles
  ::lifecycles
  {:on-server-boot [:router/add-route! :trader/download-market-data
                                       {:route-template "/@app-home/trader/market-data"
                                        :get (fn [request] (download-market-data request))
                                        :restricted? true}]})
