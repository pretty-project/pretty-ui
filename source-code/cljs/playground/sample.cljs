
(ns playground.sample
    (:require [x.app-core.api      :as a :refer [r]]
              [x.app-elements.api  :as elements]
              [x.app-layouts.api   :as layouts]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sample-header
  [_]
  [elements/menu-bar {:menu-items [{:label "Apple"  :on-click [] :color :default}
                                   {:label "Banana" :on-click [] :color :muted}
                                   {:label "Cherry" :on-click [] :color :muted}]}])

(defn- sample-body
  [_]
  [:div "Sample body"])

(defn- view
  [_]
  [layouts/layout-a {:body   {:content #'sample-body}
                     :header {:content #'sample-header}
                     :description "Description"
                     :label       "My sample page"}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::render!
  [:x.app-ui/set-surface! ::view {:content #'view}])

(a/reg-event-fx
  ::load!
  {:dispatch-n [[:x.app-ui/set-header-title! "Sample"]
                [:x.app-ui/set-window-title! "Sample"]
                [::render!]]})

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:x.app-router/add-route! ::route
                                          {:route-event    [::load!]
                                           :route-template "/sample"
                                           :restricted?    true}]})
