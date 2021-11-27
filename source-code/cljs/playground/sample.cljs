
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
  :sample/render!
  [:ui/set-surface! ::view {:content #'view}])

(a/reg-event-fx
  :sample/load!
  {:dispatch-n [[:ui/set-header-title! "Sample"]
                [:ui/set-window-title! "Sample"]
                [:sample/render!]]})
