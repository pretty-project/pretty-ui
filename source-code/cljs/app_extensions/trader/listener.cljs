
(ns app-extensions.trader.listener
    (:require [mid-fruits.format  :as format]
              [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [mid-extensions.trader.listener :as listener]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def price-inc?              listener/price-inc?)
(def price-inc-from-minimum? listener/price-inc-from-minimum?)
(def drop-length             listener/drop-length)
(def price-drop              listener/price-drop)



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
           (if-let [x (drop-length kline-data)]
                   (let [price-drop (format/decimals (price-drop kline-data) 2)
                         drop-data  (str "(" x " period, " price-drop " USD)")]
                        [:trader/log! :trader/listener
                                      (str "Price is increasing from the last drop " drop-data)
                                      (get-in db [:trader :monitor :kline-data :timestamp])])))))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id])
