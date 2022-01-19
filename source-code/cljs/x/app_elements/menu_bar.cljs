
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.08
; Description:
; Version: v1.0.4
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.menu-bar
    (:require [mid-fruits.candy          :refer [param return]]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
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
                         :on-mouse-up   (engine/blur-element-function bar-id)}
                        (some? active?) (assoc :data-active (boolean active?)))
                ; If menu-item is NOT disabled ...
                (cond-> {:on-mouse-up (engine/blur-element-function bar-id)}
                        (some? href)     (assoc :href        (str        href))
                        (some? on-click) (assoc :on-click   #(a/dispatch on-click))
                        (some? active?)  (assoc :data-active (boolean    active?)))))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- bar-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) bar-props
  ;  {:orientation (keyword)(opt)
  ;    :horizontal, :vertical}
  ;
  ; @return (map)
  ;  {:horizontal-align (keyword)
  ;   :layout (keyword)
  ;   :menu-items (map)
  ;   :orientation (keyword)}
  [{:keys [orientation] :as bar-props}]
  (merge {:layout      :row
          :orientation :horizontal}
         (if-not (= orientation :vertical)
                 {:horizontal-align :left})
         (param bar-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-item-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ;  {:icon (keyword)(opt)}
  ;
  ; @return (hiccup)
  [_ _ {:keys [icon]}]
  (if icon [:div.x-menu-bar--menu-item--icon icon]))

(defn- menu-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @return (hiccup)
  [_ _ {:keys [label]}]
  (if label [:div.x-menu-bar--menu-item--label [components/content label]]))

(defn- button-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ;
  ; @return (hiccup)
  [bar-id bar-props item-props]
  [:button.x-menu-bar--menu-item (menu-item-attributes bar-id bar-props item-props)
                                 [menu-item-icon       bar-id bar-props item-props]
                                 [menu-item-label      bar-id bar-props item-props]
                                 [engine/element-badge bar-id item-props]])

(defn- anchor-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ;
  ; @return (hiccup)
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
  ;
  ; @return (hiccup)
  [bar-id bar-props {:keys [href on-click] :as item-props}]
  (cond (some? href)     [anchor-item bar-id bar-props item-props]
        (some? on-click) [button-item bar-id bar-props item-props]))

(defn- menu-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;  {:menu-items (maps in vector)}
  ;
  ; @return (hiccup)
  [bar-id {:keys [menu-items] :as bar-props}]
  ; XXX#5406
  ; A {:orientation :horizontal} menük esetén az overflow-x: scroll tulajdonság
  ; és a display: flex tulajdonság kizárólag akkor használhatók egyszerre
  ; (hibamentesen), ha scroll-container elem (.x-menu-bar--items)
  ; szélessége nem nagyobb, mint a benne lévő elemek összes szélessége.
  (reduce #(conj %1 [menu-item bar-id bar-props %2])
           [:div.x-menu-bar--menu-items]
           (param menu-items)))

(defn menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;
  ; @return (hiccup)
  [bar-id bar-props]
  [:div.x-menu-bar (menu-bar-attributes bar-id bar-props)
                   [menu-items          bar-id bar-props]])

(defn element
  ; @param (keyword)(opt) bar-id
  ; @param (map) bar-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :left
  ;    (XXX#5406 overflow-x: scroll & {:horizontal-align :space-between} nem lehetséges)
  ;    Only w/ {:orientation :horizontal}
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :menu-items (maps in vector)
  ;    [{:active? (boolean)(opt)
  ;       Default: false
  ;      :badge-color (keyword)(opt)
  ;       :primary, :secondary, :success, :warning
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
  ;  [elements/menu-bar {...}]
  ;
  ; @usage
  ;  [elements/menu-bar :my-menu-bar {...}]
  ;
  ; @return (component)
  ([bar-props]
   [element (a/id) bar-props])

  ([bar-id bar-props]
   (let [bar-props (bar-props-prototype bar-props)]
        [menu-bar bar-id bar-props])))
