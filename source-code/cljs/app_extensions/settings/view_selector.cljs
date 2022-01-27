
(ns app-extensions.settings.view-selector
    (:require [mid-fruits.candy     :refer [param]]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [x.app-ui.api         :as ui]
              [app-plugins.view-selector.api                 :as view-selector]
              [app-extensions.settings.appearance-settings   :rename {body appearance-settings}]
              [app-extensions.settings.notification-settings :rename {body notification-settings}]
              [app-extensions.settings.personal-settings     :rename {body personal-settings}]
              [app-extensions.settings.privacy-settings      :rename {body privacy-settings}]))



;; -- Settings body components ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [view-id] :as body-props}]
  (case view-id :personal      [personal-settings     body-id body-props]
                :privacy       [privacy-settings      body-id body-props]
                :notifications [notification-settings body-id body-props]
                :appearance    [appearance-settings   body-id body-props]
                [:div (str body-props)]))



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
  [elements/horizontal-polarity ::header
                                {:middle-content [menu-bar header-id header-props]}])



;; -- Settings components -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [view-selector/view :settings {:body   #'body
                                 :header #'header}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :settings.view-selector/render-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! :settings.view-selector/view
                    {:view #'view}])

(a/reg-event-fx
  :settings.view-selector/load-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if-not (r ui/element-rendered? db :surface :settings.view-selector/view)
              [:settings.view-selector/render-selector!])))
