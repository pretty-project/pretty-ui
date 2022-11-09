
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.icon-button.views
    (:require [elements.button.helpers         :as button.helpers]
              [elements.engine.api             :as engine]
              [elements.element.helpers        :as element.helpers]
              [elements.icon-button.helpers    :as icon-button.helpers]
              [elements.icon-button.presets    :as icon-button.presets]
              [elements.icon-button.prototypes :as icon-button.prototypes]
              [mid-fruits.random               :as random]
              [reagent.api                     :as reagent]
              [x.app-components.api            :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon-button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:label (metamorphic-content)(opt)}
  [_ {:keys [label]}]
  (if label [:div.e-icon-button--label (x.components/content label)]))

(defn- icon-button-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:icon (keyword)
  ;   :icon-family (keyword)}
  [_ {:keys [icon icon-family]}]
  [:i.e-icon-button--icon {:data-icon-family icon-family} icon])

(defn- icon-button-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [:button.e-icon-button--body (icon-button.helpers/button-body-attributes button-id button-props)
                               [icon-button-icon                           button-id button-props]
                               [icon-button-label                          button-id button-props]
                               [engine/element-badge                       button-id button-props]])

(defn- icon-button-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id {:keys [tooltip] :as button-props}]
  [:div.e-icon-button (icon-button.helpers/button-attributes button-id button-props)
                      [icon-button-body                      button-id button-props]])

(defn- icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (button.helpers/button-did-mount-f    button-id button-props))
                       :component-will-unmount (fn [_ _] (button.helpers/button-will-unmount-f button-id button-props))
                       :component-did-update   (fn [%]   (button.helpers/button-did-update-f   button-id %))
                       :reagent-render         (fn [_ button-props] [icon-button-structure button-id button-props])}))

(defn element
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ;  {:badge-color (keyword or string)(opt)
  ;    :primary, :secondary, :success, :warning
  ;   :badge-content (metamorphic-content)(opt)
  ;   :background-color (keyword or string)(opt)
  ;    :highlight, :muted, :none, :primary, :secondary, :success, :warning
  ;    Default: :none
  ;   :border-color (keyword or string)(opt)
  ;    :highlight, :muted, :none, :primary, :secondary, :success, :warning
  ;    Default: :none
  ;   :border-radius (keyword)(opt)
  ;    :none, :xxs, :xs, :s, :m, :l, :xl
  ;    Default: :xs
  ;   :class (keyword or keywords in vector)(opt)
  ;   :color (keyword or string)(opt)
  ;    :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;    Default: :default
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :height (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl
  ;    Default: :xxl
  ;   :hover-color (keyword or string)(opt)
  ;    :highlight, :invert, :muted, :none, :primary, :secondary, :success, :warning
  ;    Default: :none
  ;   :icon (keyword)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :keypress (map)(constant)(opt)
  ;    {:key-code (integer)
  ;     :required? (boolean)(opt)
  ;      Default: false}
  ;   :label (metamorphic-content)(opt)
  ;   :on-click (metamorphic handler)(opt)
  ;   :on-mouse-over (metamorphic handler)(opt)
  ;   :preset (keyword)(opt)
  ;   :stop-propagation? (boolean)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :variant (keyword)(opt)
  ;    :placeholder
  ;   :width (keyword)(opt)
  ;    :l, :xl, :xxl, :3xl
  ;    Default: :xxl}
  ;
  ; @usage
  ;  [icon-button {...}]
  ;
  ; @usage
  ;  [icon-button :my-button {...}]
  ;
  ; @usage
  ;  [icon-button {:keypress {:key-code 13} :on-click [:my-event]}]
  ([button-props]
   [element (random/generate-keyword) button-props])

  ([button-id {:keys [keypress] :as button-props}]
   (let [button-props (element.helpers/apply-preset icon-button.presets/BUTTON-PROPS-PRESETS button-props)
         button-props (icon-button.prototypes/button-props-prototype button-props)]
        [icon-button button-id button-props])))
