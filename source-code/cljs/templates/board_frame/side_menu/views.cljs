
(ns templates.board-frame.side-menu.views
    (:require [components.api                             :as components]
              [templates.board-frame.side-menu.prototypes :as side-menu.prototypes]
              [x.components.api                           :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- workspace-button
  []
  [components/side-menu-button ::workspace-button
                               {:icon        :circle
                                ;:icon-color "var(--soft-blue-xx-dark)"
                                :label "My workspace"
                                :on-click [:x.router/go-home!]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn side-menu-content
  ; @param (keyword) board-id
  ; @param (map) menu-props
  ; {:content (metamorphic content)}
  [board-id {:keys [content]}]
  [:<> [components/side-menu-header {}]
       [workspace-button]
       [x.components/content board-id content]
       [components/side-menu-footer {}]])

(defn side-menu
  ; @param (keyword) board-id
  ; @param (map) menu-props
  ;
  ; @usage
  ; [side-menu :my-board {...}]
  [board-id menu-props]
  (let [menu-props (side-menu.prototypes/menu-props-prototype menu-props)
        menu-props (assoc menu-props :content [side-menu-content board-id menu-props])]
       [components/side-menu ::side-menu menu-props]))
