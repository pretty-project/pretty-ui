
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns layouts.surface-a.views
    (:require [css.api                   :as css]
              [elements.api              :as elements]
              [layouts.surface-a.helpers :as helpers]
              [layouts.surface-a.state   :as state]
              [react.api                 :as react]
              [reagent.api               :as reagent]
              [re-frame.api              :as r]
              [x.app-components.api      :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn background
  [_ _]
  [:div#surface-a--background [:svg {:style {:width "100%" :height "100%"}
                                     :preserve-aspect-ratio "none"
                                     :view-box              "0 0 100 100"}
                                    [:polygon.surface-a--background-pattern {:points "0,10 0,30 5,75 0,100 10,100 10,70 0,10 5,0 0,0"}]
                                    [:polygon.surface-a--background-pattern {:points "100,90 100,50 95,80 90,100 95,100"}]
                                    [:polygon.surface-a--background-pattern {:points "100,0 100,10 95,5 80,0"}]]])



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
  (reagent/lifecycles {:component-did-mount    (fn []  (helpers/title-sensor-did-mount-f sensor-props))
                       :component-will-unmount (fn []  (helpers/title-sensor-will-unmount-f))
                       :component-did-update   (fn [%] (helpers/title-sensor-did-update-f %))
                       :reagent-render         (fn []  [:div#surface-a--title-sensor (if offset {:style {:transform (-> offset css/px css/translate-y)}})])}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header-title
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [react/mount-animation {:mounted? @state/HEADER-TITLE-VISIBLE?}
                         [:div#surface-a--header-title (x.components/content @state/HEADER-TITLE)]])

(defn- header-shadow
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [react/mount-animation {:mounted? @state/HEADER-SHADOW-VISIBLE?}
                         [:div#surface-a--header-shadow]])



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
  (let [parent-route @(r/subscribe [:router/get-current-route-parent])]
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
  (cond @(r/subscribe [:router/at-home?])
         [at-home-icon-button]
        @(r/subscribe [:router/home-next-door?])
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

(defn- user-profile-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::settings-button
                   {:color     :muted
                    :font-size :xs
                    :indent    {:vertical :xs}
                    :label     :user-profile
                    :on-click  [:router/go-to! "/@app-home/user-profile"]}])

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
  [_ _]
  (if-let [user-identified? @(r/subscribe [:user/user-identified?])]
          [:div#surface-a--footer [:div#surface-a--footer-content [privacy-policy-button]
                                                                  [terms-of-service-button]
                                                                  [user-profile-button]
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
                                                          [:div.surface-a--header-block [menu-icon-button]]]])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  (if-let [user-identified? @(r/subscribe [:user/user-identified?])]
          (reagent/lifecycles {:component-did-mount    (fn [] (helpers/header-did-mount-f))
                               :component-will-unmount (fn [] (helpers/header-will-unmount-f))
                               :reagent-render         (fn [] [header-structure])})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) layout-props
  ;  {:content (metamorphic-content)}
  [surface-id {:keys [content] :as layout-props}]
  [:div#surface-a--body [:div#surface-a--body-content [x.components/content surface-id content]]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-a
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) layout-props
  [surface-id layout-props]
  [:div#surface-a (helpers/layout-attributes surface-id layout-props)
                  [background                surface-id layout-props]
                  [footer                    surface-id layout-props]
                  [body                      surface-id layout-props]
                  [header                    surface-id layout-props]
                  [:div#surface-a--header-sensor]])

(defn layout
  ; @param (keyword) surface-id
  ; @param (map) layout-props
  ;  {:content (metamorphic-content)
  ;   :on-mount (metamorphic-event)(opt)
  ;   :on-unmount (metamorphic-event)(opt)
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [layout :my-surface {...}]
  [surface-id {:keys [on-mount on-unmount] :as layout-props}]
  (let [] ;layout-props (prototypes/layout-props-prototype layout-props)
       (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch on-mount))
                            :component-will-unmount (fn [_ _] (r/dispatch on-unmount))
                            :reagent-render         (fn [_ _] [surface-a surface-id layout-props])})))
