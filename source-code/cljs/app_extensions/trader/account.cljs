
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

(defn get-account-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (get-in db [:trader :account]))

(a/reg-sub :trader/get-account-props get-account-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-wallet-balance-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [api-key api-secret]}]
  [elements/button ::get-wallet-balance-button
                   {:preset :primary-button
                    :label "Get wallet balance"
                    :on-click [:trader/request-wallet-balance!]
                    :layout :fit :indent :right
                    :disabled? (not (and (string/nonempty? api-key)
                                         (string/nonempty? api-secret)))}])

(defn- get-api-key-info-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [api-key api-secret]}]
  [elements/button ::get-api-key-info-button
                   {:preset :primary-button
                    :label "Get API key info"
                    :on-click [:trader/request-api-key-info!]
                    :layout :fit :indent :right
                    :disabled? (not (and (string/nonempty? api-key)
                                         (string/nonempty? api-secret)))}])

(defn- api-key-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/text-field ::api-key-field
                       {:label "API key"
                        :min-width :s :indent :right :disable-autofill? false
                        :value-path [:trader :account :api-key]}])

(defn- api-secret-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/text-field ::api-secret-field
                       {:label "API secret"
                        :min-width :s :indent :left
                        :value-path [:trader :account :api-secret]}])

(defn- api-key-expiration-label
  [_ {:keys [api-key-info]}]
  (if-let [expiration-date (get-in api-key-info [:result 0 :expired_at])]
          [elements/label {:content (str "Expiration date: " (time/timestamp-string->date-time expiration-date))
                           :layout :fit :indent :left :color :muted}]))

(defn- account
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [account-id account-props]
  [:div {:style (styles/account-style)}
        [:div {:style {:display :flex}}
              [api-key-field    account-id account-props]
              [api-secret-field account-id account-props]]
        [:div {:style {:display :flex}}
              [get-api-key-info-button  account-id account-props]
              [api-key-expiration-label account-id account-props]]
        [:div {:style {:display :flex}}
              [get-wallet-balance-button account-id account-props]]])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [view-id]
  [components/subscriber view-id
                         {:component  #'account
                          :subscriber [:trader/get-account-props]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :trader/receive-wallet-balance!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ wallet-balance]]
      {:db (assoc-in db [:trader :account :wallet-balance] wallet-balance)
       :dispatch [:trader/log! :trader/account (get wallet-balance :uri)]}))

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/request-wallet-balance!
  (fn [{:keys [db]} _]
      [:sync/send-request! :trader/synchronize!
                           {:method :post
                            :params {:api-key    (get-in db [:trader :account :api-key])
                                     :api-secret (get-in db [:trader :account :api-secret])}
                            :uri "/trader/get-wallet-balance"
                            :on-failure [:trader/->account-network-error]
                            :on-success [:trader/receive-wallet-balance!]
                            :on-sent    [:trader/log! :trader/account "Fetching data from server ..."]}]))

(a/reg-event-fx
  :trader/receive-api-key-info!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ api-key-info]]
      {:db (assoc-in db [:trader :account :api-key-info] api-key-info)
       :dispatch [:trader/log! :trader/account (get api-key-info :uri)]}))

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/request-api-key-info!
  (fn [{:keys [db]} _]
      [:sync/send-request! :trader/synchronize!
                           {:method :post
                            :params {:api-key    (get-in db [:trader :account :api-key])
                                     :api-secret (get-in db [:trader :account :api-secret])}
                            :uri "/trader/get-api-key-info"
                            :on-failure [:trader/->account-network-error]
                            :on-success [:trader/receive-api-key-info!]
                            :on-sent    [:trader/log! :trader/account "Fetching data from server ..."]}]))

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/->account-network-error
  (fn [{:keys [db]} _]
      [:trader/log! :trader/account "Network error"]))
