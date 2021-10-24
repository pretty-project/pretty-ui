
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.19
; Description:
; Version: v0.3.2
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.label
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.keyword        :as keyword]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) label-props
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :font-size (keyword)
  ;   :font-weight (keyword)
  ;   :horizontal-align (keyword)}
  [label-props]
  (merge {:color            :default
          :font-size        :s
          :font-weight      :bold
          :horizontal-align :left
          :layout           :row}
         (param label-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;  {:icon (keyword) Material icon class}
  ;
  ; @return (hiccup or nil)
  [_ {:keys [icon]}]
  [:i.x-label--icon (keyword/to-dom-value icon)])

(defn- label-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;  {:content (metamorphic-content)}
  ;
  ; @return (hiccup)
  [_ label-props]
  (let [content-props (components/extended-props->content-props label-props)]
       [:div.x-label--body [components/content content-props]]))

(defn- label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;  {:icon (keyword) Material icon class}
  ;
  ; @return (hiccup)
  [label-id {:keys [icon] :as label-props}]
  [:div.x-label
    (engine/element-attributes label-id label-props)
    (if (some? icon)
        [label-icon label-id label-props])
    [label-body label-id label-props]
    (if (some? icon)
        [:div.x-label--icon-placeholder])])

(defn view
  ; XXX#0439
  ; A label elemen megjelenített szöveg nem törik meg akkor sem, ha nincs elegendő hely.
  ; A text elemen megjelenített szöveg megtörik, ha nincs elegendő hely.
  ; A text elemen lehetséges kijelölni a szöveget, a label elemen nem lehetséges.
  ;
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ;  {:class (string or vector)(opt)
  ;   :color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success, :muted, :highlight, :default
  ;    Default: :default
  ;   :content (metamorphic-content)
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :font-weight (keyword)(opt)
  ;    :normal, :bold, extra-bold
  ;    Default :bold
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :left
  ;   :icon (keyword)(opt) Material icon class
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :selectable? (boolean)(opt)
  ;    Default: false
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/label {...}]
  ;
  ; @usage
  ;  [elements/label :my-label {...}]
  ;
  ; @return (component)
  ([label-props]
   [view nil label-props])

  ([label-id label-props]
   (let [label-id    (a/id label-id)
         label-props (a/prot label-props label-props-prototype)]
        [label label-id label-props])))
