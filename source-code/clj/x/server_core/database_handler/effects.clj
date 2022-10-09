
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.database-handler.effects
    (:require [re-frame.api                        :as r :refer [r]]
              [x.app-details                       :as details]
              [x.server-core.database-handler.subs :as database-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx
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
