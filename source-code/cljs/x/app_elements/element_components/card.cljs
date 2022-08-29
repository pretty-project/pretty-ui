
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.card
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]
              [x.app-environment.api     :as environment]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) card-props
  ;
  ; @return (map)
  ;  {:border-radius (keyword)
  ;   :horizontal-align (keyword)
  ;   :min-width (keyword)}
  [card-props]
  (merge {:border-radius    :s
          :horizontal-align :center
          :min-width        :xxs}
         (param card-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- card-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {:content (metamorphic-content)}
  [card-id {:keys [content]}]
  [:div.x-card--body [components/content card-id content]])

(defn- toggle-card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {:on-click (metamorphic-event)(opt)}
  [card-id {:keys [on-click] :as card-props}]
  [:button.x-card (engine/element-attributes card-id card-props
                                             {:on-click    #(a/dispatch on-click)
                                              :on-mouse-up #(environment/blur-element!)})
                  [card-content         card-id card-props]
                  [engine/element-badge card-id card-props]])

(defn- static-card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  [card-id card-props]
  [:div.x-card (engine/element-attributes card-id card-props)
               [card-content              card-id card-props]
               [engine/element-badge      card-id card-props]])

(defn- card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {:on-click (metamorphic-event)(opt)}
  [card-id {:keys [on-click] :as card-props}]
  (cond (some? on-click) [toggle-card card-id card-props]
        (nil?  on-click) [static-card card-id card-props]))

(defn element
  ; @param (keyword)(opt) card-id
  ; @param (map) card-props
  ;  XXX#3240
  ;  {:background-color (keyword or string)(opt)
  ;    :highlight, :muted, :none, :primary, :secondary, :success, :warning
  ;    Default: :none
  ;   :badge-color (keyword or string)(opt)
  ;    :primary, :secondary, :success, :warning
  ;   :badge-content (metamorphic-content)(opt)
  ;   :border-color (keyword or string)(opt)
  ;    :highlight, :muted, :none, :primary, :secondary, :success, :warning
  ;    Default: :none
  ;   :border-radius (keyword)(opt)
  ;    :none, :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :content (metamorphic-content)(opt)
  ;   :class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :horizontal-align (keyword)(opt)
  ;    :center, :left, :right
  ;    Default: :center
  ;   :hover-color (keyword or string)(opt)
  ;    :highlight, :muted, :none, :primary, :secondary, :success, :warning
  ;    Default: :none
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :xxs
  ;   :on-click (metamorphic-event)(opt)
  ;   :stretch-orientation (keyword)(opt)
  ;    :horizontal, :vertical, :both, :none
  ;    Default: :vertical
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/card {...}]
  ;
  ; @usage
  ;  [elements/card :my-card {...}]
  ([card-props]
   [element (a/id) card-props])

  ([card-id card-props]
   (let [card-props (card-props-prototype card-props)]
        [card card-id card-props])))
