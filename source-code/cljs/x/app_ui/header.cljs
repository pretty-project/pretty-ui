
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
  (get-in db (db/path :ui/header :header-title)))

(defn- get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:at-home? (boolean)
  ;   :debug-mode? (boolean)
  ;   :header-title (metamorphic-content)
  ;   :parent-path (string)
  ;   :render-header? (boolean)
  ;   :viewport-small? (boolean)}
  [db _]
  {:at-home?        (r router/at-home?                  db)
   :debug-mode?     (r a/debug-mode-detected?           db)
   :header-title    (r get-header-title                 db)
   :parent-path     (r router/get-current-route-parent  db)
   :render-header?  (r interface/application-interface? db)
   :viewport-small? (r environment/viewport-small?      db)})

(a/reg-sub :ui/get-header-props get-header-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-header-title!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (metamorphic-content) title
  ;
  ; @return (map)
  [db [_ header-title]]
  (assoc-in db (db/path :ui/header :header-title) header-title))

(a/reg-event-db :ui/set-header-title! set-header-title!)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header-apps-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;  {:at-home? (boolean)}
  ;
  ; @return (component)
  [_ {:keys [at-home?]}]
  [elements/button ::apps-icon-button
                   {:badge-color (if-not at-home? :secondary)
                    :disabled?   (param at-home?)
                    :on-click    [:router/go-home!]
                    :preset      :apps-icon-button}])
                   ;:icon        :dashboard

(defn- header-up-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (component)
  [_ _]
  [elements/button ::up-icon-button
                   {:preset   :back-icon-button
                    :on-click [:router/go-up!]}])

(defn- header-back-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (component)
  [_ _]
  [elements/button ::back-icon-button
                   {:preset   :back-icon-button
                    :on-click [:router/go-back!]}])

(defn- header-dev-tools-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (component)
  [_ _]
  [elements/button ::dev-tools-icon-button
                   {:icon     :auto_fix_high
                    :on-click [:developer/render-developer-tools!]
                    :preset   :default-icon-button}])

(defn- header-menu-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (component)
  [_ _]
  [elements/button ::menu-icon-button
                   {:preset   :user-menu-icon-button
                    :on-click [:views/render-menu!]}])

(defn- header-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
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
  ; @param (map) header-props
  ;  {:label (metamorphic-content)(opt)
  ;   :parent-path (string)(opt)}
  ;
  ; @return (component)
  [header-id {:keys [debug-mode? header-title parent-path] :as header-props}]
  [:<> [:div.x-app-header--block (if (some?   parent-path)      [header-up-icon-button        header-id header-props]
                                                                [header-apps-icon-button      header-id header-props])]
       [:div.x-app-header--block (if (some?   header-title)     [header-label                 header-id header-props])]
       [:div.x-app-header--block (if (boolean debug-mode?) [:<> [header-dev-tools-icon-button header-id header-props]
                                                                [header-menu-icon-button      header-id header-props]]
                                                           [:<> [header-menu-icon-button      header-id header-props]])]])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;  {:render-header? (boolean)}
  ;
  ; @return (component)
  [header-id {:keys [render-header?] :as header-props}]
  (cond (boolean render-header?)
        [:div#x-app-header {:data-nosnippet true}
                           [header-label-bar header-id header-props]]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [components/subscriber ::view
                         {:component  #'header
                          :subscriber [:ui/get-header-props]}])
