
(ns server-extensions.trader.account
    (:require [clj-http.client    :as client]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.map     :as map]
              [pathom.api         :as pathom]
              [x.server-core.api  :as a]
              [server-extensions.trader.engine :as engine]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-api-key-info
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env _]
  (let [api-key      (pathom/env->param env :api-key)
        api-secret   (pathom/env->param env :api-secret)
        use-mainnet? (pathom/env->param env :use-mainnet?)
        uri          (if use-mainnet? (engine/api-key-info-uri      {:api-key api-key :api-secret api-secret})
                                      (engine/api-key-info-test-uri {:api-key api-key :api-secret api-secret}))
        response     (client/get uri)]
       (println "trader/account: get data from" uri)
       (-> response (engine/get-response->body)
                    (map/rekey-item :result :api-key-info)
                    (assoc :uri uri))))

(defresolver get-api-key-info
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env resolver-props]
             {:trader/get-api-key-info (download-api-key-info env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def HANDLERS [get-api-key-info])

(pathom/reg-handlers! :trader/account HANDLERS)
