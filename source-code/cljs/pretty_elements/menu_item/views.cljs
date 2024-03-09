
(ns pretty-elements.menu-item.views
    (:require [fruits.random.api                    :as random]
              [pretty-accessories.api               :as pretty-accessories]
              [pretty-elements.engine.api           :as pretty-elements.engine]
              [pretty-elements.menu-item.attributes :as menu-item.attributes]
              [pretty-elements.menu-item.prototypes :as menu-item.prototypes]
              [pretty-elements.methods.api          :as pretty-elements.methods]
              [pretty-models.api                    :as pretty-models]
              [pretty-subitems.api                  :as pretty-subitems]
              [reagent.core                         :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-item
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:badge (map)(opt)
  ;  :icon (map)(opt)
  ;  :label (map)(opt)
  ;  :marker (map)(opt)
  ;  :tooltip (map)(opt)
  ;  ...}
  [id {:keys [badge icon label marker tooltip] :as props}]
  [:div (menu-item.attributes/outer-attributes id props)
        [(pretty-models/clickable-model-auto-tag props) (menu-item.attributes/inner-attributes id props)
         (if label   [pretty-accessories/label   (pretty-subitems/subitem-id id :label)   label])
         (if icon    [pretty-accessories/icon    (pretty-subitems/subitem-id id :icon)    icon])
         (if badge   [pretty-accessories/badge   (pretty-subitems/subitem-id id :badge)   badge])
         (if marker  [pretty-accessories/marker  (pretty-subitems/subitem-id id :marker)  marker])
         (if tooltip [pretty-accessories/tooltip (pretty-subitems/subitem-id id :tooltip) tooltip])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount id props))
                         :reagent-render         (fn [_ props] [menu-item id props])}))

(defn view
  ; @description
  ; Button element for menu bars with optional dropdown content for dropdown menus.
  ;
  ; @links Implemented accessories
  ; [Badge](pretty-ui/cljs/pretty-accessories/api.html#badge)
  ; [Icon](pretty-ui/cljs/pretty-accessories/api.html#icon)
  ; [Label](pretty-ui/cljs/pretty-accessories/api.html#label)
  ; [Marker](pretty-ui/cljs/pretty-accessories/api.html#marker)
  ; [Tooltip](pretty-ui/cljs/pretty-accessories/api.html#tooltip)
  ;
  ; @links Implemented models
  ; [Clickable model](pretty-core/cljs/pretty-models/api.html#clickable-model)
  ; [Container model](pretty-core/cljs/pretty-models/api.html#container-model)
  ;
  ; @links Implemented properties
  ; [Dropdown properties](pretty-core/cljs/pretty-properties/api.html#dropdown-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented models.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/menu-item.png)
  ; [menu-item {:border-color    :secondary
  ;             :border-width    :xs
  ;             :border-position :bottom
  ;             :href-uri        "/my-uri"
  ;             :label           {:content "My menu item"}}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-map    id props {:icon :icon-name :label :content})
             props (pretty-elements.methods/apply-element-preset           id props)
             props (pretty-elements.methods/import-element-dynamic-props   id props)
             props (pretty-elements.methods/import-element-focus-reference id props)
             props (pretty-elements.methods/import-element-state-events    id props)
             props (pretty-elements.methods/import-element-state           id props)
             props (menu-item.prototypes/props-prototype                   id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
