
(ns app-extensions.settings.views
    (:require [mid-fruits.candy     :refer [param]]
              [x.app-activities.api :as activities]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [x.app-layouts.api    :as layouts]
              [x.app-user.api       :as user]
              [app-plugins.view-selector.api             :as view-selector]
              [app-extensions.settings.appearance-settings   :rename {body appearance-settings}]
              [app-extensions.settings.notification-settings :rename {body notification-settings}]
              [app-extensions.settings.personal-settings     :rename {body personal-settings}]
              [app-extensions.settings.privacy-settings      :rename {body privacy-settings}]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-description
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [registered-at (r user/get-user-profile-item      db :registered-at)
        registered-at (r activities/get-actual-timestamp db  registered-at)]
       (components/content {:content :registered-at-n :replacements [registered-at]})))

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:description (r get-description    db)
   :label       (r user/get-user-name db)})

(a/reg-sub ::get-view-props get-view-props)



;; -- Settings body components ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [view-id] :as body-props}]
  (case view-id :personal      [personal-settings     body-id body-props]
                :privacy       [privacy-settings      body-id body-props]
                :notifications [notification-settings body-id body-props]
                :appearance    [appearance-settings   body-id body-props]))



;; -- Settings header components ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [view-id]}]
  [{:icon :person        :active? (= view-id :personal)      :on-click [:view-selector/go-to! :settings :personal]}
   {:icon :security      :active? (= view-id :privacy)       :on-click [:view-selector/go-to! :settings :privacy]}
   {:icon :notifications :active? (= view-id :notifications) :on-click [:view-selector/go-to! :settings :notifications]}
   {:icon :auto_awesome  :active? (= view-id :appearance)    :on-click [:view-selector/go-to! :settings :appearance]}])

(defn- menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id header-props]
  [elements/menu-bar ::menu-bar
                     {:menu-items (menu-bar-items header-id header-props)
                      :horizontal-align :center}])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id header-props]
  [elements/polarity ::header
                     {:middle-content [menu-bar header-id header-props]}])



;; -- Settings components -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [description label]}]
  [layouts/layout-a surface-id {:body   {:content #'body   :subscriber [:view-selector/get-view-props :settings]}
                                :header {:content #'header :subscriber [:view-selector/get-view-props :settings]}
                                :min-width :m
                                :description description
                                :label       label}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :settings/render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! ::view {:view {:content #'view :subscriber [::get-view-props]}}])

(a/reg-event-fx
  :settings/load!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n [[:ui/listen-to-process! :settings/synchronize!]
                [:ui/set-window-title!  :settings]
                [:ui/set-header-title!  :settings]
                [:settings/render!]]})
