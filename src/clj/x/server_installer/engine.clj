
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.18
; Description:
; Version: v0.2.2
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



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn server-installed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (let [installed-version (r a/get-install-detail db :installed-version)]
       (some? installed-version)))

(defn- module-installed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) module-id
  ;
  ; @return (boolean)
  [db [_ module-id]]
  (let [installed? (get-in db (db/path ::modules module-id :installed?))]
       (boolean installed?)))

(defn get-installed-at
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  [db _]
  (r a/get-install-detail db :installed-at))

(defn get-installed-version
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  [db _]
  (r a/get-install-detail db :installed-version))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ->module-installed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) module-id
  ;
  ; @return (map)
  [db [_ module-id]]
  (assoc-in db (db/path ::modules module-id :installed?)
               (param true)))

(a/reg-event-db :x.server-installer/->module-installed ->module-installed)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.server-installer/install-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
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
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (println details/app-name "self-test install")
      (if (and (r module-installed? db :db)
               (r module-installed? db :media)
               (r module-installed? db :user))
          [:x.server-installer/->server-installed]
          (println details/app-name "installation error"))))

(a/reg-handled-fx
  :mark-server-as-installed!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      (let [timestamp       (time/timestamp)
            install-details {:installed-at      timestamp
                             :installed-version details/app-version}]
           [:x.app-core/swap-server-config! assoc-in :install-details install-details])))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.server-installer/->server-installed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      (println details/app-name "installed version:" details/app-version)
      {:dispatch-n [[:x.boot-loader/run-app!]]}))
                    ;[:mark-server-as-installed!]]}))
