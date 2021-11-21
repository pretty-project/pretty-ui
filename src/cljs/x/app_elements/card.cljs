
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
              [mid-fruits.css            :as css]
              [mid-fruits.keyword        :as keyword]
              [mid-fruits.logical        :refer [nonfalse?]]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (px)
(def DEFAULT-HEADER-OFFSET 47)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- card-header-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {:header (map)
  ;   {:offset (px)(opt)
  ;    :sticky? (boolean)(opt)}}
  ;
  ; @result (map)
  ;  {:data-sticky (boolean)
  ;   :style (map)
  ;    {:top (string)}}
  [_ {:keys [header]}]
  (if (get header :sticky?)
      (let [offset (get header :offset)]
           {:data-sticky (param true)
            :style {:top (css/px offset)}})))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) card-props
  ;  {:header (map)(opt)
  ;   {:sticky? (boolean)(opt)}}
  ;
  ; @return (map)
  ;  {:horizontal-align (keyword)
  ;   :min-width (keyword)}
  [{:keys [header] :as card-props}]
  (merge {:horizontal-align :center
          :min-width        :xxs}
         (param card-props)
         (if (get header :sticky?)
             {:header (merge {:offset DEFAULT-HEADER-OFFSET} header)})))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  [card-id {:keys [header] :as card-props}]
  [:div.x-card--header
    (card-header-attributes card-id card-props)
    [components/content     card-id header]])

(defn- card-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {:expanded? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [card-id {:keys [body expanded?] :as card-props}]
  (if (nonfalse? expanded?)
      [:div.x-card--body [components/content card-id body]]))

(defn- button-card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {:header (map)(opt)
  ;   :on-click (metamorphic-event)(opt)}
  ;
  ; @return (hiccup)
  [card-id {:keys [header on-click] :as card-props}]
  [:button.x-card (engine/element-attributes card-id card-props
                                             {:on-click   #(a/dispatch on-click)
                                              :on-mouse-up (engine/blur-element-function card-id)})
                  [:div.x-card--structure
                    [card-body card-id card-props]
                    (if (some? header)
                        [card-header card-id card-props])]
                  ; XXX#0093
                  ; A card elem sarkai border-radius tulajdonsággal vannak lekerekítve, amiből
                  ; a {position: sticky} card-header alsó sarkai kilógnának, amikor a card-header
                  ; lecsúszik a card elem aljáig.
                  ; Azért szükséges a card-tail spacert alkalmazni, hogy a {position: sticky} card-header
                  ; ne tudjon a card elem aljái lecsúszni.
                  ; {overflow: hidden} tulajdonsággal nem lehet eltűntetni a card-header kilógó sarkait,
                  ; mert {overflow: hidden} elemben nem működne a {position: sticky} tulajdonság.
                  [:div.x-card--tail]
                  [engine/element-badge card-id card-props]])

(defn- static-card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {:header (map)(opt)}
  ;
  ; @return (hiccup)
  [card-id {:keys [header] :as card-props}]
  [:div.x-card (engine/element-attributes card-id card-props)
               [:div.x-card--structure
                 [card-body card-id card-props]
                 (if (some? header)
                     [card-header card-id card-props])]
               ; XXX#0093
               [:div.x-card--tail]
               [engine/element-badge    card-id card-props]
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
        (nil?  on-click)      [static-card card-id card-props]))

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
  ;  {:badge-color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success
  ;   :body (map)
  ;    {:content (metamorphic-content)(opt)
  ;     :content-props (map)(opt)
  ;     :subscriber (subscription vector)(opt)}
  ;   :class (string or vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :expandable? (boolean)(opt)
  ;    TODO ...
  ;    Default: false
  ;   :expanded? (boolean)(opt)
  ;    Default: false
  ;    Only w/ {:expandable? true}
  ;   :ghost-view? (boolean)(opt)
  ;    Default: false
  ;   :header (map)(opt)
  ;    {:content (metamorphic-content)
  ;     :content-props (map)(opt)
  ;     :offset (px)(opt)
  ;      Default: DEFAULT-HEADER-OFFSET
  ;     :sticky? (boolean)(opt)
  ;      Default: false
  ;     :subscriber (subscription-vector)(opt)}
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center
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
