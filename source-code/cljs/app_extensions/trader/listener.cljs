
(ns app-extensions.trader.listener
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [mid-extensions.trader.listener :as listener]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def inc-from-lowest?  listener/inc-from-lowest?)
(def inc-from-minimum? listener/inc-from-minimum?)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-listeners-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {})

(a/reg-sub :trader/get-listeners-props get-listeners-props)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :trader/resolve-kline-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [kline-data (get-in db [:trader :monitor :kline-data])]
           (if (inc-from-lowest? kline-data 10)
               [:trader/log! :trader/listener "Price is increasing from the lowest of last 10 period!"
                                              (get-in db [:trader :monitor :kline-data :timestamp])]))))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id])
