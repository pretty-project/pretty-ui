
(ns pretty-elements.dropdown-menu.views
    (:require [fruits.random.api                        :as random]
              [pretty-elements.dropdown-menu.attributes :as dropdown-menu.attributes]
              [pretty-elements.dropdown-menu.prototypes :as dropdown-menu.prototypes]
              [pretty-elements.engine.api               :as pretty-elements.engine]
              [pretty-elements.menu-bar.views           :as menu-bar.views]
              [pretty-elements.surface.views            :as surface.views]
              [pretty-presets.engine.api                :as pretty-presets.engine]
              [reagent.api                              :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- dropdown-menu
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  [menu-id menu-props]
  [:div (dropdown-menu.attributes/menu-attributes menu-id menu-props)
        [:div (dropdown-menu.attributes/menu-body-attributes menu-id menu-props)
              (let [bar-id    (pretty-elements.engine/element-id->subitem-id menu-id :menu-bar)
                    bar-props (dropdown-menu.prototypes/bar-props-prototype  menu-id menu-props)]
                   [menu-bar.views/view bar-id bar-props])
              (let [surface-id    (pretty-elements.engine/element-id->subitem-id    menu-id :surface)
                    surface-props (dropdown-menu.prototypes/surface-props-prototype menu-id menu-props)]
                   [surface.views/view surface-id surface-props])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  [menu-id menu-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    menu-id menu-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount menu-id menu-props))
                       :reagent-render         (fn [_ menu-props] [dropdown-menu menu-id menu-props])}))

(defn view
  ; @description
  ; Dropdown style menu.
  ;
  ; @param (keyword)(opt) menu-id
  ; @param (map) menu-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :menu-bar (map)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :surface (map)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [dropdown-menu {...}]
  ;
  ; @usage
  ; [dropdown-menu :my-dropdown-menu {...}]
  ([menu-props]
   [view (random/generate-keyword) menu-props])

  ([menu-id menu-props]
   ; @note (tutorials#parameterizing)
   (fn [_ menu-props]
       (let [menu-props (pretty-presets.engine/apply-preset                     menu-id menu-props)
             menu-props (dropdown-menu.prototypes/menu-props-prototype          menu-id menu-props)
             menu-props (pretty-elements.engine/element-subitem<-disabled-state menu-id menu-props :menu-bar)
             menu-props (pretty-elements.engine/element-subitem<-disabled-state menu-id menu-props :surface)
             menu-props (pretty-elements.engine/leave-element-disabled-state    menu-id menu-props :menu-bar)
             menu-props (pretty-elements.engine/leave-element-disabled-state    menu-id menu-props :surface)]
            [view-lifecycles menu-id menu-props]))))
