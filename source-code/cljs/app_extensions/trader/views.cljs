
(ns app-extensions.trader.views
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [x.app-layouts.api  :as layouts]
              [app-extensions.trader.overview  :as overview]
              [app-extensions.trader.positions :as positions]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [view-id]}]
  [{:label "Overview"  :on-click [:view-selector/go-to! :trader :overview]  :active? (= view-id :overview)}
   {:label "Positions" :on-click [:view-selector/go-to! :trader :positions] :active? (= view-id :positions)}])



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn monitoring-switch
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id]
  [elements/switch ::monitoring-switch
                   {:label "Monitoring" :initial-value true :indent :both}])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id view-props]
  [elements/polarity {:start-content [elements/menu-bar {:menu-items (menu-items header-id view-props)}]
                      :end-content   [monitoring-switch header-id]}])

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [view-id]}]
  (case view-id :overview  [overview/body  body-id]
                :positions [positions/body body-id]
                "Follow the white rabbit!"))

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [layouts/layout-a surface-id
                    {:body   {:content #'body   :subscriber [:view-selector/get-view-props :trader]}
                     :header {:content #'header :subscriber [:view-selector/get-view-props :trader]}
                     :description "Connected to https://api.bybit.com"}])



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
