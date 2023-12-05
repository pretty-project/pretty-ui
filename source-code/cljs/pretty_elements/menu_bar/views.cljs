
(ns pretty-elements.menu-bar.views
    (:require [pretty-elements.menu-bar.attributes :as menu-bar.attributes]
              [pretty-elements.menu-bar.prototypes :as menu-bar.prototypes]
              [hiccup.api                   :as hiccup]
              [metamorphic-content.api      :as metamorphic-content]
              [pretty-presets.api           :as pretty-presets]
              [random.api                   :as random]))

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
  ;  :on-click (Re-Frame metamorphic-event)(opt)}
  [bar-id bar-props {:keys [href icon label on-click] :as item-props}]
  (let [item-props (pretty-presets/apply-preset                        item-props)
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
  ; XXX#5406
  ; For menu bars with horizontal orientation, the '{overflow-x: scroll}'
  ; and '{display: flex}' properties can only used together (without errors)
  ; if the width of the scroll container element ('.pe-menu-bar--body') is not greater
  ; than the total width of the elements within.
  ; Therefore, the '{:horizontal-align :space-between}' setting cannot be implemented,
  ; while keeping the '{overflow-x: scroll}' property.
  [:div (menu-bar.attributes/menu-bar-attributes bar-id bar-props)
        [:div (menu-bar.attributes/menu-bar-body-attributes bar-id bar-props)
              (letfn [(f0 [item-props] [menu-bar-item bar-id bar-props item-props])]
                     [:div (menu-bar.attributes/menu-bar-items-attributes bar-id bar-props)
                           (hiccup/put-with [:<>] menu-items f0)])]])

(defn element
  ; @info
  ; XXX#0713
  ; Some other items based on the 'menu-bar' element and their documentations link here.
  ;
  ; @description
  ; You can set the default menu item properties by using the ':item-default'
  ; property or you can specify the properties for each menu item separately.
  ;
  ; @param (keyword)(opt) bar-id
  ; @param (map) bar-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;   :center, :left, :right
  ;   Default: :left
  ;   W/ {:orientation :horizontal}
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :item-default (map)(opt)
  ;   {:badge-position (keyword)(opt)
  ;     :tl, :tr, :br, :bl, :left, :right, :bottom, :top
  ;     Default: :tr
  ;    :border-color (keyword or string)(opt)
  ;     :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;    :border-radius (map)(opt)
  ;    :border-width (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;    :color (keyword or string)(opt)
  ;     :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;     Default: :inherit
  ;    :fill-color (keyword or string)(opt)
  ;     :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;    :font-size (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;     Default: :s
  ;    :font-weight (keyword)(opt)
  ;     :inherit, :thin, :extra-light, :light, :normal, :medium, :semi-bold, :bold, :extra-bold, :black
  ;     Default :medium
  ;    :hover-color (keyword or string)(opt)
  ;     :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;    :hover-effect (keyword)(opt)
  ;     :opacity
  ;    :icon-color (keyword or string)(opt)
  ;     :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;     Default: :inherit
  ;    :icon-family (keyword)(opt)
  ;     :material-symbols-filled, :material-symbols-outlined
  ;     Default: :material-symbols-outlined
  ;    :icon-size (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;     Default: :s
  ;    :indent (map)(opt)
  ;    :line-height (keyword)(opt)
  ;     :auto, :inherit, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;     Default: :text-block
  ;    :marker-color (keyword)(opt)
  ;     :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;    :marker-position (keyword)(opt)
  ;     :tl, :tr, :br, :bl, left, :right, bottom, :top
  ;     Default: :tr
  ;    :outdent (map)(opt)}
  ;  :menu-items (maps in vector)
  ;   [{:badge-color (keyword)(opt)
  ;      :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;      Default: :primary
  ;     :badge-content (metamorphic-content)(opt)
  ;     :disabled? (boolean)(opt)
  ;     :href (string)(opt)
  ;     :icon (keyword)(opt)
  ;     :label (metamorphic-content)(opt)
  ;     :on-click (Re-Frame metamorphic-event)(opt)
  ;     :on-mouse-over (Re-Frame metamorphic-event)(opt)
  ;     :preset (keyword)(opt)
  ;     :target (keyword)(opt)
  ;      :blank, :self}]
  ;  :orientation (keyword)(opt)
  ;   :horizontal, :vertical
  ;   Default: :horizontal
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [menu-bar {...}]
  ;
  ; @usage
  ; [menu-bar :my-menu-bar {...}]
  ([bar-props]
   [element (random/generate-keyword) bar-props])

  ([bar-id bar-props]
   (fn [_ bar-props] ; XXX#0106 (README.md#parametering)
       (let [bar-props (pretty-presets/apply-preset             bar-props)
             bar-props (menu-bar.prototypes/bar-props-prototype bar-props)]
            [menu-bar bar-id bar-props]))))
