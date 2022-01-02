
(ns app-extensions.trader.connection
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.css       :as css]
              [mid-fruits.format    :as format]
              [mid-fruits.keyword   :as keyword]
              [mid-fruits.loop      :refer [reduce-indexed]]
              [mid-fruits.map       :as map :refer [dissoc-in]]
              [mid-fruits.math      :as math]
              [mid-fruits.pretty    :as pretty]
              [mid-fruits.reader    :as reader]
              [mid-fruits.random    :as random]
              [mid-fruits.string    :as string]
              [mid-fruits.svg       :as svg]
              [mid-fruits.time      :as time]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-sync.api       :as sync]
              [x.app-elements.api   :as elements]
              [app-fruits.react-transition  :as react-transition]
              [app-extensions.trader.engine :as engine]
              [app-extensions.trader.styles :as styles]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------


;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- connection-watching?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ connection-id]]
  (get-in db [:trader :connection connection-id :connection-watching?]))

(defn- get-connection-interval
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ connection-id]]
  (get-in db [:trader :connection connection-id :connection-interval]))

(defn- get-connection-time-left
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ connection-id]]
  (get-in db [:trader :connection connection-id :connection-time-left]))

(defn- connection-time-elapsed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ connection-id]]
  (= 0 (r get-connection-time-left db connection-id)))

(defn- get-connection-toggle-button-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ connection-id]]
  {:connection-interval  (r get-connection-interval    db connection-id)
   :connection-time-left (r get-connection-time-left   db connection-id)
   :connection-watching? (r connection-watching?       db connection-id)
   :synchronizing?       (r sync/listening-to-request? db :trader/synchronize!)})

(a/reg-sub :trader/get-connection-toggle-button-props get-connection-toggle-button-props)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-connection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) connection-id
  ; @param (map) connection-props
  ;  {:connection-interval (s)
  ;   :on-connect (metamorphic-event)}
  [db [_ connection-id connection-props]]
  (assoc-in db [:trader :connection connection-id] connection-props))

(a/reg-event-db :trader/init-connection! init-connection!)

(defn- stop-connection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ connection-id]]
  (-> db (dissoc-in [:trader :connection connection-id :connection-watching?])
         (dissoc-in [:trader :connection connection-id :connection-time-left])))

(a/reg-event-db :trader/stop-connection! stop-connection!)

(defn- reset-timer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ connection-id]]
  (let [connection-interval (r get-connection-interval db connection-id)]
       (assoc-in db [:trader :connection connection-id :connection-time-left] connection-interval)))

(defn- start-connection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ connection-id]]
  (let [connection-interval (r get-connection-interval db connection-id)]
       (-> db (assoc-in [:trader :connection connection-id :connection-watching?] true)
              (assoc-in [:trader :connection connection-id :connection-time-left] connection-interval))))

(defn- update-connection-time!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ connection-id]]
  (update-in db [:trader :connection connection-id :connection-time-left] dec))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- connection-timer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [connection-interval connection-time-left]}]
  [elements/circle-diagram {:sections [{:color :primary   :value connection-time-left}
                                       {:color :highlight :value (- connection-interval connection-time-left)}]}])

(defn- connection-toggle-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [connection-id _]
  (let [toggle-button-props (a/subscribe [:trader/get-connection-toggle-button-props connection-id])]
       (fn [_ {:keys [disabled?]}]
           [:div {:style {:opacity (if disabled? ".5" "1")}}
                 [:div {:style (styles/connection-timer-style)}
                       [connection-timer connection-id @toggle-button-props]]
                 [elements/button {:disabled? (or disabled? (:synchronizing? @toggle-button-props))
                                   :icon      (if (:connection-watching? @toggle-button-props) :pause :play_arrow)
                                   :on-click  [:trader/toggle-connection! connection-id]
                                   :preset    :default-icon-button}]])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :trader/toggle-connection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ connection-id]]
      (let [on-connect (get-in db [:trader :connection connection-id :on-connect])]
           (if (r connection-watching? db connection-id)
               ; If connection is watching ...
               {:db (r stop-connection!  db connection-id)}
               ; If connection is NOT watching ...
               {:db (r start-connection! db connection-id)
                :dispatch-n [on-connect [:trader/update-connection-time! connection-id]]}))))

(a/reg-event-fx
  :trader/update-connection-time!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ connection-id]]
      (let [on-connect (get-in db [:trader :connection connection-id :on-connect])]
           (cond ; If connection is watching and time is elapsed ...
                 (and (r connection-watching?     db connection-id)
                      (r connection-time-elapsed? db connection-id))
                 {:db (r reset-timer! db connection-id)
                  :dispatch on-connect
                  :dispatch-later [{:ms 1000 :dispatch [:trader/update-connection-time! connection-id]}]}
                 ; If connection is watching and connection-time is NOT elapsed ...
                 (r connection-watching? db connection-id)
                 {:db (r update-connection-time! db connection-id)
                  :dispatch-later [{:ms 1000 :dispatch [:trader/update-connection-time! connection-id]}]}))))
