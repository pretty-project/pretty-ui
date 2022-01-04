
(ns app-extensions.trader.main
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [app-extensions.trader.account  :as account]
              [app-extensions.trader.controls :as controls]
              [app-extensions.trader.listener :as listener]
              [app-extensions.trader.monitor  :as monitor]
              [app-extensions.trader.styles   :as styles]
              [app-extensions.trader.wallet   :as wallet]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [:<> [elements/horizontal-separator {:size :xxl}]
       [:div {:style (styles/box-list-style)}
             [monitor/body  surface-id]
             [wallet/body   surface-id]
             [listener/body surface-id]
             [controls/body surface-id]]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :trader/render-main!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! ::view {:view {:content #'view}}])

(a/reg-event-fx
  :trader/load-main!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n [[:ui/listen-to-process! :trader/synchronize!]
                [:gestures/init-view-handler! :trader/controls {:default-view-id :log}]
                [:ui/set-header-title! "Trader"]
                [:ui/set-window-title! "Trader"]
                [:trader/render-main!]]})
