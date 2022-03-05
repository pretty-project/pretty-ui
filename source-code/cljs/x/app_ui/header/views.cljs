
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.header.views
    (:require [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-go-home-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/icon-button ::go-home-icon-button
                        {:preset :apps :on-click [:router/go-home!]}])
                        ;:badge-color :secondary

(defn header-go-up-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [route-parent @(a/subscribe [:ui/get-route-parent])]
          (case route-parent "/@app-home" [header-go-home-icon-button]
                                          [elements/icon-button ::go-up-icon-button
                                                                {:preset :back :on-click [:router/go-to! route-parent]}])))

(defn header-at-home-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/icon-button ::at-home-icon-button
                        {:preset :apps :disabled? true}])

(defn- header-navigation-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [at-home? @(a/subscribe [:router/at-home?])]
          [header-at-home-icon-button]
          [header-go-up-icon-button]))

(defn- header-dev-tools-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [db-write-count @(a/subscribe [:developer/get-db-write-count])]
       [elements/icon-button ::dev-tools-icon-button
                             {:icon   :auto_fix_high
                              :preset :default
                              :on-click [:developer/render-developer-tools!]
                              :keypress {:key-code 77}}]))
                             ;:label db-write-count

(defn- header-menu-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/icon-button ::menu-icon-button
                        {:preset :user-menu
                         :on-click [:views/render-app-menu!]}])

(defn- header-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [header-title @(a/subscribe [:ui/get-header-title])]
       [:div#x-app-header--title (components/content {:content header-title})]))

(defn- header-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [debug-mode?  @(a/subscribe [:core/debug-mode-detected?])
        header-title @(a/subscribe [:ui/get-header-title])]
       [:<> [:div.x-app-header--block [header-navigation-icon-button]]
            [:div.x-app-header--block (if header-title     [header-label])]
            [:div.x-app-header--block (if debug-mode? [:<> [header-dev-tools-icon-button]
                                                           [header-menu-icon-button]]
                                                      [:<> [header-menu-icon-button]])]]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [render-header? @(a/subscribe [:ui/render-header?])]
          [:div#x-app-header {:data-nosnippet true}
                             [:div#x-app-header--body [header-label-bar]]]
          (if-let [debug-mode? @(a/subscribe [:core/debug-mode-detected?])]
                  [:div {:style {:position :fixed :bottom 0 :right 0}}
                        [header-dev-tools-icon-button]])))
