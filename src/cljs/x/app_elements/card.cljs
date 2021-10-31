
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.18
; Description:
; Version: v0.3.8
; Compatibility: x4.4.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.card
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.keyword        :as keyword]
              [mid-fruits.logical        :refer [nonfalse?]]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) card-props
  ;  {:stretch-orientation (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:border-color (keyword)
  ;   :horizontal-align (keyword)
  ;   :min-width (keyword)}
  [{:keys [stretch-orientation] :as card-props}]
  (merge {:border-color     :highlight
          :horizontal-align :center
          :min-width        :xxs}
         (param card-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- card-header-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {:icon (keyword)(opt) Material icon class}
  ;
  ; @return (hiccup or nil)
  [_ {:keys [icon]}]
  (if (some? icon)
      [:i.x-card--header--icon (keyword/to-dom-value icon)]))

(defn- card-header-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @return (hiccup or nil)
  [card-id {:keys [label]}]
  (if (some? label)
      [:div.x-card--header--label [components/content {:content label}]]))

(defn- card-header-expand-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {:expandable? (boolean)(opt)
  ;   :expanded? (boolean)(opt)}
  ;
  ; @return (hiccup or nil)
  [card-id {:keys [expandable? expanded?]}]
  (if (boolean expandable?)
      [:button.x-card--header--expand-button
        (if (nonfalse? expanded?)
            [:i.x-card--header--expand-button-icon
              {:on-click   #(a/dispatch [:x.app-elements/compress-element! card-id])
               :on-mouse-up (engine/blur-element-function card-id)}
              (keyword/to-dom-value :expand_less)]
            [:i.x-card--header--expand-button-icon
              {:on-click   #(a/dispatch [:x.app-elements/expand-element!   card-id])
               :on-mouse-up (engine/blur-element-function card-id)}
              (keyword/to-dom-value :expand_more)])]))

(defn- card-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (hiccup)
  [card-id card-props]
  [:div.x-card--header
    [card-header-icon          card-id card-props]
    [card-header-label         card-id card-props]
    [card-header-expand-button card-id card-props]])

(defn- card-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {:expanded? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [card-id {:keys [expanded?] :as card-props}]
  (if (nonfalse? expanded?)
      (let [content-props (components/extended-props->content-props card-props)]
           [:div.x-card--body [components/content card-id content-props]])))

(defn- button-card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {:on-click (metamorphic-event)(opt)}
  ;
  ; @return (hiccup)
  [card-id {:keys [on-click] :as card-props}]
  [:button.x-card (engine/element-attributes card-id card-props
                                             {:on-click   #(a/dispatch on-click)
                                              :on-mouse-up (engine/blur-element-function card-id)})
                  (if (engine/element-props->render-element-header? card-props)
                      [card-header card-id card-props])
                  [card-body card-id card-props]])

(defn- static-card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (hiccup)
  [card-id card-props]
  [:div.x-card (engine/element-attributes card-id card-props)
               (if (engine/element-props->render-element-header? card-props)
                   [card-header card-id card-props])
               [card-body               card-id card-props]
               [engine/element-stickers card-id card-props]])

(defn- ghost-card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (hiccup)
  [card-id card-props]
  [:div.x-card (engine/element-attributes card-id card-props)])

(defn- card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {:ghost-view? (boolean)(opt)
  ;   :on-click (metamorphic-event)(opt)}
  ;
  ; @return (hiccup)
  [card-id {:keys [ghost-view? on-click] :as card-props}]
  (cond (boolean ghost-view?) [ghost-card  card-id card-props]
        (some? on-click)      [button-card card-id card-props]
        (nil? on-click)       [static-card card-id card-props]))

(defn view
  ; XXX#8711
  ; A card elem az x.app-components.api/content komponens használatával jeleníti meg
  ; a számára :content tulajdonságként átadott tartalmat.
  ; A card elemnél alkalmazott :content, :content-props és :subscriber tulajdonságok
  ; használatának leírását az x.app-components.api/content komponens dokumentációjában találod.
  ;
  ; @param (keyword)(opt) card-id
  ; @param (map) card-props
  ;  XXX#3240
  ;  {:border-color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success, :muted, :default
  ;    Default: :highlight
  ;   :class (string or vector)(opt)
  ;   :content (metamorphic-content)(opt)
  ;   :content-props (map)(opt)
  ;   :expandable? (boolean)(opt)
  ;    Default: false
  ;   :expanded? (boolean)(opt)
  ;    Default: false
  ;    Only w/ {:expandable? true}
  ;   :ghost-view? (boolean)(opt)
  ;    Default: false
  ;   :highlighted? (boolean)(opt)
  ;    Default: false
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center
  ;   :icon (keyword)(opt) Material icon class
  ;   :label (metamorphic-content)(opt)
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :xxs
  ;   :on-click (metamorphic-event)(opt)
  ;   :stickers (maps in vector)(opt)
  ;    [{:disabled? (boolean)(opt)
  ;       Default: false
  ;      :icon (keyword) Material icon class
  ;      :on-click (metamorphic-event)(opt)
  ;      :tooltip (metamorphic-content)(opt)}]
  ;   :stretch-orientation (keyword)(opt)
  ;    :horizontal, :vertical, :both, :none
  ;    Default: :vertical
  ;   :style (map)(opt)
  ;   :subscriber (subscription vector)(opt)}
  ;
  ; @usage
  ;  XXX#7610
  ;  A card elemen megjelenő tartalom használatának leírását a blank elem dokumentációjában találod.
  ;
  ; @usage
  ;  [elements/card {...}]
  ;
  ; @usage
  ;  [elements/card :my-card {...}]
  ;
  ; @return (component)
  ([card-props]
   [view nil card-props])

  ([card-id card-props]
   (let [card-id    (a/id   card-id)
         card-props (a/prot card-props card-props-prototype)]
        [card card-id card-props])))
