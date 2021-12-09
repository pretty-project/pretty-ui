
(ns extensions.settings.views
    (:require [mid-fruits.candy     :refer [param]]
              [x.app-activities.api :as activities]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [x.app-layouts.api    :as layouts]
              [x.app-user.api       :as user]
              [app-plugins.view-selector.api             :as view-selector]
              [extensions.settings.appearance-settings   :rename {body appearance-settings}]
              [extensions.settings.notification-settings :rename {body notification-settings}]
              [extensions.settings.personal-settings     :rename {body personal-settings}]
              [extensions.settings.privacy-settings      :rename {body privacy-settings}]))



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

(defn personal-settings-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id {:keys [view-id]}]
  [elements/button ::personal-settings-button
                   {:color    (if (not= view-id :personal) :muted)
                    :icon     :person
                    :on-click [:view-selector/go-to! :settings :personal]
                    :preset   :default-icon-button}])

(defn privacy-settings-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id {:keys [view-id]}]
  [elements/button ::privacy-settings-button
                   {:color    (if (not= view-id :privacy) :muted)
                    :icon     :security
                    :on-click [:view-selector/go-to! :settings :privacy]
                    :preset   :default-icon-button}])

(defn notification-settings-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id {:keys [view-id]}]
  [elements/button ::notification-settings-button
                   {:color    (if (not= view-id :notifications) :muted)
                    :icon     :notifications
                    :on-click [:view-selector/go-to! :settings :notifications]
                    :preset   :default-icon-button}])

(defn appearance-settings-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id {:keys [view-id]}]
  [elements/button ::appearance-settings-button
                   {:color    (if (not= view-id :appearance) :muted)
                    :icon     :auto_awesome
                    :on-click [:view-selector/go-to! :settings :appearance]
                    :preset   :default-icon-button}])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id header-props]
  [elements/polarity ::header
                     {:middle-content [:<> [personal-settings-button     header-id header-props]
                                           [privacy-settings-button      header-id header-props]
                                           [notification-settings-button header-id header-props]
                                           [appearance-settings-button   header-id header-props]]}])



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
