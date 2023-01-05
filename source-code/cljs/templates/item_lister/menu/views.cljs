
(ns templates.item-lister.menu.views
    (:require [components.api                        :as components]
              [templates.item-lister.menu.prototypes :as menu.prototypes]
              [x.components.api                      :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-item-button
  ; @param (keyword) lister-id
  ; @param (map) menu-props
  ; {:on-click (metamorphic-event)}
  ;
  ; @usage
  ; [create-item-button :my-lister {...}]
  [_ {:keys [on-click]}]
  [components/side-menu-button ::create-item-button
                               {:icon     :add
                                :label    :add!
                                :on-click on-click}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- group-label
  ; @param (keyword) lister-id
  ; @param (map) menu-props
  ; {:group-icon (keyword)
  ;  :group-icon-family (keyword)
  ;  :group-label (metamorphic-content)}
  [_ {:keys [group-icon group-icon-family group-label]}]
  [components/side-menu-label ::group-label
                              {:content     group-label
                               :icon        group-icon
                               :icon-family group-icon-family}])

(defn menu
  ; @param (keyword) lister-id
  ; @param (map) menu-props
  ; {:content (metamorphic-content)(opt)
  ;  :group-icon (keyword)
  ;  :group-icon-family (keyword)(opt)
  ;   Default: :material-icons-filled
  ;  :group-label (metamorphic-content)}
  ;
  ; @usage
  ; [menu :my-lister {...}]
  [lister-id {:keys [content] :as menu-props}]
  (let [menu-props (menu.prototypes/menu-props-prototype lister-id menu-props)]
       [:<> [group-label          lister-id menu-props]
            [x.components/content lister-id content]]))
