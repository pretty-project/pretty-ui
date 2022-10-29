
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.element-components.icon-button
    (:require [elements.engine.api                 :as engine]
              [elements.element.helpers            :as element.helpers]
              [elements.preset-handler.icon-button :as preset-handler.icon-button]
              [mid-fruits.candy                    :refer [param return]]
              [mid-fruits.random                   :as random]
              [re-frame.api                        :as r]
              [x.app-components.api                :as x.components]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ;  {:background-color (keyword or string)(opt)
  ;   :hover-color (keyword or string)(opt)}
  ;
  ; @return (map)
  ;  {:background-color (keyword or string)
  ;   :border-radius (keyword)
  ;   :color (keyword or string)
  ;   :icon-family (keyword)
  ;   :height (keyword)}
  [{:keys [background-color hover-color] :as button-props}]
  (merge {:color            :default
          :icon-family      :material-icons-filled
          :height           :xxl}
         (if background-color {:border-radius :s})
         (if hover-color      {:border-radius :s})
         (param button-props)))



;; -- Components --------------------------------------------------------------
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
  [button-id {:keys [label] :as button-props}]
  [:button.e-icon-button--body (merge
                                (engine/clickable-body-attributes button-id button-props {:on-mouse-over #(r/dispatch (:on-mouse-over button-props))})
                                {:style (:style button-props)
                                 :data-labeled (some? label)})
                               [icon-button-icon                 button-id button-props]
                               [icon-button-label                button-id button-props]
                               [engine/element-badge             button-id button-props]])

(defn- icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id {:keys [tooltip] :as button-props}]
  [:div.e-icon-button (engine/element-attributes button-id button-props (if tooltip {:data-tooltip (x.components/content {:content tooltip})}))
                      [icon-button-body          button-id button-props]])

(defn- stated-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [engine/stated-element button-id
                         {:render-f      #'icon-button
                          :element-props button-props
                          :destructor    [:elements/destruct-clickable! button-id]
                          :initializer   [:elements/init-clickable!     button-id]}])

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
  ;   :height (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :xxl
  ;   :label (metamorphic-content)(opt)
  ;   :on-click (metamorphic handler)(opt)
  ;   :on-mouse-over (metamorphic handler)(opt)
  ;   :preset (keyword)(opt)
  ;   :stop-propagation? (boolean)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :variant (keyword)(opt)
  ;    :placeholder}
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
   (let [button-props (element.helpers/apply-preset preset-handler.icon-button/BUTTON-PROPS-PRESETS button-props)
         button-props (button-props-prototype button-props)]
        (if keypress [stated-element button-id button-props]
                     [icon-button    button-id button-props]))))
