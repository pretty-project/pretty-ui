
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.20
; Description:
; Version: v0.8.2
; Compatibility: x4.2.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.header
    (:require [mid-fruits.candy      :refer [param]]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-db.api          :as db]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]
              [x.app-router.api      :as router]
              [x.app-ui.interface    :as interface]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-header-title
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (metamorphic-content)
  [db _]
  (get-in db (db/path ::primary :header-title)))

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:at-home? (boolean)
  ;   :header-title (metamorphic-content)
  ;   :parent-path (string)
  ;   :render-header? (boolean)
  ;   :viewport-small? (boolean)}
  [db _]
  {:at-home?        (r router/at-home?                  db)
   :debug-mode?     (r a/debug-mode?                    db)
   :header-title    (r get-header-title                 db)
   :parent-path     (r router/get-current-route-parent  db)
   :render-header?  (r interface/application-interface? db)
   :viewport-small? (r environment/viewport-small?      db)})

(a/reg-sub ::get-view-props get-view-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-header-title!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (metamorphic-content) title
  ;
  ; @return (map)
  [db [_ header-title]]
  (assoc-in db (db/path ::primary :header-title) header-title))

(a/reg-event-db :x.app-ui/set-header-title! set-header-title!)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header-home-icon-button-badge
  ; TEMP
  []
  [:div {:style {:position :absolute :top "4px" :right "10px" :background "var( --soft-blue )"
                 :width "20px" :height "20px" :border-radius "50%"
                 :font-size "8px" :font-weight 600 :align-items :center :line-height "18px"
                 :display :flex :flex-direction :column :justify-content :center}}
        (param "12")])

(defn- header-home-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) view-props
  ;  {:at-home? (boolean)}
  ;
  ; @return (component)
  [_ {:keys [at-home?]}]
  [:div [elements/button ::home-icon-button
                         {:disabled? (param at-home?)
                          :on-click  [:x.app-router/go-home!]
                          :preset    :home-icon-button}]
                         ;:tooltip   :back-to-home!
                         ;:icon      :dashboard
        (if-not (boolean at-home?)
                [header-home-icon-button-badge])])

(defn- header-up-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ _]
  [elements/button ::up-icon-button
                   {:preset   :back-icon-button
                    :on-click [:x.app-router/go-up!]}])
                   ;:tooltip  :back!

(defn- header-back-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ _]
  [elements/button ::back-icon-button
                   {:preset   :back-icon-button
                    :on-click [:x.app-router/go-back!]}])
                   ;:tooltip  :back!

(defn- header-db-browser-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ _]
  [elements/button ::db-browser-icon-button
                   {:icon      :storage
                    :icon-size :xxs
                    :on-click  [:x.app-developer/render-database-browser!]
                    :preset    :default-icon-button}])
                   ;:tooltip   :application-database-browser

(defn- header-menu-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ _]
  [elements/button ::menu-icon-button
                   {:preset   :user-menu-icon-button
                    :on-click [:x.app-views.menu/render!]}])
                   ;:tooltip  :app-menu}])

(defn- header-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) view-props
  ;  {:header-title (metamorphic-content)(opt)
  ;   :viewport-small? (boolean)}
  ;
  ; @return (component)
  [_ {:keys [header-title viewport-small?]}]
  [:div#x-app-header--title (components/content {:content header-title})])

(defn- header-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) view-props
  ;  {:label (metamorphic-content)(opt)
  ;   :parent-path (string)(opt)}
  ;
  ; @return (component)
  [header-id {:keys [header-title parent-path] :as view-props}]
  [:<> [:div.x-app-header--block (if (some? parent-path)  [header-up-icon-button         header-id view-props]
                                                          [header-home-icon-button       header-id view-props])]
       [:div.x-app-header--block (if (some? header-title) [header-label                  header-id view-props])]
       [:div.x-app-header--block (if (a/debug-mode?) [:<> [header-db-browser-icon-button header-id view-props]
                                                          [header-menu-icon-button       header-id view-props]]
                                                     [:<> [header-menu-icon-button       header-id view-props]])]])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) view-props
  ;  {:render-header? (boolean)}
  ;
  ; @return (component)
  [header-id {:keys [render-header?] :as view-props}]
  (cond (boolean render-header?)
        [:div#x-app-header {:data-nosnippet true}
                           [header-label-bar header-id view-props]]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [components/subscriber ::view
                         {:component  #'header
                          :subscriber [::get-view-props]}])
