
(ns server-extensions.trader.log
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.time   :as time]
              [mid-fruits.vector :as vector]
              [pathom.api        :as pathom]
              [x.server-core.api :as a]
              [server-extensions.trader.engine       :as engine]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn log!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) module-id
  ; @param (string) log-entry
  ; @param (map) options
  ;  {:highlighted? (boolean)(opt)
  ;    Default: false}
  ;   :warning? (boolean)(opt)
  ;    Default: false}
  [db [_ module-id log-entry {:keys [highlighted? warning?]}]]
  (update-in db [:trader :log] vector/cons-item {:module-id module-id :log-entry log-entry
                                                 :timestamp    (time/timestamp-string)
                                                 :highlighted? (boolean highlighted?)
                                                 :warning?     (boolean warning?)}))

; @usage
;  [:trader/log! :my-module "My log entry" {:warning? true}]
(a/reg-event-db :trader/log! log!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-log-data-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ;  {:log-items (maps in vector)}
  [env response-props]
  (let [log-items (a/subscribed [:db/get-item [:trader :log]])]
       {:log-items log-items}))

(defresolver download-log-data
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:trader/download-log-data (map)
             ;    {:log-items (maps in vector)}}
             [env resolver-props]
             {:trader/download-log-data (download-log-data-f env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [download-log-data])

(pathom/reg-handlers! ::handlers HANDLERS)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-boot [:trader/log! :trader/log "Initializing server ..."]})
