
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.18
; Description:
; Version: v0.6.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.card
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) card-props
  ;
  ; @return (map)
  ;  {:horizontal-align (keyword)
  ;   :min-width (keyword)}
  [card-props]
  (merge {:horizontal-align :center
          :min-width        :xxs}
         (param card-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- card-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (hiccup)
  [card-id card-props]
  (let [content-props (components/extended-props->content-props card-props)]
       [:div.x-card--body [components/content card-id content-props]]))

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
                  [card-content         card-id card-props]
                  [engine/element-badge card-id card-props]])

(defn- static-card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (hiccup)
  [card-id card-props]
  [:div.x-card (engine/element-attributes card-id card-props)
               [card-content              card-id card-props]
               [engine/element-badge      card-id card-props]
               [engine/element-stickers   card-id card-props]])

(defn- card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {:on-click (metamorphic-event)(opt)}
  ;
  ; @return (hiccup)
  [card-id {:keys [on-click] :as card-props}]
  (cond (some? on-click) [button-card card-id card-props]
        (nil?  on-click) [static-card card-id card-props]))

(defn element
  ; XXX#8711
  ; A card elem az x.app-components.api/content komponens használatával jeleníti meg
  ; a számára :content tulajdonságként átadott tartalmat.
  ; A card elemnél alkalmazott :content, :content-props és :subscriber tulajdonságok
  ; használatának leírását az x.app-components.api/content komponens dokumentációjában találod.
  ;
  ; @param (keyword)(opt) card-id
  ; @param (map) card-props
  ;  XXX#3240
  ;  {:badge-color (keyword)(opt)
  ;    :primary, :secondary, :success, :warning
  ;   :content (metamorphic-content)(opt)
  ;   :content-props (map)(opt)
  ;   :class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :xxs
  ;   :on-click (metamorphic-event)(opt)
  ;   :stickers (maps in vector)(opt)
  ;    [{:disabled? (boolean)(opt)
  ;       Default: false
  ;      :icon (keyword)
  ;      :icon-family (keyword)(opt)
  ;       :material-icons-filled, :material-icons-outlined
  ;       Default: :material-icons-filled
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
   [element (a/id) card-props])

  ([card-id card-props]
   (let [card-props (card-props-prototype card-props)]
        [card card-id card-props])))
