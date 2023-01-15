
(ns templates.static-page.side-menu.views
    (:require [components.api                             :as components]
              [templates.static-page.side-menu.prototypes :as side-menu.prototypes]
              [x.components.api                           :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn side-menu-content
  ; @param (keyword) page-id
  ; @param (map) menu-props
  ; {:content (metamorphic content)}
  [page-id {:keys [content]}]
  [:<> [components/side-menu-header {}]
       [x.components/content page-id content]
       [components/side-menu-footer {}]])

(defn side-menu
  ; @param (keyword) module-id
  ; @param (map) menu-props
  ;
  ; @usage
  ; [side-menu :my-static-page {...}]
  [page-id menu-props]
  (let [menu-props (side-menu.prototypes/menu-props-prototype menu-props)
        menu-props (assoc menu-props :content [side-menu-content page-id menu-props])]
       [components/side-menu ::side-menu menu-props]))
