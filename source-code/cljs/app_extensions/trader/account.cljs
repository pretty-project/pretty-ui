
(ns app-extensions.trader.account
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.string    :as string]
              [mid-fruits.time      :as time]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [app-extensions.trader.engine :as engine]
              [app-extensions.trader.styles :as styles]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-mainnet?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (get-in db [:trader :account :use-mainnet?]))

(defn- get-api-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:api-key      (get-in db [:trader :account :api-key])
   :api-secret   (get-in db [:trader :account :api-secret])
   :use-mainnet? (r use-mainnet? db)})

(defn get-account-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (get-in db [:trader :account]))

(a/reg-sub :trader/get-account-props get-account-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mainnet-indicator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [use-mainnet?]}]
  (if use-mainnet? [elements/icon {:layout :touch-target :icon :fingerprint
                                   :color :warning}]))

(defn- get-api-key-info-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [api-key api-secret]}]
  [elements/button ::get-api-key-info-button
                   {:label "Get API key info" :layout :fit
                    :on-click [:trader/request-api-key-info!]
                    :disabled? (not (and (string/nonempty? api-key)
                                         (string/nonempty? api-secret)))}])

(defn- api-key-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/text-field {:label "API key"
                        :min-width :s :indent :both :disable-autofill? false :name :favorite-color ; az api-key name nem autofill
                        :value-path [:trader :account :api-key]}])

(defn- api-secret-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/text-field {:label "API secret"
                        :min-width :s :indent :both
                        :value-path [:trader :account :api-secret]}])

(defn- api-key-expiration-label
  [_ {:keys [api-key-info]}]
  (if-let [expiration-date (get-in api-key-info [:api-key-info 0 :expired_at])]
          [elements/label {:content (str "Expiration date: " (time/timestamp-string->date-time expiration-date))
                           :layout :fit :indent :left :color :primary :font-size :xs}]))

(defn api-fields
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [account-id account-props]
  [:<> [:div {:style (styles/row {:justify-content "center"})}
             [api-key-field    account-id account-props]
             [api-secret-field account-id account-props]]
       [:div {:style (styles/row {:justify-content "flex-end"})}
             [elements/switch {:label "api.bybit.com" :secondary-label "testnet-api.bybit.com"
                               :font-size :xs :border-color :default :indent :right :initial-value false
                               :value-path [:trader :account :use-mainnet?]}]]])

(defn- account
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [account-id account-props]
  [:div {:style (styles/overlay-center)}
        [:div [api-fields               account-id account-props]
              [api-key-expiration-label account-id account-props]
              [elements/horizontal-separator {:size :xxl}]
              [:div {:style (styles/row {:justify-content "flex-end"})}
                    [get-api-key-info-button  account-id account-props]]]])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [view-id]
  [components/subscriber view-id
                         {:component  #'account
                          :subscriber [:trader/get-account-props]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :trader/receive-api-key-info!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ response]]
      (let [api-key-info (get response :trader/get-api-key-info)]
           {:db (assoc-in db [:trader :account :api-key-info] api-key-info)
            :dispatch [:trader/log! :trader/account (get api-key-info :uri)
                                                    (get api-key-info :timestamp)]})))

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/request-api-key-info!
  (fn [{:keys [db]} _]
      [:sync/send-query! :trader/synchronize!
                         {:on-failure [:trader/->account-network-error]
                          :on-success [:trader/receive-api-key-info!]
                          :on-sent    [:trader/log! :trader/account "Fetching data from server ..."]
                          :query      [`(:trader/get-api-key-info ~(r get-api-details db))]}]))

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/->account-network-error
  (fn [{:keys [db]} _]
      [:trader/log! :trader/account "Network error"]))
