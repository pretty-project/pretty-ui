
(ns server-extensions.trader.order
    (:require [clj-http.client    :as client]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.reader  :as reader]
              [mid-fruits.string  :as string]
              [mid-fruits.time    :as time]
              [mid-fruits.vector  :as vector]
              [server-fruits.http :as http]
              [x.server-core.api  :as a]
              [server-extensions.trader.engine :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn query-create-order!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (let [uri      (engine/create-order-uri)]
        ;response (client/post uri)
       (println "trader/order: post data to" uri)))
       ;(-> response (response->kline-data)
       ;             (assoc :uri uri))

(defn send-create-order
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (http/map-wrap {:body (query-create-order! request)}))

{:api_key        ""
 :side           "Buy"           ; "Buy" "Sell"
 :symbol         "ETHUSD"
 :order_type     "Market"        ; "Market" "Limit"
 :qty            10              ;  Must be a positive integer
 :time_in_force "GoodTillCancel" ; "GoodTillCancel" "ImmediateOrCancel" "FillOrKill" "PostOnly"
 :timestamp      00000
 :sign           ""}



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:router/add-route! :trader/query-create-order
                                    {:route-template "/trader/query-create-order"
                                     :post {:handler send-create-order}}]})
