
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.settings.view-selector.views
    (:require [extensions.settings.appearance-settings.views   :rename {body appearance-settings}]
              [extensions.settings.notification-settings.views :rename {body notification-settings}]
              [extensions.settings.personal-settings.views     :rename {body personal-settings}]
              [extensions.settings.privacy-settings.views      :rename {body privacy-settings}]
              [extensions.settings.view-selector.helpers       :as view-selector.helpers]
              [plugins.view-selector.api                       :as view-selector]
              [x.app-core.api                                  :as a]
              [x.app-elements.api                              :as elements]
              [x.app-layouts.api                               :as layouts]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [current-view-id @(a/subscribe [:view-selector/get-current-view-id :settings.view-selector])]
       (case current-view-id :personal      [personal-settings     :settings.view-selector/view]
                             :privacy       [privacy-settings      :settings.view-selector/view]
                             :notifications [notification-settings :settings.view-selector/view]
                             :appearance    [appearance-settings   :settings.view-selector/view]
                                            [personal-settings     :settings.view-selector/view])))

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [view-selector/body :settings.view-selector
                      {:content         #'body-structure
                       :default-view-id :personal}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/menu-bar ::menu-bar
                     {:menu-items (view-selector.helpers/menu-bar-items)
                      :horizontal-align :center}])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [elements/horizontal-polarity ::header
                                {:middle-content [menu-bar]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [layouts/layout-a ::view
                    {:body   #'body
                     :header #'header}])
