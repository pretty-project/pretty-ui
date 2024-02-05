
(ns pretty-elements.dropdown-menu.views
    (:require [fruits.random.api                        :as random]
              [pretty-elements.dropdown-menu.attributes :as dropdown-menu.attributes]
              [pretty-elements.dropdown-menu.prototypes :as dropdown-menu.prototypes]
              [pretty-elements.menu-bar.views           :as menu-bar.views]
              [pretty-elements.surface.views           :as surface.views]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [pretty-elements.engine.api :as pretty-elements.engine]
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
  ;
  ; @note
  ; For more information, check out the documentation of the ['menu-bar'](/pretty-ui/cljs/pretty-elements/api.html#menu-bar) element.
  ;
  ; @param (keyword)(opt) menu-id
  ; @param (map) menu-props
  ; {:menu-item-default (map)(opt)
  ;  :menu-items (maps in vector)(opt)
  ;   [{:content (metamorphic-content)(opt)
  ;     :placeholder (metamorphic-content)(opt)
  ;     :preset (keyword)(opt)}]

  ;  :disabled? (boolean)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)

  ;  :surface (map)(opt)
  ;   {:border-color (keyword or string)(opt)
  ;    :border-position (keyword)(opt)
  ;    :border-radius (map)(opt)
  ;     {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;    :border-width (keyword, px or string)(opt)
  ;    :fill-color (keyword or string)
  ;    :indent (map)(opt)
  ;    :outdent (map)(opt)
  ;    :preset (keyword)(opt)}}
  ;
  ; :menu-bar (map)(opt)
  ; :surface (map)(opt)
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
       (let [menu-props (pretty-presets.engine/apply-preset            menu-id menu-props)
             menu-props (dropdown-menu.prototypes/menu-props-prototype menu-id menu-props)]
             ;menu-props (pretty-elements.engine/apply-element-item-default     menu-id menu-props :menu-items :menu-item-default)
             ;menu-props (pretty-elements.engine/inherit-element-disabled-state menu-id menu-props :menu-items :menu-item-default)
            [view-lifecycles menu-id menu-props]))))
