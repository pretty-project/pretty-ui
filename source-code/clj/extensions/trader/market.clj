
(ns extensions.trader.market
    (:require [clj-http.client            :as client]
              [extensions.trader.account  :as account]
              [extensions.trader.engine   :as engine]
              [extensions.trader.klines   :as klines]
              [extensions.trader.settings :as settings]
              [mid-fruits.candy           :refer [param return]]
              [x.server-core.api          :as a :refer [r]]))



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

; @param (string)
(def MARKET-DATA-INTERVAL "3")

; @constant (integer)
;  Last Day On Earth (3m x 480 = 24h)
(def MARKET-DATA-LIMIT 480)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  (a/dispatch [:db/set-item! [:trader :market]
                             (-> {:interval     (param MARKET-DATA-INTERVAL)
                                  :limit        (param MARKET-DATA-LIMIT)
                                  :symbol       (-> :symbol       settings/get-settings-item :value)
                                  :use-mainnet? (-> :use-mainnet? account/get-api-details-item)}
                                 (klines/request-kline-data!))]))

(a/reg-fx :trader/request-market-data! request-market-data!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-market-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (get-in db [:trader :market]))

(defn get-market-data-error
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (get-in db [:trader :market :error]))

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
      {:trader/request-market-data! nil}))

(a/reg-event-fx
  :trader/resolve-market-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if-let [error (r get-market-data-error db)]
              [:trader/log! (str "Market-data error: " error)
                            {:warning? true}]
              (if (r listener-active? db)
                  {:trader/run-listener! nil}))))
