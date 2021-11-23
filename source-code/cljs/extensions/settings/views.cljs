
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



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keywords in vector)
(def ALLOWED-VIEWS [:personal :privacy :appearance :notifications])

; @constant (keywords)
(def DEFAULT-VIEW :personal)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (r view-selector/get-body-props db :settings))

(a/reg-sub ::get-body-props get-body-props)

(defn- get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (r view-selector/get-header-props db :settings))

(a/reg-sub ::get-header-props get-header-props)

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
  [body-id {:keys [selected-view] :as body-props}]
  (case selected-view :personal      [personal-settings     body-id body-props]
                      :privacy       [privacy-settings      body-id body-props]
                      :notifications [notification-settings body-id body-props]
                      :appearance    [appearance-settings   body-id body-props]))



;; -- Settings header components ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn personal-settings-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id {:keys [selected-view]}]
  [elements/button ::personal-settings-button
                   {:color    (if (= selected-view :personal) :default :muted)
                    :icon     :person
                    :on-click [:view-selector/change-view! :settings :personal]
                    :preset   :default-icon-button}])

(defn privacy-settings-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id {:keys [selected-view]}]
  [elements/button ::privacy-settings-button
                   {:color    (if (= selected-view :privacy) :default :muted)
                    :icon     :security
                    :on-click [:view-selector/change-view! :settings :privacy]
                    :preset   :default-icon-button}])

(defn notification-settings-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id {:keys [selected-view]}]
  [elements/button ::notification-settings-button
                   {:color    (if (= selected-view :notifications) :default :muted)
                    :icon     :notifications
                    :on-click [:view-selector/change-view! :settings :notifications]
                    :preset   :default-icon-button}])

(defn appearance-settings-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id {:keys [selected-view]}]
  [elements/button ::appearance-settings-button
                   {:color    (if (= selected-view :appearance) :default :muted)
                    :icon     :auto_awesome
                    :on-click [:view-selector/change-view! :settings :appearance]
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
  [layouts/layout-a surface-id {:body   {:content    #'body
                                         :subscriber [::get-body-props]}
                                :header {:content    #'header
                                         :subscriber [::get-header-props]
                                         :sticky?    true}
                                :min-width :m
                                :description description
                                :label       label}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :settings/render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-surface! ::view
                          {:content    #'view
                           :subscriber [::get-view-props]}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:view-selector/initialize! :settings {:allowed-views ALLOWED-VIEWS
                                                       :default-view  DEFAULT-VIEW}]})
