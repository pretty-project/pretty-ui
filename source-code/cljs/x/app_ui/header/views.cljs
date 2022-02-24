
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.header.views
    (:require [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header-apps-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  (let [at-home? @(a/subscribe [:router/at-home?])]
       [elements/icon-button ::apps-icon-button
                             {:preset :apps
                             ;:badge-color (if-not at-home? :secondary)
                              :on-click    [:router/go-home!]
                              :disabled? at-home?}]))

(defn- header-up-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [elements/icon-button ::up-icon-button
                        {:preset :back
                         :on-click [:router/go-up!]}])

(defn- header-back-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [elements/icon-button ::back-icon-button
                        {:preset :back
                         :on-click [:router/go-back!]}])

(defn- header-dev-tools-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  (let [db-write-count @(a/subscribe [:developer/get-db-write-count])]
       [elements/icon-button ::dev-tools-icon-button
                             {:icon   :auto_fix_high
                              :preset :default
                              :on-click [:developer/render-developer-tools!]}]))
                             ;:label db-write-count

(defn- header-menu-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [elements/icon-button ::menu-icon-button
                        {:preset :user-menu
                         :on-click [:views/render-app-menu!]}])

(defn- header-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  (let [header-title @(a/subscribe [:ui/get-header-title])]
       [:div#x-app-header--title (components/content {:content header-title})]))

(defn- header-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  (let [debug-mode?  @(a/subscribe [:core/debug-mode-detected?])
        header-title @(a/subscribe [:ui/get-header-title])
        route-parent @(a/subscribe [:router/get-current-route-parent])]
       [:<> [:div.x-app-header--block (if route-parent     [header-up-icon-button]
                                                           [header-apps-icon-button])]
            [:div.x-app-header--block (if header-title     [header-label])]
            [:div.x-app-header--block (if debug-mode? [:<> [header-dev-tools-icon-button]
                                                           [header-menu-icon-button]]
                                                      [:<> [header-menu-icon-button]])]]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  (if-let [render-header? @(a/subscribe [:ui/render-header?])]
          [:div#x-app-header {:data-nosnippet true}
                             [:div#x-app-header--body [header-label-bar]]]))
