
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.08
; Description:
; Version: v0.7.6
; Compatibility: x4.3.9



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
  ; @param (map) view-props
  ;
  ; @return (map)
  [bar-id view-props]
  (engine/element-attributes bar-id view-props))

(defn- menu-item-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) view-props
  ;  {:selected-content-id (keyword)(opt)}
  ; @param (map) item-props
  ;  {:content-id (keyword)(opt)
  ;   :href (string)(opt)
  ;   :on-click (metamorphic-event)(opt)
  ;   :on-mouse-over (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ;  {:data-selected (boolean)
  ;   :on-click (function)
  ;   :on-focus (function)
  ;   :on-mouse-over (function)}
  [bar-id {:keys [selected-content-id]} {:keys [color content-id href on-click on-mouse-over]}]
  (cond-> {:data-color    (a/dom-value color)
           :data-selected (= selected-content-id content-id)
           :on-mouse-up   (engine/blur-element-function bar-id)}
          (some? href) (assoc :href href)
          (nil?  href) (merge {:on-click      #(a/dispatch-some on-click)
                               :on-focus      #(a/dispatch-some on-mouse-over)
                               :on-mouse-over #(a/dispatch-some on-mouse-over)})))



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
  ;  {:font-size (keyword)
  ;   :horizontal-align (keyword)
  ;   :layout (keyword)
  ;   :menu-items (map)
  ;   :orientation (keyword)}
  [{:keys [orientation] :as bar-props}]
  (merge {:font-size   :s
          :layout      :row
          :orientation :horizontal}
         (if-not (= orientation :vertical)
                 {:horizontal-align :center})
         (param bar-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ;
  ; @return (map)
  [db [_ bar-id]]
  (r engine/get-element-view-props db bar-id))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sensor-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) view-props
  ; @param (map) item-props
  ;  {:label (metamorphic-content)}
  ;
  ; @return (hiccup)
  [bar-id view-props {:keys [label] :as item-props}]
  ; XXX#7605
  ; A sensor-item is button elem, hogy billentyűzetről is vezérelhető legyen.
  [:button.x-menu-bar--sensor-item
    (menu-item-attributes bar-id view-props item-props)
    [components/content {:content label}]])

(defn- button-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) view-props
  ; @param (map) item-props
  ;  {:label (metamorphic-content)}
  ;
  ; @return (hiccup)
  [bar-id view-props {:keys [label] :as item-props}]
  [:button.x-menu-bar--button-item
    (menu-item-attributes bar-id view-props item-props)
    [components/content {:content label}]])

(defn- anchor-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) view-props
  ; @param (map) item-props
  ;  {:label (metamorphic-content)}
  ;
  ; @return (hiccup)
  [bar-id view-props {:keys [label] :as item-props}]
  [:a.x-menu-bar--anchor-item
    (menu-item-attributes bar-id view-props item-props)
    [components/content {:content label}]])

(defn- menu-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) view-props
  ; @param (map) item-props
  ;  {:href (string)(opt)
  ;   :on-click (metamorphic-event)(opt)}
  ;
  ; @return (hiccup)
  [bar-id view-props {:keys [href on-click] :as item-props}]
  (cond (some? href)     [anchor-item bar-id view-props item-props]
        (some? on-click) [button-item bar-id view-props item-props]
        (nil?  on-click) [sensor-item bar-id view-props item-props]))

(defn- menu-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) view-props
  ;  {:menu-items (maps in vector)}
  ;
  ; @return (hiccup)
  [bar-id {:keys [menu-items] :as view-props}]
  ; XXX#5406
  ; A {:orientation :horizontal} menük esetén az overflow-x: scroll tulajdonság
  ; és a display: flex tulajdonság kizárólag akkor használhatók egyszerre
  ; (hibamentesen), ha scroll-container elem (.x-menu-bar--items)
  ; szélessége nem nagyobb, mint a benne lévő elemek összes szélessége.
  (reduce #(vector/conj-item %1 [menu-item bar-id view-props %2])
           [:div.x-menu-bar--items]
           (param menu-items)))

(defn menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [bar-id view-props]
  [:div.x-menu-bar (menu-bar-attributes bar-id view-props)
                   [menu-items bar-id view-props]])

(defn view
  ; @param (keyword)(opt) bar-id
  ; @param (map) bar-props
  ;  {:class (string or vector)(opt)
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center
  ;    (XXX#5406 overflow-x: scroll & {:horizontal-align :space-between} nem lehetséges)
  ;    Only w/ {:orientation :horizontal}
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :menu-items (maps in vector)
  ;    [{:color (keyword)(opt)
  ;       :primary, :secondary, :default, :muted, :highlight
  ;       Default: :default
  ;      :href (string)(opt)
  ;       XXX#7004
  ;       A {:href "..."} tulajdonság használata esetén a menu elemek [:a] elemként
  ;       renderelődnek és az {:on-click ...} valamint az {:on-mouse-over ...}
  ;       tulajdonságok figyelmen kívűl hagyódnak!
  ;      :icon (keyword)(opt)
  ;      :icon-family (keyword)(opt)
  ;       :material-icons-filled, :material-icons-outlined
  ;       Default: :material-icons-filled
  ;      :label (metamorphic-content)
  ;      :on-click (metamorphic-event)(opt)
  ;      :on-mouse-over (metamorphic-event)(opt)}]
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
   [view nil bar-props])

  ([bar-id bar-props]
   (let [bar-id    (a/id   bar-id)
         bar-props (a/prot bar-props bar-props-prototype)]
        [menu-bar bar-id bar-props])))
