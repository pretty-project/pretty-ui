
; WARNING! EXPIRED! DO NOT USE!



;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.11
; Description:
; Version: v0.3.4
; Compatibility: x3.9.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.home-screen
    (:require [mid-fruits.keyword :as keyword]
              [x.app-core.api     :as a]
              [x.app-elements.api :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- extension-notifications-bar
  [extension-id {:keys [error-count notification-count]}]
  [:div.x-home-screen--card--notifications-bar
   [:div.x-home-screen--card--errors.x-label
    (if (= 0 error-count) {:style {:opacity ".5"}})
    error-count " errors"]
   [:div.x-home-screen--card--notifications.x-label
    (if (= 0 notification-count) {:style {:opacity ".5"}})
    notification-count " notifications"]])

(defn- extension-last-message
  [extension-id {:keys [last-message]}]
  [:div.x-home-screen--card--last-message
   [elements/label {:content last-message
                    :horizontal-align :left}]])

(defn- extension-content
  [extension-id extension-data]
  [:div.x-home-screen--card--content
   [extension-last-message      extension-id extension-data]
   [extension-notifications-bar extension-id extension-data]])

(defn- extension-dnd-sensor
  [extension-id extension-data]
  [elements/icon
   {:layout :touch-target
    :icon   :drag_handle}])

(defn- extension-label
  [extension-id {:keys [icon label]}]
  [:div.x-home-screen--card--label
   [elements/icon {:layout :touch-target
                   :icon   icon}]
   [elements/label {:content label}]])

(defn- extension-header
  [extension-id extension-data]
  [:div.x-home-screen--card--header
   [extension-label      extension-id extension-data]
   [extension-dnd-sensor extension-id extension-data]])

(defn- extension
  ; @param (keyword) extension-id
  ; @param (map) extension-data
  ;  {:error-count (integer)
  ;   :icon (keyword) Material icon class
  ;   :label (metamorphic-content)
  ;   :latest-message (string)
  ;   :notification-count (integer)}
  ;
  ; @return (hiccup)
  [extension-id extension-data]
  [:div.x-home-screen--card.x-box
   {:data-horizontal-align (keyword/to-dom-value :left)}
   [extension-header  extension-id extension-data]
   [extension-content extension-id extension-data]])

(defn- extensions
  []
  [:div {:style {:height         "3000px"}}
   [extension nil {:error-count  3
                   :icon         :settings
                   :label        "Machines"
                   :last-message "\"Mákdaráló\" machine shuts down!"
                   :notification-count 0}]])

(defn- view
  []
  [extensions])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-surface!
   ::view
   {:content  #'view
    :content-label "Home"}])

(a/reg-event-fx
  ::initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-router/set-home! {:route-event [::render!]}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [::initialize!]})
