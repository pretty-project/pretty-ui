
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



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :core/connect-to-database!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [database-name (r config-handler/get-database-detail db :database-name)
            database-host (r config-handler/get-database-detail db :database-host)
            database-port (r config-handler/get-database-detail db :database-port)]
           (println details/app-codename "connecting to:" database-name
                                         "database at:"   database-host
                                         "on port:"       database-port)
           {:mongo-db/build-connection! [database-name database-host database-port]})))
