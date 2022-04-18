
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.header.views
    (:require [mid-fruits.css          :as css]
              [reagent.api             :as reagent]
              [x.app-components.api    :as components]
              [x.app-core.api          :as a]
              [x.app-elements.api      :as elements]
              [x.app-ui.header.helpers :as header.helpers]
              [x.app-ui.header.state   :as header.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn title-sensor
  ; @param (keyword)(opt) sensor-id
  ; @param (map) sensor-props
  ;  {:offset (px)(opt)
  ;   :title (metamorphic-content)}
  ([sensor-props]
   [title-sensor (a/id) sensor-props])

  ([sensor-id {:keys [offset] :as sensor-props}]
   (reagent/lifecycles {:component-did-mount    (fn [] (header.helpers/sensor-did-mount-f    sensor-id sensor-props))
                        :component-will-unmount (fn [] (header.helpers/sensor-will-unmount-f sensor-id))
                        :component-did-update   (fn [this] (let [[_ sensor-props] (reagent/arguments this)]
                                                                (header.helpers/sensor-did-update-f sensor-id sensor-props)))
                        :reagent-render         (fn [] [:div#x-app-header--title-sensor (if offset {:style {:top (css/px offset)}})])})))



;; ----------------------------------------------------------------------------
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
  (let [app-home     @(a/subscribe [:router/get-app-home])
        parent-route @(a/subscribe [:router/get-current-route-parent])]
       (if (= app-home parent-route)
           [header-go-home-icon-button]
           [elements/icon-button ::go-up-icon-button
                                 {:preset :back :on-click [:router/go-to! parent-route]}])))

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
  (if @header.state/HEADER-TITLE [:div#x-app-header--title (components/content @header.state/HEADER-TITLE)]))

(defn- header-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [debug-mode? @(a/subscribe [:core/debug-mode-detected?])]
       [:<> [:div.x-app-header--block [header-navigation-icon-button]]
            [:div.x-app-header--block [header-label]]
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
                  [:div {:style {:position :fixed :bottom 0 :right 0 :z-index 999 :color :pink}}
                        [header-dev-tools-icon-button]])))
