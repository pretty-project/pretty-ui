
(ns server-extensions.trader.monitor
    (:require [mid-fruits.candy   :refer [param return]]
              [pathom.api         :as pathom]
              [x.server-core.api  :as a]
              [server-extensions.trader.account      :as account]
              [server-extensions.trader.engine       :as engine]
              [server-extensions.trader.klines       :as klines]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-monitor-data-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  [env _]
  (klines/query-kline-data {:interval     (pathom/env->param  env :interval)
                            :limit        (pathom/env->param  env :limit)
                            :symbol       (pathom/env->param  env :symbol)
                            :use-mainnet? (account/get-api-detail :use-mainnet?)}))

(defresolver get-monitor-data
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:trader/get-monitor-data (map)}
             [env resolver-props]
             {:trader/get-monitor-data (get-monitor-data-f env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-monitor-data])

(pathom/reg-handlers! ::handlers HANDLERS)
