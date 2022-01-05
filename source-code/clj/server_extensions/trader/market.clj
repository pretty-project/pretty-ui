
(ns server-extensions.trader.market
    (:require [mid-fruits.candy  :refer [param return]]
              [x.server-core.api :as a]
              [server-extensions.trader.klines :as klines]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-market-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :trader/update-market-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      (println (str "update-market-data!"))))
;      [:trader/log! :trader/market "Updating market data ..."
;                                   {:highlighted? true}))))

(a/reg-event-fx
  :trader/resolve-market-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      (println (str "resolve-market-data!"))))

      ;[:trader/log! :trader/market "Resolving market data ..."
      ;                             {:highlighted? true})))))
