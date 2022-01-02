
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

(defn- menu-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [view-id]}]
  [{:label "Log" :on-click [:gestures/change-view! :trader/controls :log]
    :active? (= view-id :log)}
   {:label "Positions" :on-click [:gestures/change-view! :trader/controls :positions]
    :active? (= view-id :positions)
    :badge-color :primary}
   {:label "Account" :on-click [:gestures/change-view! :trader/controls :account]
    :active? (= view-id :account)}
   {:label "Settings" :on-click [:gestures/change-view! :trader/controls :settings]
    :active? (= view-id :settings)}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-controls-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:use-mainnet? (r account/use-mainnet?          db)
   :view-id      (r gestures/get-selected-view-id db :trader/controls)})

(a/reg-sub :trader/get-controls-props get-controls-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [controls-id controls-props]
  [elements/menu-bar ::menu-bar
                     {:indent :both :menu-items (menu-items controls-id controls-props)}])

(defn- menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [controls-id controls-props]
  [elements/polarity {:start-content [menu-bar-menu controls-id controls-props]
                      :end-content   [account/mainnet-indicator controls-id controls-props]}])

(defn- controls
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [controls-id {:keys [view-id] :as controls-props}]
  [:div {:style (styles/box-structure-style)}
        [:div {:style (styles/box-body-style)}
              [menu-bar controls-id controls-props]
              (case view-id :log       [log/view      controls-id]
                            :positions [settings/view controls-id]
                            :account   [account/view  controls-id]
                            :settings  [settings/view controls-id])]
        [elements/horizontal-separator {:size :xxl}]])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [components/subscriber body-id
                         {:component  #'controls
                          :subscriber [:trader/get-controls-props]}])
