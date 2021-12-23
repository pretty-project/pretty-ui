
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.19
; Description:
; Version: v0.3.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.label
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) label-props
  ;  {:icon (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :font-size (keyword)
  ;   :font-weight (keyword)
  ;   :horizontal-align (keyword)
  ;   :selectable? (boolean)}
  [{:keys [icon] :as label-props}]
  (merge {:color            :default
          :font-size        :s
          :font-weight      :bold
          :horizontal-align :left
          :layout           :row
          :selectable?      false}
         (if (some? icon) {:icon-family :material-icons-filled})
         (param label-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;  {:icon (keyword)
  ;   :icon-family (keyword)}
  ;
  ; @return (hiccup or nil)
  [_ {:keys [icon icon-family]}]
  [:i.x-label--icon {:data-icon-family icon-family}
                    (param icon)])

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
  ;  {:icon (keyword)}
  ;
  ; @return (hiccup)
  [label-id {:keys [icon] :as label-props}]
  [:div.x-label (engine/element-attributes label-id label-props)
                (if (some? icon) [label-icon label-id label-props])
                [label-body label-id label-props]
                (if (some? icon) [:div.x-label--icon-placeholder])])

(defn element
  ; XXX#0439
  ; A label elemen megjelenített szöveg nem törik meg akkor sem, ha nincs elegendő hely.
  ; A text elemen megjelenített szöveg megtörik, ha nincs elegendő hely.
  ;
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :color (keyword)(opt)
  ;    :default, :highlight, :muted, :primary, :secondary, :success, :warning    
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
  ;   :icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;    Only w/ {:icon ...}
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
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
   [element (a/id) label-props])

  ([label-id label-props]
   (let [label-props (a/prot label-props label-props-prototype)]
        [label label-id label-props])))
