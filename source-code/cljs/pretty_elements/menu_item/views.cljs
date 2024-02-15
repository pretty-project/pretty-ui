
(ns pretty-elements.menu-item.views
    (:require [fruits.random.api                    :as random]
              [pretty-elements.engine.api           :as pretty-elements.engine]
              [pretty-elements.menu-item.attributes :as menu-item.attributes]
              [pretty-elements.menu-item.prototypes :as menu-item.prototypes]
              [pretty-presets.engine.api            :as pretty-presets.engine]
              [reagent.core :as reagent]
              [pretty-accessories.api :as pretty-accessories]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-item
  ; @ignore
  ;
  ; @param (keyword) item-id
  ; @param (map) item-props
  ; {:badge (map)(opt)
  ;  :cover (map)(opt)
  ;  :icon (keyword)(opt)
  ;  :icon-position (keyword)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :marker (map)(opt)}
  [item-id {:keys [badge cover icon icon-position label marker] :as item-props}]
  [:div (menu-item.attributes/menu-item-attributes item-id item-props)
        [(pretty-elements.engine/clickable-auto-tag      item-id item-props)
         (menu-item.attributes/menu-item-body-attributes item-id item-props)
         (case icon-position :right [:<> (if label [:div (menu-item.attributes/menu-item-label-attributes item-id item-props) label])
                                         (if icon  [:i   (menu-item.attributes/menu-item-icon-attributes  item-id item-props) icon])]
                                    [:<> (if icon  [:i   (menu-item.attributes/menu-item-icon-attributes  item-id item-props) icon])
                                         (if label [:div (menu-item.attributes/menu-item-label-attributes item-id item-props) label])])
         (if badge  [pretty-accessories/badge  item-id badge])
         (if marker [pretty-accessories/marker item-id marker])
         (if cover  [pretty-accessories/cover  item-id cover])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) item-id
  ; @param (map) item-props
  [item-id item-props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    item-id item-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount item-id item-props))
                         :reagent-render         (fn [_ item-props] [menu-item item-id item-props])}))

(defn view
  ; @description
  ; Button element for menu bars with optional dropdown content for dropdown menus.
  ;
  ; @param (keyword)(opt) item-id
  ; @param (map) item-props
  ; {:badge (map)(opt)
  ;  :border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :click-effect (keyword)(opt)
  ;  :cover (map)(opt)
  ;  :cursor (keyword or string)(opt)
  ;  :dropdown-content (metamorphic-content)(opt)
  ;  :dropdown-menu-id (keyword)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;  :font-weight (keyword or integer)(opt)
  ;  :gap (keyword, px or string)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;  :highlighted? (boolean)(opt)
  ;  :highlight-color (keyword or string)(opt)
  ;  :highlight-pattern (keyword)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;  :hover-pattern (keyword)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :href-target (keyword)(opt)
  ;  :href-uri (string)(opt)
  ;  :icon (keyword)(opt)
  ;  :icon-color (keyword or string)(opt)
  ;  :icon-family (keyword)(opt)
  ;  :icon-position (keyword)(opt)
  ;  :icon-size (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :label (metamorphic-content)(opt)
  ;  :label-placeholder (metamorphic-content)(opt)
  ;  :letter-spacing (keyword, px or string)(opt)
  ;  :line-height (keyword, px or string)(opt)
  ;  :marker (map)(opt)
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-click-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :tab-disabled? (boolean)(opt)
  ;  :text-align (keyword)(opt)
  ;  :text-color (keyword or string)(opt)
  ;  :text-direction (keyword)(opt)
  ;  :text-overflow (keyword)(opt)
  ;  :text-selectable? (boolean)(opt)
  ;  :text-transform (keyword)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [menu-item {...}]
  ;
  ; @usage
  ; [menu-item :my-menu-item {...}]
  ([item-props]
   [view (random/generate-keyword) item-props])

  ([item-id item-props]
   ; @note (tutorials#parameterizing)
   (fn [_ item-props]
       (let [item-props (pretty-presets.engine/apply-preset        item-id item-props)
             item-props (menu-item.prototypes/item-props-prototype item-id item-props)]
            [view-lifecycles item-id item-props]))))