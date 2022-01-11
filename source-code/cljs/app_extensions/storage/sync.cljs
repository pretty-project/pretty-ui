
(ns app-extensions.storage.sync
    (:require [x.app-core.api :as a :refer [r]]
              [app-extensions.storage.capacity-handler :as capacity-handler]
              [app-extensions.storage.engine           :as engine]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- receive-server-response!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ server-response]]
  (as-> db % (r capacity-handler/receive-capacity-details! db server-response)))

(a/reg-event-db :storage/receive-server-response! receive-server-response!)
