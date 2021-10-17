
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.18
; Description:
; Version: v0.3.0
; Compatibility: x4.3.9



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
  ;  {:request-id (keyword)(opt)
  ;   :stretch-orientation (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :container-stretch-orientation (keyword)
  ;   :horizontal-align (keyword)
  ;   :min-width (keyword)
  ;   :status-animation? (boolean)}
  [{:keys [request-id stretch-orientation] :as card-props}]
  (merge {:color            :primary
          :horizontal-align :center
          :min-width        :xxs

          ; A stretch-orientation tulajdonságot szükséges az element-container komponens
          ; számára is átadni, hogy alkalmazkodni tudjon a környezethez az elem.
          :container-stretch-orientation stretch-orientation}

         (if (some? request-id) {:status-animation? true})
         (param card-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ;
  ; @return (map)
  [db [_ card-id]]
  (r engine/get-element-view-props db card-id))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- card-header-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) view-props
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
  ; @param (map) view-props
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
  ; @param (map) view-props
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
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [card-id view-props]
  [:div.x-card--header
    [card-header-icon          card-id view-props]
    [card-header-label         card-id view-props]
    [card-header-expand-button card-id view-props]])

(defn- card-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) view-props
  ;  {:expanded? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [card-id {:keys [expanded?] :as view-props}]
  (if (nonfalse? expanded?)
      (let [content-props (components/extended-props->content-props view-props)]
           [:div.x-card--body [components/content card-id content-props]])))

(defn- button-card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) view-props
  ;  {:on-click (metamorphic-event)(opt)}
  ;
  ; @return (hiccup)
  [card-id {:keys [on-click] :as view-props}]
  [:button.x-card (engine/element-attributes card-id view-props
                                             {:on-click   #(a/dispatch on-click)
                                              :on-mouse-up (engine/blur-element-function card-id)})
                  (if (engine/element-props->render-element-header? view-props)
                      [card-header card-id view-props])
                  [card-body card-id view-props]])

(defn- static-card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [card-id view-props]
  [:div.x-card (engine/element-attributes card-id view-props)
               (if (engine/element-props->render-element-header? view-props)
                   [card-header card-id view-props])
               [card-body card-id view-props]])

(defn- ghost-card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [card-id view-props]
  [:div.x-card (engine/element-attributes card-id view-props)])

(defn- card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) view-props
  ;  {:ghost-view? (boolean)(opt)
  ;   :on-click (metamorphic-event)(opt)}
  ;
  ; @return (hiccup)
  [card-id {:keys [ghost-view? on-click] :as view-props}]
  (cond (boolean ghost-view?)
        [ghost-card card-id view-props]
        (some? on-click)
        [button-card card-id view-props]
        (nil? on-click)
        [static-card card-id view-props]))

(defn view
  ; XXX#8711
  ; A card elem az x.app-components.api/content komponens használatával jeleníti meg
  ; a számára :content és :context-surface tulajdonságként átadott tartalmat.
  ; A card elemnél alkalmazott :content, :content-props és :subscriber tulajdonságok
  ; használatának leírását az x.app-components.api/content komponens dokumentációjában találod.
  ;
  ; @param (keyword)(opt) card-id
  ; @param (map) card-props
  ;  XXX#3240
  ;  {:color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success, :muted, :default
  ;    Default: :primary
  ;   :class (string or vector)(opt)
  ;   :content (metamorphic-content)(opt)
  ;   :content-props (map)(opt)
  ;   :context-surface (map)(constant)(opt)
  ;    {:content (metamorphic-content)
  ;     :content-props (map)(opt)
  ;     :subscriber (subscription vector)(opt)}
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :expandable? (boolean)(opt)
  ;    Default: false
  ;   :expanded? (boolean)(opt)
  ;    Default: false
  ;    Only w/ {:expandable? true}
  ;   :ghost-view? (boolean)(opt)
  ;    Default: false
  ;   :highlighted? (boolean)(opt)
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
  ;   :request-id (keyword)(constant)(opt)
  ;   :status-animation? (boolean)(opt)
  ;    Default: true
  ;    Only w/ {:request-id ...}
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
  ; @usage
  ;  (defn my-context-surface [card-id card-props] "Context surface")
  ;  [elements/card
  ;    :my-card
  ;    {:context-surface {:content       #'my-context-surface
  ;                       :content-props card-props}
  ;     :stickers [{:icon     :more_vert
  ;                 :on-click [:x.app-elements/render-context-surface! :my-card]}]}]
  ;
  ; @return (component)
  ([card-props]
   [view nil card-props])

  ([card-id card-props]
   (let [card-id    (a/id   card-id)
         card-props (a/prot card-props card-props-prototype)]
        [engine/container card-id
          {:base-props card-props
           :component  card
           :subscriber [::get-view-props card-id]}])))
