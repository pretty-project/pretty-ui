
(ns app-extensions.trader.listener
    (:require [mid-fruits.candy     :refer [param return]]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [app-extensions.trader.styles :as styles]
              [app-extensions.trader.sync   :as sync]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-listener-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (merge (get-in db [:trader :listener])
         (r sync/get-response db :trader/get-listener-data)
         {:responsed?  (r sync/responsed?  db :trader/get-listener-data)
          :subscribed? (r sync/subscribed? db :trader/listener)}))

(a/reg-sub :trader/get-listener-props get-listener-props)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- source-code-preview
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [source-code]}]
  [:button {:style     (styles/source-code-preview-style)
            :on-click #(a/dispatch [:trader/edit-source-code!])}
           (str source-code)
           [:div {:style (styles/source-code-preview-icon-style)}
                 [elements/icon {:icon :code}]]])

(defn- listener-state-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id {:keys [listener-active?]}]
  [elements/button ::listener-state-button
                   {:icon :power_settings_new :layout :icon-button :indent :right
                    :preset (if listener-active? :warning-icon-button :muted-icon-button)
                    :on-click [:trader/toggle-listener!]}])

(defn- listener
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props]
  [:div {:style (styles/box-structure-style)}
        [:div {:style (styles/box-body-style)}
              [:div {:style (styles/overlay-center-style)}
                    [source-code-preview module-id module-props]]
              [:div {:style (styles/box-tc-controls-style)}
                    [elements/label {:content "Danger zone" :color :secondary :font-weight :extra-bold :font-size :l}]]
              [:div {:style (styles/box-tr-controls-style)}
                    [listener-state-button module-id module-props]]
              [sync/synchronizing-label module-id module-props]]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [components/subscriber :trader/listener
                         {:component  #'listener
                          :subscriber [:trader/get-listener-props]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/connect-to-listener!
  [:trader/add-subscription! :trader/listener
                             {:query [`(:trader/get-listener-data ~{})]}])

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/toggle-listener!
  [:sync/send-query! :trader/synchronize!
                     {:query [:debug `(:trader/toggle-listener! ~{})]}])
