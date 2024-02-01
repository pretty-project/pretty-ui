
(ns pretty-elements.menu-bar.views
    (:require [fruits.hiccup.api                   :as hiccup]
              [fruits.random.api                   :as random]
              [metamorphic-content.api             :as metamorphic-content]
              [pretty-elements.menu-bar.attributes :as menu-bar.attributes]
              [pretty-elements.menu-bar.prototypes :as menu-bar.prototypes]
              [pretty-elements.engine.api                   :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api                         :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar-item
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ; {:href (string)(opt)
  ;  :icon (keyword)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :on-click (function or Re-Frame metamorphic-event)(opt)}
  [bar-id bar-props {:keys [href icon label on-click] :as item-props}]
  (let [item-props (pretty-presets.engine/apply-preset                 item-props)
        item-props (menu-bar.prototypes/item-props-prototype bar-props item-props)]
       [:div (menu-bar.attributes/menu-item-attributes bar-id bar-props item-props)
             [(cond href :a on-click :button :else :div)
              (menu-bar.attributes/menu-item-body-attributes bar-id bar-props item-props)
              (if icon  [:i   (menu-bar.attributes/menu-item-icon-attributes  bar-id bar-props item-props) icon])
              (if label [:div (menu-bar.attributes/menu-item-label-attributes bar-id bar-props item-props)
                              (metamorphic-content/compose label)])]]))

(defn- menu-bar
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; {:menu-items (maps in vector)}
  [bar-id {:keys [menu-items] :as bar-props}]
  ; For menu bars with horizontal orientation, the '{overflow-x: scroll}'
  ; and '{display: flex}' properties can only used together (properly)
  ; if the width of the scroll container element ('.pe-menu-bar--body') is not greater
  ; than the total width of the elements within.
  ; Therefore, the '{:horizontal-align :space-between}' setting cannot be implemented,
  ; while keeping the '{overflow-x: scroll}' property.
  [:div (menu-bar.attributes/menu-bar-attributes bar-id bar-props)
        [:div (menu-bar.attributes/menu-bar-body-attributes bar-id bar-props)
              (letfn [(f0 [item-props] [menu-bar-item bar-id bar-props item-props])]
                     [:div (menu-bar.attributes/menu-bar-items-attributes bar-id bar-props)
                           (hiccup/put-with [:<>] menu-items f0)])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  [bar-id bar-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    bar-id bar-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount bar-id bar-props))
                       :reagent-render         (fn [_ bar-props] [menu-bar bar-id bar-props])}))

(defn view
  ; @description
  ; You can set the default menu item properties by using the ':item-default'
  ; property or you can specify the properties for each menu item separately.
  ;
  ; @param (keyword)(opt) bar-id
  ; @param (map) bar-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :item-default (map)(opt)
  ;   {:badge-position (keyword)(opt)
  ;     Default: :tr
  ;    :border-color (keyword or string)(opt)
  ;    :border-radius (map)(opt)
  ;     {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;    :border-width (keyword, px or string)(opt)
  ;    :click-effect (keyword)(opt)
  ;     Default: :opacity (if 'href-uri' or 'on-click-f' is provided)
  ;    :fill-color (keyword or string)(opt)
  ;    :fill-pattern (keyword)(opt)
  ;     Default: :cover
  ;    :font-size (keyword, px or string)(opt)
  ;     Default: :s
  ;    :font-weight (keyword or integer)(opt)
  ;     Default :medium
  ;    :hover-color (keyword or string)(opt)
  ;    :hover-effect (keyword)(opt)
  ;    :hover-pattern (keyword)(opt)
  ;    :icon-color (keyword or string)(opt)
  ;     Default: :inherit
  ;    :icon-family (keyword)(opt)
  ;     Default: :material-symbols-outlined
  ;    :icon-size (keyword, px or string)(opt)
  ;     Default: provided :font-size (if any) or :s
  ;    :indent (map)(opt)
  ;     {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;    :line-height (keyword, px or string)(opt)
  ;     Default: :text-block
  ;    :marker-color (keyword or string)(opt)
  ;    :marker-position (keyword)(opt)
  ;     Default: :tr
  ;    :outdent (map)(opt)
  ;     {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;    :text-color (keyword or string)(opt)
  ;     Default: :inherit}
  ;  :menu-items (maps in vector)
  ;   [{:badge-color (keyword or string)(opt)
  ;      Default: :primary
  ;     :badge-content (metamorphic-content)(opt)
  ;     :disabled? (boolean)(opt)
  ;     :href-target (keyword)(opt)
  ;     :href-uri (string)(opt)
  ;     :icon (keyword)(opt)
  ;     :label (metamorphic-content)(opt)
  ;     :on-click-f (function)(opt)
  ;     :preset (keyword)(opt)}]
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :orientation (keyword)(opt)
  ;   :horizontal, :vertical
  ;   Default: :horizontal
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [menu-bar {...}]
  ;
  ; @usage
  ; [menu-bar :my-menu-bar {...}]
  ([bar-props]
   [view (random/generate-keyword) bar-props])

  ([bar-id bar-props]
   ; @note (tutorials#parameterizing)
   (fn [_ bar-props]
       (let [bar-props (pretty-presets.engine/apply-preset      bar-id bar-props)
             bar-props (menu-bar.prototypes/bar-props-prototype bar-id bar-props)]
            [view-lifecycles bar-id bar-props]))))
