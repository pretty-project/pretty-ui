
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
  ;  {:content (metamorphic-content)}
  [card-id {:keys [content]}]
  [:div.x-card--body [components/content card-id content]])

(defn- button-card
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
               [engine/element-badge      card-id card-props]
               [engine/element-stickers   card-id card-props]])

(defn- card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {:on-click (metamorphic-event)(opt)}
  [card-id {:keys [on-click] :as card-props}]
  (cond (some? on-click) [button-card card-id card-props]
        (nil?  on-click) [static-card card-id card-props]))

(defn element
  ; @param (keyword)(opt) card-id
  ; @param (map) card-props
  ;  XXX#3240
  ;  {:badge-color (keyword)(opt)
  ;    :primary, :secondary, :success, :warning
  ;   :badge-content (metamorphic-content)(opt)
  ;   :content (metamorphic-content)(opt)
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
