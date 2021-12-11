
(ns app-extensions.trader.views
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [x.app-layouts.api  :as layouts]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [view-id]}]
  [{:label "Overview" :on-click [:view-selector/go-to! :trader :overview] :active? (= view-id :overview)}
   {:label "Trades"   :on-click [:view-selector/go-to! :trader :trades]   :active? (= view-id :trades)}])



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/menu-bar {:menu-items (menu-items surface-id view-props)}])

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [:div (str view-props)])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [layouts/layout-a surface-id
                    {:body   {:content #'body   :subscriber [:view-selector/get-view-props :trader]}
                     :header {:content #'header :subscriber [:view-selector/get-view-props :trader]}}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :trader/render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! ::view {:view {:content #'view}}])


(a/reg-event-fx
  :trader/load!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n [[:ui/set-header-title! "Trader"]
                [:ui/set-window-title! "Trader"]
                [:trader/render!]]})
