
(ns app-extensions.trader.account
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.map       :refer [dissoc-in]]
              [mid-fruits.string    :as string]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [app-extensions.trader.styles :as styles]
              [app-extensions.trader.sync   :as sync]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn api-details-filled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [api-key    (get-in db [:trader :account :api-key])
        api-secret (get-in db [:trader :account :api-secret])]
       (and (string/nonempty? api-key)
            (string/nonempty? api-secret))))

(defn use-mainnet?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (get-in db [:trader :account :use-mainnet?]))

(defn- get-api-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:api-key      (get-in db [:trader :account :api-key])
   :api-secret   (get-in db [:trader :account :api-secret])
   :use-mainnet? (get-in db [:trader :account :use-mainnet?])})

(defn get-account-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (merge (get-in db [:trader :account])
         (r sync/get-response db :trader/get-account-data)
         {:api-details-filled? (r api-details-filled? db)
          :responsed?          (r sync/responsed?     db :trader/get-account-data)}))

(a/reg-sub :trader/get-account-props get-account-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- invalid-api-details-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:div {:style (styles/overlay-center-style)}
        [elements/label {:content "Invalid api details" :color :warning}]])

(defn- missing-api-details-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:div {:style (styles/overlay-center-style)}
        [elements/label {:content "Missing api details" :color :highlight}]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- use-mainnet-switch
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/switch ::use-mainnet-switch
                   {:label "api.bybit.com" :secondary-label "testnet-api.bybit.com"
                    :font-size :xs :indent :both :initial-value false :border-color :default
                    :value-path [:trader :account :use-mainnet?]}])

(defn- api-key-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/text-field ::api-key-field
                       {:label "API key"
                        :min-width :s :indent :both :disable-autofill? false :name :favorite-color
                        :value-path [:trader :account :api-key]}])

(defn- api-secret-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/text-field ::api-secret-field
                       {:label "API secret"
                        :placeholder "**************"
                        :min-width :s :indent :both
                        :value-path [:trader :account :api-secret]}])

(defn- save-api-details-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [api-details-filled?] :as module-props}]
  [elements/button ::save-account-button
                   {:label "Save"
                    :indent :right :disabled? (not api-details-filled?)
                    :on-click [:trader/upload-api-details!]}])

(defn- api-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id {:keys [failured?] :as module-props}]
  [:div {:style (styles/overlay-center-style)}
        [:div [:div {:style (styles/row-style {:justify-content "center"})}
                    [api-key-field    module-id module-props]
                    [api-secret-field module-id module-props]]
              [:div {:style (styles/row-style {:justify-content "flex-start"})}
                    [use-mainnet-switch module-id module-props]]
              [:div {:style (styles/row-style {:justify-content "center"})}
                    [elements/label {:content (if failured? "Account error") :color :warning :font-size :s}]]
              [:div {:style (styles/row-style {:justify-content "flex-end"})}
                    [save-api-details-button module-id module-props]]]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- account
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props]
  [api-details module-id module-props])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [components/subscriber :trader/account
                         {:render-f    #'account
                          :subscriber [:trader/get-account-props]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/connect-to-account!
  (fn [{:keys [db]} _]
      [:trader/subscribe-to-query! :trader/account
                                   {:query [`(:trader/download-account-data ~{})]}]))

(a/reg-event-fx
  :trader/upload-api-details!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      [:sync/send-query! :trader/synchronize!
                         {:query [:debug `(trader/upload-api-details! ~(r get-api-details db))]}]))
