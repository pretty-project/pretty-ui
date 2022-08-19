
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.database-handler.effects
    (:require [x.app-details                       :as details]
              [x.server-core.database-handler.subs :as database-handler.subs]
              [x.server-core.event-handler         :as event-handler :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :core/connect-to-database!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [database-name (r database-handler.subs/get-database-name db)
            database-host (r database-handler.subs/get-database-host db)
            database-port (r database-handler.subs/get-database-port db)]
           (println details/app-codename "connecting to:" database-name
                                           "database at:" database-host
                                               "on port:" database-port)
           {:fx [:mongo-db/build-connection! database-name database-host database-port]})))
