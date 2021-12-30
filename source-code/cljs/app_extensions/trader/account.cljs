
(ns app-extensions.trader.account
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.string    :as string]
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

(defn- api-key-test-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [api-key]}]
  [elements/button ::api-key-test-button
                   {:preset :primary-button
                    :label "Get API key info"
                    :on-click [:trader/request-api-key-info!]
                    :layout :fit
                    :disabled? (not (string/nonempty? api-key))}])

(defn- api-key-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/text-field ::api-key
                       {:label "API key"
                        :min-width :s
                        :value-path [:trader :account :api-key]}])

(defn- account
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [account-id account-props]
  [:div {:style (styles/account-style)}
        [:div {:style {:display :flex}}
              [api-key-field account-id account-props]]
        [api-key-test-button account-id account-props]])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [view-id]
  [components/subscriber view-id
                         {:component  #'account
                          :subscriber [:trader/get-account-props]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/request-api-key-info!
  (fn [{:keys [db]} _]
      [:sync/send-request! :trader/synchronize!
                           {:method :post
                            :params {:api-key (get-in db [:trader :account :api-key])}
                            :uri "/trader/get-api-key-info"
                            :on-failure [:trader/->account-network-error]}]))

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/->account-network-error
  (fn [{:keys [db]} _]
      [:trader/log! :trader/account "Network error"]))
