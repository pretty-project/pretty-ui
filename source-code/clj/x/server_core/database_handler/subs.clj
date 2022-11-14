
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.database-handler.subs
    (:require [mid.x.core.config-handler.subs :as config-handler.subs]
              [re-frame.api                   :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-database-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  [db _]
  (r config-handler.subs/get-server-config-item db :database-name))

(defn get-database-host
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  [db _]
  (if-let [is-docker? (System/getenv "DOCKER")]
          (r config-handler.subs/get-server-config-item db :docker-database-host)
          (r config-handler.subs/get-server-config-item db :database-host)))

(defn get-database-port
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  [db _]
  (r config-handler.subs/get-server-config-item db :database-port))
