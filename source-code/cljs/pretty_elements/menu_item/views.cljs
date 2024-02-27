
(ns pretty-elements.menu-item.views
    (:require [fruits.random.api                    :as random]
              [pretty-elements.engine.api           :as pretty-elements.engine]
              [pretty-elements.menu-item.attributes :as menu-item.attributes]
              [pretty-elements.menu-item.prototypes :as menu-item.prototypes]
              [pretty-presets.engine.api            :as pretty-presets.engine]
              [reagent.core :as reagent]
              [pretty-accessories.api :as pretty-accessories]
              [pretty-elements.icon.views :as icon.views]
              [pretty-elements.label.views :as label.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-item
  ; @ignore
  ;
  ; @param (keyword) item-id
  ; @param (map) item-props
  ; {:badge (map)(opt)
  ;  :icon (map)(opt)
  ;  :label (map)(opt)
  ;  :marker (map)(opt)
  ;  ...}
  [item-id {:keys [badge icon label marker] :as item-props}]
  [:div (menu-item.attributes/menu-item-attributes item-id item-props)
        [(pretty-elements.engine/clickable-auto-tag       item-id item-props)
         (menu-item.attributes/menu-item-inner-attributes item-id item-props)
         (if label  [label.views/view          item-id label])
         (if icon   [icon.views/view           item-id icon])
         (if badge  [pretty-accessories/badge  item-id badge])
         (if marker [pretty-accessories/marker item-id marker])]])

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
  ; [Marker](pretty-ui/cljs/pretty-accessories/api.html#marker)
  ; [Tooltip](pretty-ui/cljs/pretty-accessories/api.html#tooltip)
  ;
  ; @links Implemented elements
  ; [Icon](pretty-ui/cljs/pretty-elements/api.html#icon)
  ; [Label](pretty-ui/cljs/pretty-elements/api.html#label)
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
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) item-id
  ; @param (map) item-props
  ; Check out the implemented accessories.
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/menu-item.png)
  ; [menu-item {:border-color    :secondary
  ;             :border-width    :xs
  ;             :border-position :bottom
  ;             :href-uri        "/my-uri"
  ;             :label           {:content "My menu item"}}]
  ([item-props]
   [view (random/generate-keyword) item-props])

  ([item-id item-props]
   ; @note (tutorials#parameterizing)
   (fn [_ item-props]
       (let [item-props (pretty-presets.engine/apply-preset        item-id item-props)
             item-props (menu-item.prototypes/item-props-prototype item-id item-props)]
            [view-lifecycles item-id item-props]))))
