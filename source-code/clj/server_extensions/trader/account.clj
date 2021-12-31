
(ns server-extensions.trader.account
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

(defn get-api-key-info!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (let [api-key    (http/request->param request :api-key)
        api-secret (http/request->param request :api-secret)
        uri        (engine/api-key-info-uri {:api-key api-key :api-secret api-secret})
        response   (client/get uri)]
       (println "trader/account: get data from" uri)
       (-> response (engine/response->body)
                    (assoc :uri uri))))

(defn download-api-key-info
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (http/map-wrap {:body (get-api-key-info! request)}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-wallet-balance!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (let [api-key    (http/request->param request :api-key)
        api-secret (http/request->param request :api-secret)
        uri        (engine/wallet-balance-uri {:api-key api-key :api-secret api-secret})
        response   (client/get uri)]
       (println "trader/account: get data from" uri)
       (-> response (engine/response->body)
                    (assoc :uri uri))))

(defn download-wallet-balance
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (http/map-wrap {:body (get-wallet-balance! request)}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot {:dispatch-n [[:router/add-route! :trader/api-key-info-route
                                                  {:route-template "/trader/get-api-key-info"
                                                   :post {:handler download-api-key-info}}]
                              [:router/add-route! :trader/wallet-balance-route
                                                  {:route-template "/trader/get-wallet-balance"
                                                   :post {:handler download-wallet-balance}}]]}})
