
(ns extensions.trader.log
    (:require [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [extensions.trader.engine              :as engine]
              [mid-fruits.candy                      :refer [param return]]
              [mid-fruits.time                       :as time]
              [mid-fruits.vector                     :as vector]
              [pathom.api                            :as pathom]
              [x.server-core.api                     :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn log!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) log-entry
  ; @param (map) options
  ;  {:highlighted? (boolean)(opt)
  ;    Default: false}
  ;   :warning? (boolean)(opt)
  ;    Default: false}
  [db [_ log-entry {:keys [highlighted? warning?]}]]
  (update-in db [:trader :log] vector/cons-item {:log-entry    (param   log-entry)
                                                 :highlighted? (boolean highlighted?)
                                                 :warning?     (boolean warning?)
                                                 :timestamp    (time/timestamp-string)}))

; @usage
;  [:trader/log! "My log entry" {:warning? true}]
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
  (let [log-items @(a/subscribe [:db/get-item [:trader :log]])]
       {:log-items log-items}))

(defresolver download-log-data
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
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

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:trader/log! "Initializing server ..."]})
