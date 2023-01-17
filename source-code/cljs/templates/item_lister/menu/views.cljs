
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
                               {:icon     :add_circle
                                :icon-color :secondary
                                :label    "Add item"
                                :on-click on-click}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- group-label                                ; <- már nem label
  ; @param (keyword) lister-id
  ; @param (map) menu-props
  ; {:group-icon (keyword)
  ;  :group-icon-family (keyword)
  ;  :group-label (metamorphic-content)}
  [_ {:keys [group-icon group-icon-family group-label]}]
  [components/side-menu-button ::group-label
                              {:label     group-label
                               :icon        group-icon
                               :icon-family group-icon-family
                               ;:icon-color :primary

                               :preset :active}])

(defn menu
  ; @param (keyword) lister-id
  ; @param (map) menu-props
  ; {:content (metamorphic-content)(opt)
  ;  :group-icon (keyword)
  ;  :group-icon-family (keyword)(opt)
  ;   Default: :material-symbols-outlined
  ;  :group-label (metamorphic-content)}
  ;
  ; @usage
  ; [menu :my-lister {...}]
  [lister-id {:keys [content] :as menu-props}]
  (let [menu-props (menu.prototypes/menu-props-prototype lister-id menu-props)]
       [:<> [group-label          lister-id menu-props]

            ; TEMP
            [components/side-menu-button {:label "Board members"
                                          :icon :groups
                                          ;:icon-color "red"
                                          :badge-content "40"
                                          ;:badge-position :tr
                                          :icon-size :m}]




            [x.components/content lister-id content]]))
