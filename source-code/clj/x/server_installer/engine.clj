
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.18
; Description:
; Version: v0.5.6
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-installer.engine
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.time   :as time]
              [server-fruits.io  :as io]
              [x.app-details     :as details]
              [x.server-core.api :as a :refer [r]]
              [x.server-db.api   :as db]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- install-details-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:installed-at (string)
  ;   :installed-version (string)}
  []
  (let [timestamp (time/timestamp-string)]
       {:installed-at      (str timestamp)
        :installed-version (str details/app-version)}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn server-installed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (let [installed-version (r a/get-install-detail db :installed-version)]
       (some? installed-version)))

(defn- module-installed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) module-id
  ;
  ; @return (boolean)
  [db [_ module-id]]
  (let [installed? (get-in db (db/path :installer/modules module-id :installed?))]
       (boolean installed?)))

(defn get-installed-at
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  [db _]
  (r a/get-install-detail db :installed-at))

(defn get-installed-version
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  [db _]
  (r a/get-install-detail db :installed-version))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ->module-installed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) module-id
  ;
  ; @return (map)
  [db [_ module-id]]
  (assoc-in db (db/path :installer/modules module-id :installed?)
               (param true)))

(a/reg-event-db :installer/->module-installed ->module-installed)



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- ->server-installed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (println details/app-codename "installed version:" details/app-version)
  (let [install-details (install-details-prototype)]
       (if (io/file-exists?       a/SERVER-CONFIG-FILEPATH)
           (do (io/swap-edn-file! a/SERVER-CONFIG-FILEPATH assoc :install-details install-details))
           (do (io/create-file!   a/SERVER-CONFIG-FILEPATH)
               (io/swap-edn-file! a/SERVER-CONFIG-FILEPATH assoc :install-details install-details)))))

  ; Sikeres telepítés után elindítja a szervert ...
  ; (println details/app-codename "exiting ...")
  ; (System/exit 0)

(a/reg-fx :installer/->server-installed ->server-installed)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :installer/install-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      (println details/app-codename "installing ...")
       ; Installing modules
      {:dispatch-n [[:installer/install-db!]
                    [:installer/install-user!]]
       ; Running self-test
       :dispatch-tick [{:tick 100 :dispatch [:installer/self-test!]}]}))

(a/reg-event-fx
  :installer/self-test!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (println details/app-codename "self-testing installation ...")
      (if (and (r module-installed? db :db)
               (r module-installed? db :media)
               (r module-installed? db :user))
          {:installer/->server-installed nil
           :dispatch [:boot-loader/start-server!]}
          (println details/app-codename "installation error"))))
