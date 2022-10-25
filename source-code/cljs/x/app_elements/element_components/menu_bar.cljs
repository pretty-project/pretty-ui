
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.menu-bar
    (:require [mid-fruits.candy          :refer [param return]]
              [mid-fruits.random         :as random]
              [mid-fruits.vector         :as vector]
              [re-frame.api              :as r]
              [x.app-components.api      :as components]
              [x.app-environment.api     :as environment]
              [x.app-elements.engine.api :as engine]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;
  ; @return (map)
  [bar-id bar-props]
  (engine/element-attributes bar-id bar-props))

(defn- menu-item-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ;  {:active? (boolean)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :href (string)(opt)
  ;   :on-click (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ;  {:data-active (boolean)
  ;   :data-disabled (boolean)
  ;   :href (string)
  ;   :on-click (function)
  ;   :on-mouse-up (function)}
  [bar-id _ {:keys [active? disabled? href on-click]}]
  (if disabled? ; If menu-item is disabled ...
                (cond-> {:data-disabled true
                         :on-mouse-up  #(environment/blur-element!)}
                        (some? active?) (assoc :data-active (boolean active?)))
                ; If menu-item is NOT disabled ...
                (cond-> {:on-mouse-up   #(environment/blur-element!)}
                        (some? href)     (assoc :href        (str        href))
                        (some? on-click) (assoc :on-click   #(r/dispatch on-click))
                        (some? active?)  (assoc :data-active (boolean    active?)))))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- bar-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) bar-props
  ;  {:orientation (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:font-size (keyword)
  ;   :horizontal-align (keyword)
  ;   :layout (keyword)
  ;   :menu-items (map)
  ;   :orientation (keyword)}
  [{:keys [icon orientation] :as bar-props}]
  (merge {:font-size   :s
          :layout      :row
          :orientation :horizontal}
         (if-not (= orientation :vertical)
                 {:horizontal-align :left})
         (param bar-props)))

(defn item-props-prototype
  [{:keys [icon] :as item-props}]
  (merge {}
         (if icon {:icon-family :material-icons-filled})
         (param item-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-item-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ;  {:icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)}
  [_ _ {:keys [icon icon-family]}]
  (if icon [:div.x-menu-bar--menu-item--icon {:data-icon-family icon-family} icon]))

(defn- menu-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ;  {:label (metamorphic-content)(opt)}
  [_ _ {:keys [label]}]
  (if label [:div.x-menu-bar--menu-item--label (components/content label)]))

(defn- toggle-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  [bar-id bar-props item-props]
  [:button.x-menu-bar--menu-item (menu-item-attributes bar-id bar-props item-props)
                                 [menu-item-icon       bar-id bar-props item-props]
                                 [menu-item-label      bar-id bar-props item-props]
                                 [engine/element-badge bar-id           item-props]])

(defn- anchor-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  [bar-id bar-props item-props]
  [:a.x-menu-bar--menu-item (menu-item-attributes bar-id bar-props item-props)
                            [menu-item-icon       bar-id bar-props item-props]
                            [menu-item-label      bar-id bar-props item-props]])

(defn- menu-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ;  {:href (string)(opt)
  ;   :on-click (metamorphic-event)(opt)}
  [bar-id bar-props {:keys [href on-click] :as item-props}]
  (let [item-props (item-props-prototype item-props)]
       (cond (some? href)     [anchor-item bar-id bar-props item-props]
             (some? on-click) [toggle-item bar-id bar-props item-props])))

(defn- menu-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;  {:menu-items (maps in vector)
  ;   :orientation (keyword)}
  [bar-id {:keys [menu-items orientation] :as bar-props}]
  ; XXX#5406
  ; A {:orientation :horizontal} menük esetén az overflow-x: scroll tulajdonság
  ; és a display: flex tulajdonság kizárólag akkor használhatók egyszerre
  ; (hibamentesen), ha scroll-container elem (.x-menu-bar--items)
  ; szélessége nem nagyobb, mint a benne lévő elemek összes szélessége.
  (letfn [(f [item-list item-props] (conj item-list [menu-item bar-id bar-props item-props]))]
         [:div.x-menu-bar--menu-items (case orientation :horizontal {:data-hide-scrollbar true})
                                      (reduce f [:<>] menu-items)]))

(defn menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  [bar-id bar-props]
  [:div.x-menu-bar (menu-bar-attributes bar-id bar-props)
                   [menu-items          bar-id bar-props]])

(defn element
  ; @param (keyword)(opt) bar-id
  ; @param (map) bar-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :horizontal-align (keyword)(opt)
  ;    :center, :left, :right
  ;    Default: :left
  ;    (XXX#5406 overflow-x: scroll & {:horizontal-align :space-between} nem lehetséges)
  ;    W/ {:orientation :horizontal}
  ;   :font-size (keyword)(opt)
  ;    :xs, :s
  ;    Default: :s
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :menu-items (maps in vector)
  ;    [{:active? (boolean)(opt)
  ;       Default: false
  ;      :badge-color (keyword)(opt)
  ;       :primary, :secondary, :success, :warning
  ;      :badge-content (metamorphic-content)(opt)
  ;      :disabled? (boolean)(opt)
  ;       Default: false
  ;      :href (string)(opt)
  ;       XXX#7004
  ;       A {:href "..."} tulajdonság használata esetén a menu elemek [:a] elemként
  ;       renderelődnek és az {:on-click ...} valamint az {:on-mouse-over ...}
  ;       tulajdonságok figyelmen kívűl hagyódnak!
  ;      :icon (keyword)(opt)
  ;      :icon-family (keyword)(opt)
  ;       :material-icons-filled, :material-icons-outlined
  ;       Default: :material-icons-filled
  ;      :label (metamorphic-content)(opt)
  ;      :on-click (metamorphic-event)(opt)}]
  ;   :orientation (keyword)(opt)
  ;    :horizontal, :vertical
  ;    Default: :horizontal
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [menu-bar {...}]
  ;
  ; @usage
  ;  [menu-bar :my-menu-bar {...}]
  ([bar-props]
   [element (random/generate-keyword) bar-props])

  ([bar-id bar-props]
   (let [bar-props (bar-props-prototype bar-props)]
        [menu-bar bar-id bar-props])))
