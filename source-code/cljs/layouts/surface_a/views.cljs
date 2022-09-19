
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns layouts.surface-a.views
    (:require [layouts.surface-a.helpers :as helpers]
              [layouts.surface-a.state   :as state]
              [mid-fruits.css            :as css]
              [react.api                 :as react]
              [reagent.api               :as reagent]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a]
              [x.app-elements.api        :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn title-sensor
  ; @param (map) sensor-props
  ;  {:offset (px)(opt)
  ;   :title (metamorphic-content)}
  ;
  ; @usage
  ;  [surface-a/title-sensor {...}]
  [{:keys [offset] :as sensor-props}]
  (reagent/lifecycles {:component-did-mount    (fn [] (helpers/title-sensor-did-mount-f sensor-props))
                       :component-will-unmount (fn [] (helpers/title-sensor-will-unmount-f))
                       :component-did-update   (fn [this] (let [[sensor-props] (reagent/arguments this)]
                                                               (helpers/title-sensor-did-update-f sensor-props)))
                       :reagent-render         (fn [] [:div#surface-a--title-sensor (if offset {:style {:transform (-> offset css/px css/translate-y)}})])}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header-title
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [react/mount-animation {:mounted? @state/HEADER-TITLE-VISIBLE?}
                         [:div#surface-a--header-title (components/content @state/HEADER-TITLE)]])

(defn- header-shadow
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [react/mount-animation {:mounted? @state/HEADER-SHADOW-VISIBLE?}
                         [:div#surface-a--header-shadow]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- dev-tools-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [debug-mode-detected? @(a/subscribe [:core/debug-mode-detected?])]
          (let [db-write-count @(a/subscribe [:developer/get-db-write-count])]
               [elements/icon-button ::dev-tools-icon-button
                                     {:border-radius :s
                                      :hover-color   :highlight
                                      :icon          :auto_fix_high
                                      :preset        :default
                                      :on-click      [:developer/render-developer-tools!]}])))
                                     ;:keypress      {:key-code 77}
                                     ;:label db-write-count



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/icon-button ::menu-icon-button
                        {:border-radius :s
                         :hover-color   :highlight
                         :on-click      [:views/render-menu-screen!]
                         :preset        :user-menu}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn go-home-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/icon-button ::go-home-icon-button
                        {:border-radius :s
                         :hover-color   :highlight
                         :on-click      [:router/go-home!]
                         :preset        :apps}])
                        ;:badge-color :secondary

(defn go-up-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [parent-route @(a/subscribe [:router/get-current-route-parent])]
       [elements/icon-button ::go-up-icon-button
                             {:border-radius :s
                              :hover-color   :highlight
                              :on-click      [:router/go-to! parent-route]
                              :preset        :back}]))

(defn at-home-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/icon-button ::at-home-icon-button
                        {:preset :apps :disabled? true}])

(defn- navigation-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (cond @(a/subscribe [:router/at-home?])
         [at-home-icon-button]
        @(a/subscribe [:router/home-next-door?])
         [go-home-icon-button]
        :far-from-home
         [go-up-icon-button]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- privacy-policy-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::privacy-policy-button
                   {:color     :muted
                    :font-size :xs
                    :indent    {:vertical :xs}
                    :label     :privacy-policy
                    :on-click  [:router/go-to! "/@app-home/privacy-policy"]}])

(defn- terms-of-service-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::terms-of-service-button
                   {:color     :muted
                    :font-size :xs
                    :indent    {:vertical :xs}
                    :label     :terms-of-service
                    :on-click  [:router/go-to! "/@app-home/terms-of-service"]}])

(defn- settings-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::settings-button
                   {:color     :muted
                    :font-size :xs
                    :indent    {:vertical :xs}
                    :label     :settings
                    :on-click  [:router/go-to! "/@app-home/settings"]}])

(defn- logout-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::logout-button
                   {:color     :muted
                    :font-size :xs
                    :indent    {:vertical :xs}
                    :label     :logout!
                    :on-click  [:user/logout!]}])

(defn- footer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [user-identified? @(a/subscribe [:user/user-identified?])]
          [:div#surface-a--footer [:div#surface-a--footer-content [privacy-policy-button]
                                                                  [terms-of-service-button]
                                                                  [settings-button]
                                                                  [logout-button]]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div#surface-a--header [:div#surface-a--header-content [header-shadow]
                                                          [:div.surface-a--header-block [navigation-icon-button]]
                                                          [:div.surface-a--header-block [header-title]]
                                                          [:div.surface-a--header-block [:<> [dev-tools-icon-button]
                                                                                             [menu-icon-button]]]]])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [user-identified? @(a/subscribe [:user/user-identified?])]
          (reagent/lifecycles {:component-did-mount    (fn [] (helpers/header-did-mount-f))
                               :component-will-unmount (fn [] (helpers/header-will-unmount-f))
                               :reagent-render         (fn [] [header-structure])})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-a
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) layout-props
  ;  {:content (metamorphic-content)}
  [surface-id {:keys [content] :as layout-props}]
  [:div#surface-a (helpers/layout-attributes surface-id layout-props)
                  [:div#surface-a--header-sensor]
                  [:div#surface-a--body [:div#surface-a--body-content [components/content content]]]
                  [header]
                  [footer]])

(defn layout
  ; @param (keyword) surface-id
  ; @param (map) layout-props
  ;  {:content (metamorphic-content)
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [surface-a/layout :my-surface {...}]
  [surface-id layout-props]
  (let [] ;layout-props (prototypes/layout-props-prototype layout-props)
       [surface-a surface-id layout-props]))
