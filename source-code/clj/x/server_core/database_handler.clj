
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.18
; Description:
; Version: v0.2.4
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.database-handler
    (:require [x.app-details               :as details]
              [x.mid-core.config-handler   :as config-handler]
              [x.server-core.event-handler :as event-handler :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-database-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  [db _]
  (r config-handler/get-server-config-item db :database-name))

(defn- get-database-host
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  [db _]
  (if-let [is-docker? (System/getenv "DOCKER")]
          (r config-handler/get-server-config-item db :docker-database-host)
          (r config-handler/get-server-config-item db :database-host)))

(defn- get-database-port
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  [db _]
  (r config-handler/get-server-config-item db :database-port))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :core/connect-to-database!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [database-name (r get-database-name db)
            database-host (r get-database-host db)
            database-port (r get-database-port db)]
           (println details/app-codename "connecting to:" database-name
                                           "database at:" database-host
                                               "on port:" database-port)
           {:fx [[:mongo-db/build-connection! [database-name database-host database-port]]]})))
