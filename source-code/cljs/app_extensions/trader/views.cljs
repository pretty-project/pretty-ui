
(ns app-extensions.trader.views
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [x.app-layouts.api  :as layouts]
              [app-extensions.trader.controls :as controls]
              [app-extensions.trader.monitor  :as monitor]
              [app-extensions.trader.styles   :as styles]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [:<> [elements/horizontal-separator {:size :xxl}]
       [:div {:style (styles/box-list-style)}
             [monitor/view  surface-id]
             [controls/view surface-id]]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :trader/render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! ::view {:view {:content #'view}}])

(a/reg-event-fx
  :trader/load!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n [[:ui/listen-to-process! :trader/synchronize!]
                [:gestures/init-view-handler! :trader/controls {:default-view-id :log}]
                [:ui/set-header-title! "Trader"]
                [:ui/set-window-title! "Trader"]
                [:trader/render!]]})
