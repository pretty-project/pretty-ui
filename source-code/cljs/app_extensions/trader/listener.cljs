
(ns app-extensions.trader.listener
    (:require [mid-fruits.candy     :refer [param return]]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [app-extensions.trader.engine :as engine]
              [app-extensions.trader.styles :as styles]
              [app-extensions.trader.sync   :as sync]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-listener-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (merge (get-in db [:trader :listener])
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
                    [toggle-listener-button module-id module-props]]
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
  :trader/toggle-listener!
  (fn [{:keys [db]} _]
      {:db (update-in db [:trader :listener :listener-active?] not)
       :dispatch [:sync/send-query! :trader/synchronize!
                                    {:query [:debug `(trader/toggle-listener! ~{})]}]}))
