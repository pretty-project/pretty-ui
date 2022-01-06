
(ns app-extensions.trader.sync
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.map     :as map :refer [dissoc-in]]
              [mid-fruits.vector  :as vector]
              [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]
              [app-extensions.trader.styles :as styles]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
(def SYNC-TIMEOUT 2000)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-response
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ resolver-id]]
  (get-in db [:trader :sync :responses resolver-id]))

(defn responsed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ resolver-id]]
  (some? (get-in db [:trader :sync :responses resolver-id])))

(defn synchronized?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (some? (get-in db [:trader :sync :responses])))

(defn subscribed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ subscription-id]]
  (some? (get-in db [:trader :sync :subscriptions subscription-id])))

(defn sync-active?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (get-in db [:trader :sync :active?]))

(defn- get-subscription-queries
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [subscriptions (get-in db [:trader :sync :subscriptions])]
       (reduce-kv #(vector/concat-items %1 (:query %3)) [] subscriptions)))

(defn- any-subscription-added?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [subscriptions (get-in db [:trader :sync :subscriptions])]
       (map/nonempty? subscriptions)))

(defn- synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (and (r any-subscription-added? db)
       (r sync-active?            db)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- remove-response!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ resolver-id]]
  (dissoc-in db [:trader :sync :responses resolver-id]))

(defn- add-subscription!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ subscription-id subscription-props]]
  (assoc-in db [:trader :sync :subscriptions subscription-id] subscription-props))

(defn- remove-subscription!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ subscription-id]]
  (dissoc-in db [:trader :sync :subscriptions subscription-id]))

(a/reg-event-db :trader/remove-subscription! remove-subscription!)

(defn- start-syncing!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (assoc-in db [:trader :sync :active?] true))

(defn- stop-syncing!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (dissoc-in db [:trader :sync :active?]))

(defn- toggle-syncing!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (update-in db [:trader :sync :active?] not))

(a/reg-event-db :trader/toggle-syncing! toggle-syncing!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn synchronizing-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [responsed? subscribed?]}]
  (if (and subscribed? (not responsed?))
      [:div {:style (styles/synchronizing-label-style)}
            [elements/label {:content "Downloading data ..." :color :highlight}]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/sync-subscriptions!
  (fn [{:keys [db]} _]
      (if (r synchronizing? db)
          {:dispatch-later [{:ms SYNC-TIMEOUT :dispatch [:trader/sync-subscriptions!]}]
           :dispatch [:sync/send-query! :trader/sync-subscriptions!
                                        {:query       (r get-subscription-queries db)
                                         :target-path [:trader :sync :responses]}]}
          {:db (r stop-syncing! db)})))

(a/reg-event-fx
  :trader/start-syncing!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]}]
      {:db       (r start-syncing! db)
       :dispatch [:trader/sync-subscriptions!]}))

(a/reg-event-fx
  :trader/add-subscription!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) subscription-id
  ; @param (map) subscription-props
  ;  {:query (vector)
  ;   :target-paths (item-path vectors in map)}
  (fn [{:keys [db]} [_ subscription-id subscription-props]]
      (if (r sync-active? db)
          {:db (r add-subscription! db subscription-id subscription-props)}
          {:db (r add-subscription! db subscription-id subscription-props)
           :dispatch [:trader/start-syncing!]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) query-id
  ; @param (map) query-props
  ;  {:query (vector)}
  ;
  ; @usage
  ;  [:trader/send-query! :trader/my-module {:query [`(trader/do-something! ~{})]}]
  :trader/send-query!
  (fn [{:keys [db]} [_ module-id {:keys [query]}]]
      [:sync/send-query! :trader/synchronize!
                         {:query       (vector/concat-items query (r get-subscription-queries db))
                          :target-path [:trader :sync :responses]}]))
