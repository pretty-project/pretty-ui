
(ns templates.module-frame.side-menu.views
    (:require [components.api                              :as components]
              [templates.module-frame.side-menu.prototypes :as side-menu.prototypes]
              [x.components.api                            :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- workspace-button
  []
  [components/side-menu-button ::workspace-button
                               {:icon        :circle
                                ;:icon-color "var(--soft-blue-xx-dark)"
                                :icon-family :material-icons-outlined
                                :label "My workspace"
                                :on-click [:x.router/go-home!]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn side-menu-content
  ; @param (keyword) module-id
  ; @param (map) menu-props
  ; {:content (metamorphic content)}
  [module-id {:keys [content]}]
  [:<> [components/side-menu-header {}]
       [workspace-button]
       [x.components/content module-id content]
       [components/side-menu-footer {}]])

(defn side-menu
  ; @param (keyword) module-id
  ; @param (map) menu-props
  [module-id menu-props]
  (let [menu-props (side-menu.prototypes/menu-props-prototype menu-props)
        menu-props (assoc menu-props :content [side-menu-content module-id menu-props])]
       [components/side-menu ::side-menu menu-props]))
