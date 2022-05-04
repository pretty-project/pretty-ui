
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
  [{:keys [offset] :as sensor-props}]
  (reagent/lifecycles {:component-did-mount    (fn [] (helpers/title-sensor-did-mount-f sensor-props))
                       :component-will-unmount (fn [] (helpers/title-sensor-will-unmount-f))
                       :component-did-update   (fn [this] (let [[sensor-props] (reagent/arguments this)]
                                                               (helpers/title-sensor-did-update-f sensor-props)))
                       :reagent-render         (fn [] [:div#surface-a--title-sensor (if offset {:style {:top (css/px offset)}})])}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header-title
  []
  [react/mount-animation {:mounted? @state/HEADER-TITLE-VISIBLE?}
                         [:div#surface-a--header-title (components/content @state/HEADER-TITLE)]])

(defn- header-shadow
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
                                     {:icon   :auto_fix_high
                                      :preset :default
                                      :on-click [:developer/render-developer-tools!]
                                      :keypress {:key-code 77}}])))
                                     ;:label db-write-count



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/icon-button ::menu-icon-button
                        {:preset :user-menu
                         :on-click [:views/render-app-menu!]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn go-home-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/icon-button ::go-home-icon-button
                        {:preset :apps :on-click [:router/go-home!]}])
                        ;:badge-color :secondary

(defn go-up-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [parent-route @(a/subscribe [:router/get-current-route-parent])]
       [elements/icon-button ::go-up-icon-button
                             {:preset :back :on-click [:router/go-to! parent-route]}]))

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

(defn- header-structure
  []
  [:div#surface-a--header [:div#surface-a--header-content [header-shadow]
                                                          [:div.surface-a--header-block [navigation-icon-button]]
                                                          [:div.surface-a--header-block [header-title]]
                                                          [:div.surface-a--header-block [:<> [dev-tools-icon-button]
                                                                                             [menu-icon-button]]]]])

(defn- header
  []
  (reagent/lifecycles {:component-did-mount    (fn [] (helpers/header-did-mount-f))
                       :component-will-unmount (fn [] (helpers/header-will-unmount-f))
                       :reagent-render         (fn [] [header-structure])}))

(defn surface-a
  [surface-id {:keys [content] :as layout-props}]
  [:div#surface-a [:div#surface-a--header-sensor]
                  [:div#surface-a--body [:div#surface-a--body-content content]]
                  [header]])

(defn layout
  [surface-id layout-props]
  (let [] ;layout-props (prototypes/layout-props-prototype layout-props)
       [surface-a surface-id layout-props]))
