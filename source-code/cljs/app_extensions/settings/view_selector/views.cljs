
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.settings.view-selector.views
    (:require [x.app-core.api                :as a]
              [x.app-elements.api            :as elements]
              [app-plugins.view-selector.api :as view-selector]
              [app-extensions.settings.appearance-settings.views   :rename {body appearance-settings}]
              [app-extensions.settings.notification-settings.views :rename {body notification-settings}]
              [app-extensions.settings.personal-settings.views     :rename {body personal-settings}]
              [app-extensions.settings.privacy-settings.views      :rename {body privacy-settings}]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [view-id @(a/subscribe [:view-selector/get-selected-view-id :settings])]
       [{:icon :person        :active? (= view-id :personal)      :on-click [:view-selector/go-to! :settings :personal]}
        {:icon :security      :active? (= view-id :privacy)       :on-click [:view-selector/go-to! :settings :privacy]}
        {:icon :notifications :active? (= view-id :notifications) :on-click [:view-selector/go-to! :settings :notifications]}
        {:icon :auto_awesome  :active? (= view-id :appearance)    :on-click [:view-selector/go-to! :settings :appearance]}]))



;; -- Settings body components ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [extension-id]
  (let [view-id @(a/subscribe [:view-selector/get-selected-view-id :settings])]
       (case view-id :personal      [personal-settings     extension-id]
                     :privacy       [privacy-settings      extension-id]
                     :notifications [notification-settings extension-id]
                     :appearance    [appearance-settings   extension-id])))



;; -- Settings header components ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/menu-bar ::menu-bar
                     {:menu-items (menu-bar-items)
                      :horizontal-align :center}])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [elements/horizontal-polarity ::header
                                {:middle-content [menu-bar]}])



;; -- Settings components -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [view-selector/view :settings {:body   #'body
                                 :header #'header}])
