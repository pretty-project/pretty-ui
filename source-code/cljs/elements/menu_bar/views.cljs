
(ns elements.menu-bar.views
    (:require [elements.menu-bar.helpers    :as menu-bar.helpers]
              [elements.menu-bar.prototypes :as menu-bar.prototypes]
              [random.api                   :as random]
              [x.components.api             :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-item-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; {:font-size (keyword)}
  ; @param (map) item-props
  ; {:icon (keyword)(opt)
  ;  :icon-family (keyword)(opt)}
  [_ {:keys [font-size]} {:keys [icon icon-family]}]
  (if icon [:div.e-menu-bar--menu-item--icon {:data-icon-family icon-family :data-icon-size font-size} icon]))

(defn- menu-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; {:font-size (keyword)
  ;  :font-weight (keyword)}
  ; @param (map) item-props
  ; {:label (metamorphic-content)(opt)}
  [_ {:keys [font-size font-weight]} {:keys [label]}]
  (if label [:div.e-menu-bar--menu-item--label {:data-font-size     font-size
                                                :data-font-weight   font-weight
                                                :data-line-height   :text-block
                                                :data-text-overflow :no-wrap}
                                               (x.components/content label)]))

(defn- toggle-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  [bar-id bar-props item-props]
  [:button.e-menu-bar--menu-item (menu-bar.helpers/menu-item-attributes bar-id bar-props item-props)
                                 [menu-item-icon                        bar-id bar-props item-props]
                                 [menu-item-label                       bar-id bar-props item-props]])

(defn- anchor-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  [bar-id bar-props item-props]
  [:a.e-menu-bar--menu-item (menu-bar.helpers/menu-item-attributes bar-id bar-props item-props)
                            [menu-item-icon                        bar-id bar-props item-props]
                            [menu-item-label                       bar-id bar-props item-props]])

(defn- menu-bar-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ; {:href (string)(opt)
  ;  :on-click (metamorphic-event)(opt)}
  [bar-id bar-props {:keys [href on-click] :as item-props}]
  (let [item-props (menu-bar.prototypes/item-props-prototype item-props)]
       (cond (some? href)     [anchor-item bar-id bar-props item-props]
             (some? on-click) [toggle-item bar-id bar-props item-props])))

(defn- menu-bar-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; {:menu-items (maps in vector)
  ;  :orientation (keyword)}
  [bar-id {:keys [menu-items orientation] :as bar-props}]
  (letfn [(f [item-list item-props] (conj item-list [menu-bar-item bar-id bar-props item-props]))]
         [:div.e-menu-bar--menu-items (case orientation :horizontal {:data-orientation  :horizontal
                                                                     :data-scrollable-x true}
                                                                    {:data-orientation  :vertical})
                                      (reduce f [:<>] menu-items)]))

(defn- menu-bar-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  [bar-id bar-props]
  ; XXX#5406
  ; For a menu bar with a horizontal orientation, the {overflow-x: scroll}
  ; and {display: flex} properties can only be used together (without errors)
  ; if the width of the scroll container element (.e-menu-bar--body) is not greater
  ; than the total width of the elements inside it.
  ; Therefore the {:horizontal-align :space-between} setting cannot be implemented,
  ; while keeping the {overflow-x: scroll} property.
  [:div.e-menu-bar--body (menu-bar.helpers/menu-bar-body-attributes bar-id bar-props)
                         [menu-bar-items                            bar-id bar-props]])

(defn- menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  [bar-id bar-props]
  [:div.e-menu-bar (menu-bar.helpers/menu-bar-attributes bar-id bar-props)
                   [menu-bar-body                        bar-id bar-props]])

(defn element
  ; @param (keyword)(opt) bar-id
  ; @param (map) bar-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;   :center, :left, :right
  ;   Default: :left
  ;   W/ {:orientation :horizontal}
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;   Default: :s
  ;  :font-weight (keyword)(opt)
  ;   :bold, extra-bold, :inherit, :normal
  ;   Default :bold
  ;  :height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :xxl
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :left (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :right (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :top (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;  :menu-items (maps in vector)
  ;   [{:active? (boolean)(opt)
  ;      Default: false
  ;     :badge-color (keyword)(opt)
  ;      :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;      Default: :primary
  ;      W/ {:badge-content ...}
  ;     :badge-content (metamorphic-content)(opt)
  ;     :badge-position (keyword)(opt)
  ;      :tl, :tr, :br, :bl
  ;      Default: :tr
  ;      W/ {:badge-content ...}
  ;     :disabled? (boolean)(opt)
  ;      Default: false
  ;     :href (string)(opt)
  ;     :icon (keyword)(opt)
  ;     :icon-family (keyword)(opt)
  ;      :material-icons-filled, :material-icons-outlined
  ;      Default: :material-icons-filled
  ;     :label (metamorphic-content)(opt)
  ;     :marker-color (keyword)(opt)
  ;      :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;     :marker-position (keyword)(opt)
  ;      :tl, :tr, :br, :bl
  ;      Default: :tr
  ;      W/ {:marker-color ...}
  ;     :on-click (metamorphic-event)(opt)}]
  ;  :orientation (keyword)(opt)
  ;   :horizontal, :vertical
  ;   Default: :horizontal
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
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
