
(ns templates.module-frame.side-menu.views
    (:require [components.api   :as components]
              [x.components.api :as x.components]))

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

(defn side-menu
  ; @param (keyword) module-id
  ; @param (map) menu-props
  ; {:content (metamorphic content)
  ;  :threshold (px)(opt)}
  [module-id {:keys [content threshold]}]
  [components/side-menu ::side-menu
                        {:content [:<> [components/side-menu-header {}]
                                       [workspace-button]
                                       [x.components/content module-id content]
                                       [components/side-menu-footer {}]]
                         :indent    {:left :xs}
                         :min-width :m
                         :threshold threshold}])
