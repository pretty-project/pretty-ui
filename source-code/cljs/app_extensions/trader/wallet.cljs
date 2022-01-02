
(ns app-extensions.trader.wallet
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.string    :as string]
              [mid-fruits.time      :as time]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [app-extensions.trader.account    :as account]
              [app-extensions.trader.connection :as connection]
              [app-extensions.trader.engine     :as engine]
              [app-extensions.trader.styles     :as styles]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-wallet-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ wallet-id]]
  (let [wallet-balance (get-in db [:trader :wallet wallet-id :wallet-balance])
        btc-equity     (get-in wallet-balance [:BTC :equity])]
       (merge (r account/get-account-props db)
              (get-in db [:trader :wallet wallet-id])
              {:synchronized? (some? wallet-balance)
               :failured?     (and (some? wallet-balance)
                                   (nil? btc-equity))})))

(a/reg-sub :trader/get-wallet-props get-wallet-props)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- init-wallet!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ wallet-id]]
  (r connection/init-connection! db wallet-id {:on-connect [:trader/request-wallet-balance! wallet-id]
                                               :connection-interval 30}))

(a/reg-event-db :trader/init-wallet! init-wallet!)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- wallet-login
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [wallet-id {:keys [failured?] :as wallet-props}]
  [:div {:style (styles/overlay-center)}
        [:div [:div {:style (styles/row {:justify-content "flex-start"})}]
              [elements/horizontal-separator {:size :xxl}]
              [account/api-fields wallet-id wallet-props]
              (if failured? [elements/label {:content "Invalid wallet data" :color :warning :font-size :s :indent :both}])
              [elements/horizontal-separator {:size :xxl}]]])

(defn- wallet-balance
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [api-key timestamp wallet-balance]}]
  (let [timestamp  (time/timestamp-string->date-time timestamp)
        btc-equity (get-in wallet-balance [:BTC :equity])
        eth-equity (get-in wallet-balance [:ETH :equity])]
       [:div {:style (styles/overlay-center)}
             [elements/label {:color :primary :layout :fit :font-size :m  :content (str "API-key: "         api-key)}]
             [elements/label {:color :muted   :layout :fit :font-size :xs :content (str "Synchronized at: " timestamp)}]
             [elements/horizontal-separator {:size :m}]
             [elements/label {:content (str btc-equity " BTC")
                              :font-size :xxl}]
             [elements/label {:content (str eth-equity " ETH")
                              :font-size :xxl}]]))


(defn- wallet
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [wallet-id {:keys [api-key api-secret failured? synchronized?] :as wallet-props}]
  [:div {:style (styles/box-structure-style)}
        [:div {:style (styles/box-body-style)}
              (if (and synchronized? (not failured?))
                  [wallet-balance wallet-id wallet-props]
                  [wallet-login   wallet-id wallet-props])
              [:div {:style (styles/box-tl-controls-style)}
                    [connection/connection-toggle-button wallet-id {:disabled? (not (and (string/nonempty? api-key)
                                                                                         (string/nonempty? api-secret)))}]
                    [elements/vertical-separator {:size :xxl}]
                    [elements/label {:icon :account_balance_wallet :content "My wallet" :font-size :xxl :layout :fit}]]]])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [components/subscriber :wallet/my-wallet
                         {:component  #'wallet
                          :subscriber [:trader/get-wallet-props :wallet/my-wallet]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :trader/receive-wallet-balance!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ wallet-id response]]
      (let [wallet-data (get response :trader/get-wallet-balance)
            btc-equity  (get-in wallet-data [:wallet-balance :BTC :equity])]
           {:db       (assoc-in db [:trader :wallet wallet-id] wallet-data)
            :dispatch [:trader/log! :trader/wallet (get wallet-data :uri)
                                                   (get wallet-data :timestamp)]
            :dispatch-cond [(nil? btc-equity)
                            [:trader/stop-connection! wallet-id]
                            (nil? btc-equity)
                            [:trader/log! :trader/wallet "Invalid wallet data"]]})))

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/request-wallet-balance!
  (fn [{:keys [db]} [_ wallet-id]]
      [:sync/send-query! :trader/synchronize!
                         {:on-failure [:trader/->account-network-error]
                          :on-success [:trader/receive-wallet-balance! wallet-id]
                          :on-sent    [:trader/log! :trader/account "Fetching data from server ..."]
                          :query      [`(:trader/get-wallet-balance ~(r account/get-api-details db))]}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:trader/init-wallet! :wallet/my-wallet]})
