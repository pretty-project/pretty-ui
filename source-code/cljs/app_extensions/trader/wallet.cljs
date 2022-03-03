
(ns app-extensions.trader.wallet
    (:require [app-extensions.trader.account :as account]
              [app-extensions.trader.styles  :as styles]
              [app-extensions.trader.sync    :as sync]
              [mid-fruits.time               :as time]
              [x.app-components.api          :as components]
              [x.app-core.api                :as a :refer [r]]
              [x.app-elements.api            :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-wallet-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (merge (get-in db [:trader :wallet])
         (r sync/get-response db :trader/download-account-data)
         {:responsed?  (r sync/responsed?  db :trader/download-account-data)
          :subscribed? (r sync/subscribed? db :trader/account)}))

(a/reg-sub :trader/get-wallet-props get-wallet-props)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- wallet-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [wallet-balance]}]
  (let [btc-equity (get-in wallet-balance [:BTC :equity])
        eth-equity (get-in wallet-balance [:ETH :equity])]
      [:<> [elements/label {:content (str btc-equity " BTC")
                            :font-size :xxl :indent :both}]
           [elements/label {:content (str eth-equity " ETH")
                            :font-size :xxl :indent :both}]]))

(defn- api-key-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [api-key]}]
  [elements/label {:color :primary :layout :fit :font-size :m  :content (str "API-key: " api-key)}])

(defn- api-synchronized-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [timestamp]}]
  (let [timestamp (time/timestamp-string->date-time timestamp)]
       [elements/label {:color :muted :layout :fit :font-size :xs :content (str "Synchronized at: " timestamp)}]))

(defn mainnet-indicator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [use-mainnet?]}]
  (if use-mainnet? [elements/icon {:layout :touch-target :icon :fingerprint
                                   :color :warning}]))

(defn- wallet-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props]
  [:div {:style (styles/overlay-center-style)}
        [api-key-label          module-id module-props]
        [api-synchronized-label module-id module-props]
        [elements/horizontal-separator {:size :m}]
        [wallet-data            module-id module-props]
        [:div {:style (styles/box-tr-controls-style)}
              [mainnet-indicator module-id module-props]]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- wallet
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id {:keys [error] :as module-props}]
  [:div {:style (styles/box-structure-style)}
        [:div {:style (styles/box-body-style)}
              [:div {:style (styles/box-tc-controls-style)}
                    [elements/label {:content "My wallet" :font-size :m :font-weight :extra-bold}]]
              (case error :missing-api-details [account/missing-api-details-label module-id module-props]
                          :invalid-api-details [account/invalid-api-details-label module-id module-props]
                          [wallet-structure module-id module-props])
              [sync/synchronizing-label module-id module-props]]])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [components/subscriber :trader/wallet
                         {:render-f   #'wallet
                          :subscriber [:trader/get-wallet-props]}])
