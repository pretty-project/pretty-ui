
(ns extensions.trader.listener
    (:require [extensions.trader.engine :as engine]
              [extensions.trader.styles :as styles]
              [extensions.trader.sync   :as sync]
              [mid-fruits.candy         :refer [param return]]
              [x.app-components.api     :as components]
              [x.app-core.api           :as a :refer [r]]
              [x.app-elements.api       :as elements]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-listener-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (merge (get-in db [:trader :listener])
         (r sync/get-response db :trader/download-listener-data)
         ; XXX#4066
         {:responsed?  (r sync/synchronized? db)
          :subscribed? true}))


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

(defn- toggle-listener-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id {:keys [listener-active?]}]
  [elements/button ::toggle-listener-button
                   {:icon :power_settings_new :layout :icon-button :indent :right
                    :preset  (if listener-active? :warning-icon-button :muted-icon-button)
                    :tooltip (if listener-active? "Stop listener" "Start listener")
                    :on-click [:trader/toggle-listener!]}])


(defn- listener
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props]
  [:div {:style (styles/box-structure-style)}
        [:div {:style (styles/box-body-style)}
              [:div {:style (styles/overlay-center-style)}
                    [source-code-preview module-id module-props]
                    ; TEMP
                    [elements/button {:label "Set margin"
                                      :on-click [:trader/set-margin!]}]]

              [:div {:style (styles/box-tc-controls-style)}
                    [elements/label {:content "Danger zone" :color :secondary :font-weight :extra-bold :font-size :l}]]
              [:div {:style (styles/box-tr-controls-style)}
                    [toggle-listener-button module-id module-props]]
              [sync/synchronizing-label module-id module-props]]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [components/subscriber :trader/listener
                         {:render-f   #'listener
                          :subscriber [:trader/get-listener-props]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/toggle-listener!
  (fn [{:keys [db]} _]
      {:db       (update-in db [:trader :listener :listener-active?] not)
       :dispatch [:sync/send-query! :trader/synchronize!
                                    {:display-progress? true
                                     :query [:debug `(trader/toggle-listener! ~{})]}]}))

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/connect-to-listener!
  (fn [{:keys [db]} _]
      [:trader/subscribe-to-query! :trader/listener
                                   {:query [`(:trader/download-listener-data ~{})]}]))

(a/reg-event-fx
  :trader/set-margin!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      [:sync/send-query! :trader/synchronize!
                         {:display-progress? true
                          :query [:debug `(trader/upload-margin-data! ~{})]}]))
