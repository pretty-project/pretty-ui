
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
  ;  :marker (map)(opt)
  ;  ...}
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
  ; @links Implemented accessories
  ; [Badge](pretty-ui/cljs/pretty-accessories/api.html#badge)
  ; [Cover](pretty-ui/cljs/pretty-accessories/api.html#cover)
  ; [Marker](pretty-ui/cljs/pretty-accessories/api.html#marker)
  ; [Tooltip](pretty-ui/cljs/pretty-accessories/api.html#tooltip)
  ;
  ; @links Implemented properties
  ; [Anchor properties](pretty-core/cljs/pretty-properties/api.html#anchor-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Clickable state properties](pretty-core/cljs/pretty-properties/api.html#clickable-state-properties)
  ; [Cursor properties](pretty-core/cljs/pretty-properties/api.html#cursor-properties)
  ; [Effect properties](pretty-core/cljs/pretty-properties/api.html#effect-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Icon properties](pretty-core/cljs/pretty-properties/api.html#icon-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Label properties](pretty-core/cljs/pretty-properties/api.html#label-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) item-id
  ; @param (map) item-props
  ; Check out the implemented accessories.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/menu-item.png)
  ; [menu-item {:border-color    :secondary
  ;             :border-width    :xs
  ;             :border-position :bottom
  ;             :href-uri        "/my-uri"
  ;             :label           "My menu item"}]
  ([item-props]
   [view (random/generate-keyword) item-props])

  ([item-id item-props]
   ; @note (tutorials#parameterizing)
   (fn [_ item-props]
       (let [item-props (pretty-presets.engine/apply-preset        item-id item-props)
             item-props (menu-item.prototypes/item-props-prototype item-id item-props)]
            [view-lifecycles item-id item-props]))))
