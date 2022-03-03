
(ns app-extensions.trader.sync
    (:require [app-extensions.trader.styles :as styles]
              [mid-fruits.candy             :refer [param return]]
              [mid-fruits.map               :as map :refer [dissoc-in]]
              [mid-fruits.vector            :as vector]
              [x.app-core.api               :as a :refer [r]]
              [x.app-elements.api           :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
(def SYNC-TIMEOUT 1000)



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
  :trader/subscribe-to-query!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) subscription-id
  ; @param (map) subscription-props
  ;  {:query (vector)}
  (fn [{:keys [db]} [_ subscription-id subscription-props]]
      (if (r sync-active? db)
          {:db (r add-subscription! db subscription-id subscription-props)}
          {:db (as-> db % (r add-subscription! % subscription-id subscription-props)
                          (r start-syncing!    %))
           :dispatch [:trader/sync-subscriptions!]})))

(a/reg-event-fx
  :trader/unsubscribe-from-query!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ subscription-id]]
      {:db (r remove-subscription! db subscription-id)}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/receive-app-data!
  (fn [{:keys [db]} [_ server-response]]
      {:db (-> db (assoc-in  [:trader :settings]       (:trader/download-settings      server-response))
                  ;(assoc-in  [:trader :listener]       (:trader/download-listener-data server-response))
                  (assoc-in  [:trader :editor]         (:trader/download-editor-data   server-response))
                  (update-in [:trader :account]  merge (:trader/download-api-details   server-response)))}))

(a/reg-event-fx
  :trader/download-app-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:sync/send-query! :trader/synchronize!
                     {:display-progress? true
                      :query [:debug ;`(:trader/download-listener-data ~{})
                                     `(:trader/download-settings      ~{})
                                     `(:trader/download-api-details   ~{})
                                     `(:trader/download-editor-data   ~{})]
                      :on-success [:trader/receive-app-data!]}])
