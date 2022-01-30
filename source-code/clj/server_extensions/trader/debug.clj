
(ns server-extensions.trader.debug
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.time    :as time]
              [server-fruits.http :as http]
              [x.server-core.api  :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- print-listener-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [listener-data]
  (str "Listener-state: " listener-data))

(defn- download-listener-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (let [listener-data (a/subscribed [:db/get-item [:trader :listener]])]
       (http/text-wrap {:body (print-listener-data listener-data)})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- print-log-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [log-items]
  (reduce (fn [o x] (str o "\n" x))
          nil log-items))

(defn- print-log-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [log-data]
  (str "Log-items: \n\n"
       (print-log-items log-data)))

(defn- download-log-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (let [log-data (a/subscribed [:db/get-item [:trader :log]])]
       (http/text-wrap {:body (print-log-data log-data)})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- print-kline-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [kline-list]}]
  (reduce (fn [o x] (str o "\n" x))
          nil kline-list))

(defn- print-market-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [error time-now uri-list] :as market-data}]
  (str "last updated: " (time/epoch-s->timestamp-string time-now) "\n\n"
       "URI-list: "     (str uri-list)                            "\n\n"
       "error: "        (or  error ":no-error")                   "\n\n"
       "kline-list: "   (print-kline-list market-data)))

(defn- download-market-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (let [market-data (a/subscribed [:db/get-item [:trader :market]])]
       (http/text-wrap {:body (print-market-data market-data)})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- print-scheduler-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [scheduler-data]
  (str scheduler-data))

(defn- download-scheduler-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (let [scheduler-data (a/subscribed [:db/get-item [:trader :scheduler]])]
       (http/text-wrap {:body (print-scheduler-data scheduler-data)})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot {:dispatch-n [[:router/add-route! :trader/download-listener-data
                                                     {:route-template "/@app-home/trader/listener-data"
                                                      :get (fn [request] (download-listener-data request))
                                                      :restricted? true}]
                                 [:router/add-route! :trader/download-log-data
                                                     {:route-template "/@app-home/trader/log-data"
                                                      :get (fn [request] (download-log-data request))
                                                      :restricted? true}]
                                 [:router/add-route! :trader/download-market-data
                                                     {:route-template "/@app-home/trader/market-data"
                                                      :get (fn [request] (download-market-data request))
                                                      :restricted? true}]
                                 [:router/add-route! :trader/download-scheduler-data
                                                     {:route-template "/@app-home/trader/scheduler-data"
                                                      :get (fn [request] (download-scheduler-data request))
                                                      :restricted? true}]]}})
