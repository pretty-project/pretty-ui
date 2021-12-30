
(ns server-extensions.trader.account
    (:require [clj-http.client    :as client]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.string  :as string]
              [mid-fruits.time    :as time]
              [mid-fruits.vector  :as vector]
              [server-fruits.http :as http]
              [server-fruits.hash :as hash]
              [x.server-core.api  :as a]
              [server-extensions.trader.engine :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-api-key-info!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (let [api-key  (http/request->param request :api-key)
        uri      (engine/api-key-info-uri {:api-key api-key})
        response (client/get uri)]
       (println (str api-key))
       (println (str uri))
       (println (str response))
       {:x :y}))

(defn download-api-key-info
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (http/map-wrap {:body (get-api-key-info! request)}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:router/add-route! ::route
                                    {:route-template "/trader/get-api-key-info"
                                     :post {:handler download-api-key-info}}]})
