
(ns website.navbar.views
    (:require [site.website-menus.frontend.api]
              [elements.api                :as elements]
              [random.api                  :as random]
              [re-frame.api                :as r]
              [website.menu.views          :as menu.views]
              [website.navbar.helpers      :as navbar.helpers]
              [website.scroll-sensor.views :as scroll-sensor.views]
              [x.components.api            :as x.components]
              [x.environment.api           :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- navbar-menu-button
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; {:on-menu (metamorphic-event)(opt)}
  [component-id {:keys [on-menu]}]
  (if on-menu [:button {:id :mt-navbar--menu-button
                        :data-click-effect :opacity}
                        :on-click    #(r/dispatch on-menu)
                        :on-mouse-up #(x.environment/blur-element!)
                       [:span]]))

(defn- navbar-menu-items
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; {:menu-link (namespaced map)
  [component-id {:keys [menu-link]}]
  [menu.views/component component-id {:menu-link menu-link}])

(defn- navbar-logo
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; {:logo (metamorphic-content)(opt)}
  [component-id {:keys [logo]}]
  (if logo [:div {:id :mt-navbar--logo}
                 [x.components/content component-id logo]]
           [:div {:class :mt-navbar--logo-placeholder}]))

(defn- navbar
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :style (map)(opt)
  ;  :threshold (px)(opt)}
  [component-id {:keys [class style threshold] :as component-props}]
  (let [threshold? (<= threshold @(r/subscribe [:x.environment/get-viewport-width]))]
       [:<> [scroll-sensor.views/component ::scroll-sensor {:callback-f navbar.helpers/scroll-f
                                                            :style {:left "0" :position "absolute" :top "0"}}]
            [:div {:id :mt-navbar :class class :style style
                   :data-profile (if threshold? :desktop :mobile)}
                  [navbar-logo component-id component-props]
                  (if threshold? [navbar-menu-items  component-id component-props]
                                 [navbar-menu-button component-id component-props])]]))

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) component-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :logo (metamorphic-content)(opt)
  ;  :menu-link (namespaced map)
  ;   {:menu/id (string)}
  ;  :on-menu (metamorphic-event)(opt)
  ;   Click event on the hamburger menu button
  ;  :style (map)(opt)
  ;  :threshold (px)(opt)
  ;   Threshold of the desktop view}
  ;
  ; @usage
  ; [navbar {...}]
  ;
  ; @usage
  ; [navbar :my-navbar {...}]
  ([component-props]
   [component (random/generate-keyword) component-props])

  ([component-id component-props]
   [navbar component-id component-props]))