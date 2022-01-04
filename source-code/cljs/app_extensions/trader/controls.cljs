
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
              [app-extensions.trader.styles   :as styles]
              [app-extensions.trader.sync     :as sync]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [view-id]}]
  [{:label "Log" :on-click [:gestures/change-view! :trader/controls :log]
    :active? (= view-id :log)}
   {:label "Account" :on-click [:gestures/change-view! :trader/controls :account]
    :active? (= view-id :account)}
   {:label "Settings" :on-click [:gestures/change-view! :trader/controls :settings]
    :active? (= view-id :settings)}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-controls-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  ; - A controls modul nem rendelkezik önálló subscription feliratkozással, amit
  ;   lehetne vizsgálni
  ; - A [sync/synchronizing-label ...] komponens működéséhez szükséges tulajdonságok:
  ;   {:responsed? ...}, {:subscribed? ...}
  {:responsed?  (r sync/synchronized? db)
   :subscribed? true
   :view-id     (r gestures/get-selected-view-id db :trader/controls)})

(a/reg-sub :trader/get-controls-props get-controls-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props]
  [elements/menu-bar ::menu-bar
                     {:indent :both :menu-items (menu-items module-id module-props)}])

(defn- menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props]
  [elements/polarity {:start-content [menu-bar-menu module-id module-props]}])

(defn- controls
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id {:keys [view-id] :as module-props}]
  [:div {:style (styles/box-structure-style)}
        [:div {:style (styles/box-body-style)}
              [menu-bar module-id module-props]
              (case view-id :log      [log/body      module-id]
                            :account  [account/body  module-id]
                            :settings [settings/body module-id])
              [sync/synchronizing-label module-id module-props]]
        [elements/horizontal-separator {:size :xxl}]])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [components/subscriber :trader/controls
                         {:component  #'controls
                          :subscriber [:trader/get-controls-props]}])
