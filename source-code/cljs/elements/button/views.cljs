
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.button.views
    (:require [elements.button.helpers    :as button.helpers]
              [elements.button.presets    :as button.presets]
              [elements.button.prototypes :as button.prototypes]
              [elements.element.helpers   :as element.helpers]
              [elements.engine.api        :as engine]
              [mid-fruits.candy           :refer [param]]
              [mid-fruits.random          :as random]
              [reagent.api                :as reagent]
              [x.app-components.api       :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:label (metamorphic-content)(opt)}
  [_ {:keys [label]}]
  (if label [:div.e-button--label [x.components/content label]]))

(defn- button-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:icon (keyword)(opt)}
  [button-id {:keys [icon] :as button-props}]
  [:i.e-button--icon (button.helpers/button-icon-attributes button-id button-props)
                     (param icon)])

(defn- button-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:icon (keyword)(opt)}
  [button-id {:keys [icon icon-position] :as button-props}]
  [:button.e-button--body (button.helpers/button-body-attributes button-id button-props)
                          (if icon (case icon-position :left  [button-icon button-id button-props]
                                                       :right [:div.e-button--icon-placeholder]))
                          [button-label button-id button-props]
                          (if icon (case icon-position :left  [:div.e-button--icon-placeholder]
                                                       :right [button-icon button-id button-props]))
                          [engine/element-badge button-id button-props]])

(defn- button-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [:div.e-button (button.helpers/button-attributes button-id button-props)
                 [button-body                      button-id button-props]])

(defn- button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (button.helpers/button-did-mount-f    button-id button-props))
                       :component-will-unmount (fn [_ _] (button.helpers/button-will-unmount-f button-id button-props))
                       :component-did-update   (fn [%]   (button.helpers/button-did-update-f   button-id %))
                       :reagent-render         (fn [_ button-props] [button-structure button-id button-props])}))

(defn element
  ; XXX#0714
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ;  {:badge-color (keyword or string)(opt)
  ;    :primary, :secondary, :success, :warning
  ;   :badge-content (metamorphic-content)(opt)
  ;   :background-color (keyword or string)(opt)
  ;    :highlight, :muted, :none, :primary, :secondary, :success, :warning
  ;    Default: :none
  ;   :border-color (keyword or string)(opt)
  ;    :highlight, :invert, :muted, :none, :primary, :secondary, :success, :warning
  ;    Default: :none
  ;   :border-radius (keyword)(opt)
  ;    :none, :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :class (keyword or keywords in vector)(opt)
  ;   :color (keyword or string)(opt)
  ;    :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;    Default: :default
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :inherit
  ;    Default: :s
  ;   :font-weight (keyword)(opt)
  ;    :bold, :extra-bold, :inherit
  ;    Default: :bold
  ;   :horizontal-align (keyword)(opt)
  ;    :center, :left, :right
  ;    Default: :center
  ;   :hover-color (keyword or string)(opt)
  ;    :highlight, :muted, :none, :primary, :secondary, :success, :warning
  ;    Default: :none
  ;   :icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;   :icon-position (keyword)(opt)
  ;    :left, :right
  ;    Default: :left
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :keypress (map)(opt)
  ;    {:key-code (integer)
  ;     :required? (boolean)(opt)
  ;      Default: false}
  ;   :label (metamorphic-content)(opt)
  ;   :on-click (metamorphic handler)(opt)
  ;   :on-mouse-over (metamorphic handler)(opt)
  ;   :preset (keyword)(opt)
  ;   :stop-propagation? (boolean)(opt)
  ;    Default: false
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [button {...}]
  ;
  ; @usage
  ;  [button :my-button {...}]
  ;
  ; @usage
  ;  [button {:keypress {:key-code 13} :on-click [:my-event]}]
  ([button-props]
   [element (random/generate-keyword) button-props])

  ([button-id button-props]
   (let [button-props (element.helpers/apply-preset button.presets/BUTTON-PROPS-PRESETS button-props)
         button-props (button.prototypes/button-props-prototype button-props)]
        [button button-id button-props])))
