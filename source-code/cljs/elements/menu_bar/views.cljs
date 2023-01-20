
(ns elements.menu-bar.views
    (:require [elements.menu-bar.attributes :as menu-bar.attributes]
              [elements.menu-bar.prototypes :as menu-bar.prototypes]
              [random.api                   :as random]
              [x.components.api             :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ; {:icon (keyword)(opt)
  ;  :label (metamorphic-content)(opt)
  [bar-id bar-props {:keys [icon label] :as item-props}]
  [:button (menu-bar.attributes/menu-item-body-attributes bar-id bar-props item-props)
           (if icon  [:i   (menu-bar.attributes/menu-item-icon-attributes  bar-id bar-props item-props) icon])
           (if label [:div (menu-bar.attributes/menu-item-label-attributes bar-id bar-props item-props)
                           (x.components/content label)])])

(defn- anchor-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ; {:icon (keyword)(opt)
  ;  :label (metamorphic-content)(opt)}
  [bar-id bar-props {:keys [icon label] :as item-props}]
  [:a (menu-bar.attributes/menu-item-body-attributes bar-id bar-props item-props)
      (if icon  [:i   (menu-bar.attributes/menu-item-icon-attributes  bar-id bar-props item-props) icon])
      (if label [:div (menu-bar.attributes/menu-item-label-attributes bar-id bar-props item-props)
                      (x.components/content label)])])

(defn- menu-bar-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ; {:href (string)(opt)
  ;  :on-click (metamorphic-event)(opt)}
  [bar-id bar-props {:keys [href on-click] :as item-props}]
  (let [item-props (menu-bar.prototypes/item-props-prototype bar-props item-props)]
       [:div (menu-bar.attributes/menu-item-attributes bar-id bar-props item-props)
             (cond (some? href)     [anchor-item bar-id bar-props item-props]
                   (some? on-click) [toggle-item bar-id bar-props item-props])]))

(defn- menu-bar-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; {:menu-items (maps in vector)
  ;  :orientation (keyword)}
  [bar-id {:keys [menu-items orientation] :as bar-props}]
  (letfn [(f [item-list item-props] (conj item-list [menu-bar-item bar-id bar-props item-props]))]
         [:div (case orientation :horizontal {:class            :e-menu-bar--menu-items
                                              :data-orientation :horizontal
                                              :data-scroll-axis :x}
                                             {:class            :e-menu-bar--menu-items
                                              :data-orientation :vertical})
               (reduce f [:<>] menu-items)]))

(defn- menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  [bar-id bar-props]
  ; XXX#5406
  ; For menu bars with horizontal orientation, the {overflow-x: scroll}
  ; and {display: flex} properties can only be used together (without errors)
  ; if the width of the scroll container element (.e-menu-bar--body) is not greater
  ; than the total width of the elements inside it.
  ; Therefore the {:horizontal-align :space-between} setting cannot be implemented,
  ; while keeping the {overflow-x: scroll} property.
  [:div (menu-bar.attributes/menu-bar-attributes bar-id bar-props)
        [:div (menu-bar.attributes/menu-bar-body-attributes bar-id bar-props)
              [menu-bar-items                               bar-id bar-props]]])

(defn element
  ; XXX#0713
  ; Some other items based on the menu-bar element and their documentations are linked to here.
  ;
  ; @description
  ; You can set the default item styles and settings by using the :item-default
  ; property or you can specify these values on each item separatelly.
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
  ;     :tl, :tr, :br, :bl
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
  ;     :inherit, :extra-light, :light, :normal, :medium, :bold, :extra-bold
  ;     Default :medium
  ;    :hover-color (keyword or string)(opt)
  ;     :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
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
  ;     :inherit, :native, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;     Default: :text-block
  ;    :marker-color (keyword)(opt)
  ;     :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;    :marker-position (keyword)(opt)
  ;     :tl, :tr, :br, :bl
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
  ;     :on-click (metamorphic-event)(opt)}]
  ;     :on-mouse-over (metamorphic-event)(opt)}]
  ;  :orientation (keyword)(opt)
  ;   :horizontal, :vertical
  ;   Default: :horizontal
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
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
   (let [bar-props (menu-bar.prototypes/bar-props-prototype bar-props)]
        [menu-bar bar-id bar-props])))
