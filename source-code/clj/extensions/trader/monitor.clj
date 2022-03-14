
(ns extensions.trader.monitor
    (:require [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [extensions.trader.account             :as account]
              [extensions.trader.engine              :as engine]
              [extensions.trader.klines              :as klines]
              [extensions.trader.settings            :as settings]
              [mid-fruits.candy                      :refer [param return]]
              [pathom.api                            :as pathom]
              [x.server-core.api                     :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-monitor-data-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  [env _]
  (klines/request-kline-data! {:interval     (pathom/env->param env :interval)
                               :limit        (pathom/env->param env :limit)
                               :symbol       (-> :symbol       settings/get-settings-item :value)
                               :use-mainnet? (-> :use-mainnet? account/get-api-details-item)}))

(defresolver download-monitor-data
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:trader/download-monitor-data (map)}
             [env resolver-props]
             {:trader/download-monitor-data (download-monitor-data-f env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [download-monitor-data])

(pathom/reg-handlers! ::handlers HANDLERS)
