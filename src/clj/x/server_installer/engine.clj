
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.18
; Description:
; Version: v0.4.2
; Compatibility: x4.4.1



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-installer.engine
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map]
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
  (let [timestamp (time/timestamp)]
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
  (let [installed? (get-in db (db/path ::modules module-id :installed?))]
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
  (assoc-in db (db/path ::modules module-id :installed?)
               (param true)))

(a/reg-event-db :x.server-installer/->module-installed ->module-installed)



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- ->server-installed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (println details/app-name "installed version:" details/app-version)
  (let [install-details (install-details-prototype)]
       (if (io/file-exists?       a/SERVER-CONFIG-FILEPATH)
           (do (io/swap-edn-file! a/SERVER-CONFIG-FILEPATH assoc :install-details install-details))
           (do (io/create-file!   a/SERVER-CONFIG-FILEPATH)
               (io/swap-edn-file! a/SERVER-CONFIG-FILEPATH assoc :install-details install-details))))
  ;
  (println details/app-name "exiting ...")
  (System/exit 0))

(a/reg-handled-fx :x.server-installer/->server-installed ->server-installed)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.server-installer/install-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      (println details/app-name "installing ...")
       ; Installing modules
      {:dispatch-n [[:x.server-installer/install-db!]
                    [:x.server-installer/install-media!]
                    [:x.server-installer/install-user!]]
       ; Running self-test
       :dispatch-tick [{:tick 100 :dispatch [:x.server-installer/self-test!]}]}))

(a/reg-event-fx
  :x.server-installer/self-test!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (println details/app-name "self-testing installation ...")
      (if (and (r module-installed? db :db)
               (r module-installed? db :media)
               (r module-installed? db :user))
          [:x.server-installer/->server-installed]
          (println details/app-name "installation error"))))
