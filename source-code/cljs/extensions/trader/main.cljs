
(ns extensions.trader.main
    (:require [extensions.trader.account  :as account]
              [extensions.trader.controls :as controls]
              [extensions.trader.listener :as listener]
              [extensions.trader.monitor  :as monitor]
              [extensions.trader.styles   :as styles]
              [extensions.trader.wallet   :as wallet]
              [x.app-core.api             :as a]
              [x.app-elements.api         :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [:div {:style (styles/box-list-style)}
        [monitor/body  surface-id]
        [wallet/body   surface-id]
        [listener/body surface-id]
        [controls/body surface-id]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :trader/load-main!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n [[:ui/set-title! "Trader"]
                [:trader/render-main!]]})

(a/reg-event-fx
  :trader/render-main!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/render-surface! ::view
                       {:content #'view}])
