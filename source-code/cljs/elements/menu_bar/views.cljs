
(ns elements.menu-bar.views
    (:require [elements.element.views       :as element.views]
              [elements.menu-bar.helpers    :as menu-bar.helpers]
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
  ; {:font-size (keyword)}
  ; @param (map) item-props
  ; {:label (metamorphic-content)(opt)}
  [_ {:keys [font-size]} {:keys [label]}]
  (if label [:div.e-menu-bar--menu-item--label {:data-font-size   font-size
                                                :data-font-weight :extra-bold
                                                :data-line-height :block}
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
                                 [menu-item-label                       bar-id bar-props item-props]
                                 [element.views/element-badge           bar-id           item-props]])

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
  ; A {:orientation :horizontal} menük esetén az overflow-x: scroll tulajdonság
  ; és a display: flex tulajdonság kizárólag akkor használhatók egyszerre
  ; (hibamentesen), ha scroll-container elem (.e-menu-bar--body)
  ; szélessége nem nagyobb, mint a benne lévő elemek összes szélessége.
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
  ;   (XXX#5406 overflow-x: scroll & {:horizontal-align :space-between} nem lehetséges)
  ;   W/ {:orientation :horizontal}
  ;  :font-size (keyword)(opt)
  ;   :xs, :s
  ;   Default: :s
  ;  :height (keyword)(opt)
  ;   :m, :l, :xl, :xxl
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
  ;      :primary, :secondary, :success, :warning
  ;     :badge-content (metamorphic-content)(opt)
  ;     :disabled? (boolean)(opt)
  ;      Default: false
  ;     :href (string)(opt)
  ;      XXX#7004
  ;      A {:href "..."} tulajdonság használata esetén a menu elemek [:a] elemként
  ;      renderelődnek és az {:on-click ...} valamint az {:on-mouse-over ...}
  ;      tulajdonságok figyelmen kívűl hagyódnak!
  ;     :icon (keyword)(opt)
  ;     :icon-family (keyword)(opt)
  ;      :material-icons-filled, :material-icons-outlined
  ;      Default: :material-icons-filled
  ;     :label (metamorphic-content)(opt)
  ;     :on-click (metamorphic-event)(opt)}]
  ;  :orientation (keyword)(opt)
  ;   :horizontal, :vertical
  ;   Default: :horizontal
  ;  :outdent (map)(opt)
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
