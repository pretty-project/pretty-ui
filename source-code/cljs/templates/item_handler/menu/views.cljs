
(ns templates.item-handler.menu.views
    (:require [components.api                         :as components]
              [re-frame.api                           :as r]
              [templates.item-handler.menu.prototypes :as menu.prototypes]
              [x.components.api                       :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- group-label
  ; @param (keyword) handler-id
  ; @param (map) menu-props
  ; {}
  [_ {:keys [group-icon group-icon-family group-label]}]
  [components/side-menu-label ::group-label
                              {:content     group-label}])
                               ;:icon        group-icon
                               ;:icon-family group-icon-family}])

(defn- side-menu
  ; @param (keyword) handler-id
  ; @param (map) menu-props
  [handler-id menu-props]
  [:<> [group-label handler-id menu-props]
       [components/side-menu-button {:label "Edit"
                                     :icon :edit
                                     :preset :active}]
       [components/side-menu-button {:label "Price quotes"
                                     :icon :request_quote}]
       [components/side-menu-button {:label "Jobs"
                                     :icon :article}]])

(defn menu
  ; @param (keyword) handler-id
  ; @param (map) menu-props
  ; {:content (metamorphic-content)(opt)
  ;  :group-icon (keyword)
  ;  :group-icon-family (keyword)(opt)
  ;   Default: :material-symbols-outlined
  ;  :group-label (metamorphic-content)}
  ;
  ; @usage
  ; [menu :my-handler {...}]
  [handler-id menu-props]
  (let [menu-props (menu.prototypes/menu-props-prototype handler-id menu-props)]
       (if @(r/subscribe [:item-handler/current-item-downloaded? handler-id])
            [side-menu handler-id menu-props])))
