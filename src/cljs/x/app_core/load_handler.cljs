
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.02.13
; Description:
; Version: v0.9.4
; Compatibility: x4.3.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.load-handler
    (:require [mid-fruits.candy             :refer [return]]
              [mid-fruits.time              :as time]
              [x.app-core.event-handler     :as event-handler :refer [r]]
              [x.app-core.lifecycle-handler :as lifecycle-handler]))



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  XXX#5607
;  Az applikáció elemeinek betöltésekor lehetőség van az egyes elemek aszinkron
;  inicializására.
;
; @description
;  Az elem inicializálásának kezdetekor az [::synchronize-loading!]
;  eseménnyel lehet jelezni az app-loader számára, hogy addig ne tekintse
;  befejezettnek az applikáció betöltését, amíg ugyanez az elem nem indítja
;  el a [::->synchron-signal] eseményt.



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
;  App loading timeout
(def LOAD-TIMEOUT       30000)

; @constant (string)
(def LOAD-TIMEOUT-ERROR "Load timeout error")



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-elapsed-time
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (ms)
  ;  Az applikáció betöltésének kezdete óta eltelt idő
  [db _]
  (let [elapsed-time    (time/elapsed)
        load-started-at (get-in db [::primary :data-items :load-started-at])]
       (- elapsed-time load-started-at)))

(defn- get-app-status
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [db _]
  (get-in db [::primary :data-items :status]))

(defn- timeout-reached?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (> (r get-elapsed-time db) LOAD-TIMEOUT))

(defn- synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (let [app-status (r get-app-status db)]
       (or (= app-status :loading)
           (= app-status nil))))

(defn- get-signals-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (integer)
  [db _]
  (get-in db [::primary :data-items :signals]))

(defn- synchronized?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Az applikáció betöltése akkor számít befejezettnek, ha az összes várt
  ; [::->synchron-signal] esemény megtörtént és a várokozások száma 0-ra csökkent.
  ;
  ; @return (boolean)
  [db _]
  (let [signals-count (r get-signals-count db)]
       (and (r synchronizing? db)
            (= 0 signals-count))))

(defn- timeout-error?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (and (r timeout-reached? db)
       (r synchronizing?   db)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- reg-load-started!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (assoc-in db [::primary :data-items :load-started-at]
               (time/elapsed)))

(defn- set-app-status!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) status
  ;
  ; @return (map)
  [db [_ status]]
  (assoc-in db [::primary :data-items :status] status))

(event-handler/reg-event-db :x.app-core/set-app-status! set-app-status!)

(defn synchronize-loading!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) signal-id
  ;
  ; @return (map)
  [db [_ signal-id]]
  (update-in db [::primary :data-items :signals] inc))

(event-handler/reg-event-db :x.app-core/synchronize-loading! synchronize-loading!)

(defn stop-synchronizing!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (if (r synchronizing?  db)
      (r set-app-status! db :halted)
      (return            db)))

(event-handler/reg-event-db :x.app-core/stop-synchronizing! stop-synchronizing!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :x.app-core/self-test!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
            ; 1.
      (cond (r synchronized?  db) {:dispatch [:x.app-core/->app-loaded]}
            ; 2.
            (r timeout-error? db) {:dispatch [:x.app-core/->timeout-reached]})))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :x.app-core/->synchron-signal
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if (r synchronizing? db)
          {:db (update-in db [::primary :data-items :signals] dec)
           :dispatch [:x.app-core/self-test!]})))

(event-handler/reg-event-fx
  :x.app-core/->app-loaded
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r set-app-status! db :loaded)
       :dispatch [:x.app-ui/hide-shield!]}))

(event-handler/reg-event-fx
  :x.app-core/->timeout-reached
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r set-app-status! db :loaded)
       :dispatch [:x.app-ui/set-shield! {:content LOAD-TIMEOUT-ERROR}]}))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :x.app-core/initialize-load-handler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [event-id]]
      {:db (-> db (set-app-status!   [event-id :loading])
                  (reg-load-started! [event-id]))
       :dispatch-later [{:ms LOAD-TIMEOUT :dispatch [:x.app-core/self-test!]}]}))

(lifecycle-handler/reg-lifecycles
  ::lifecycles
  {:on-app-init [:x.app-core/initialize-load-handler!]})
