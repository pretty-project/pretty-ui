
(ns server-extensions.trader.monitor
    (:require [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [mid-fruits.candy                      :refer [param return]]
              [pathom.api                            :as pathom]
              [server-extensions.trader.account      :as account]
              [server-extensions.trader.engine       :as engine]
              [server-extensions.trader.klines       :as klines]
              [server-extensions.trader.settings     :as settings]
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
