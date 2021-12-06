
(ns playground.sample
    (:require [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]
              [x.app-layouts.api  :as layouts]
              [app-plugins.view-selector.api :as view-selector]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sample-header
  [_ {:keys [view-id]}]
  [elements/menu-bar {:menu-items [{:label "Apple"  :on-click [:view-selector/go-to! :sample :apple]  :active? (= view-id :apple)}
                                   {:label "Banana" :on-click [:view-selector/go-to! :sample :banana] :active? (= view-id :banana)}
                                   {:label "Cherry" :on-click [:view-selector/go-to! :sample :cherry] :active? (= view-id :cherry)}]}])

(defn- sample-body
  [_ {:keys [view-id]}]
  [:div (str view-id)])

(defn- view
  [_ view-props]
  [layouts/layout-a {:body   {:content #'sample-body   :content-props view-props}
                     :header {:content #'sample-header :content-props view-props}
                     :description "Description"
                     :label       "My sample page"}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :sample/render!
  [:ui/set-surface! ::view {:content #'view :subscriber [:view-selector/get-view-props :sample]}])

(a/reg-event-fx
  :sample/load!
  {:dispatch-n [[:ui/set-header-title! "Sample"]
                [:ui/set-window-title! "Sample"]
                [:sample/render!]]})
