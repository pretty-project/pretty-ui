
(ns extensions.settings.views
    (:require [mid-fruits.candy          :refer [param]]
              [plugins.view-selector.api :as view-selector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.api        :as elements]
              [extensions.settings.appearance-settings   :rename {view appearance-settings}]
              [extensions.settings.notification-settings :rename {view notification-settings}]
              [extensions.settings.personal-settings     :rename {view personal-settings}]
              [extensions.settings.privacy-settings      :rename {view privacy-settings}]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
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
                    :on-click [:settings/change-view! :personal]
                    :preset   :default-icon-button}])

(defn privacy-settings-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id {:keys [selected-view]}]
  [elements/button ::privacy-settings-button
                   {:color    (if (= selected-view :privacy) :default :muted)
                    :icon     :security
                    :on-click [:settings/change-view! :privacy]
                    :preset   :default-icon-button}])

(defn notification-settings-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id {:keys [selected-view]}]
  [elements/button ::notification-settings-button
                   {:color    (if (= selected-view :notifications) :default :muted)
                    :icon     :notifications
                    :on-click [:settings/change-view! :notifications]
                    :preset   :default-icon-button}])

(defn appearance-settings-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id {:keys [selected-view]}]
  [elements/button ::appearance-settings-button
                   {:color    (if (= selected-view :appearance) :default :muted)
                    :icon     :auto_awesome
                    :on-click [:settings/change-view! :appearance]
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
  [surface-id]
  [elements/box surface-id {:body   {:content    #'body
                                     :subscriber [::get-body-props]}
                            :header {:content    #'header
                                     :subscriber [::get-header-props]
                                     :sticky?    true}
                            :horizontal-align :left}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :settings/render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-surface! ::view
                          {:content #'view
                           :content-label :settings}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:view-selector/add-routes! :settings {:default-view DEFAULT-VIEW}]})
