
(ns app-extensions.trader.controls
    (:require [mid-fruits.candy     :refer [param return]]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [x.app-gestures.api   :as gestures]
              [app-extensions.trader.account  :as account]
              [app-extensions.trader.engine   :as engine]
              [app-extensions.trader.log      :as log]
              [app-extensions.trader.settings :as settings]
              [app-extensions.trader.styles   :as styles]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-controls-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:view-id (r gestures/get-selected-view-id db :trader/controls)})

(a/reg-sub :trader/get-controls-props get-controls-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [view-id]}]
  [elements/menu-bar ::menu-items
                     {:indent :both
                      :menu-items [{:label "Log" :on-click [:gestures/change-view! :trader/controls :log]
                                    :active? (= view-id :log)}
                                   {:label "Positions" :on-click [:gestures/change-view! :trader/controls :positions]
                                    :active? (= view-id :positions)
                                    :badge-color :primary}
                                   {:label "Account" :on-click [:gestures/change-view! :trader/controls :account]
                                    :active? (= view-id :account)}
                                   {:label "Settings" :on-click [:gestures/change-view! :trader/controls :settings]
                                    :active? (= view-id :settings)}]}])

(defn- menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [controls-id {:keys [] :as controls-props}]
  [elements/polarity ::menu-bar
                     {:start-content [menu-items controls-id controls-props]
                      :orientation   :horizontal}])

(defn- controls
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [controls-id {:keys [view-id] :as controls-props}]
  [:div {:style (styles/controls-structure-style)}
        [:div {:style (styles/controls-body-style)}
              [menu-bar controls-id controls-props]
              (case view-id :log       [log/view      controls-id]
                            :positions [log/view      controls-id]
                            :account   [account/view  controls-id]
                            :settings  [settings/view controls-id])]
        [elements/horizontal-separator {:size :xxl}]])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [view-id]
  [components/subscriber view-id
                         {:component  #'controls
                          :subscriber [:trader/get-controls-props]}])
