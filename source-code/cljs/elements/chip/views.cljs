
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.chip.views
    (:require [elements.chip.helpers    :as chip.helpers]
              [elements.chip.prototypes :as chip.prototypes]
              [mid-fruits.candy         :refer [param]]
              [mid-fruits.random        :as random]
              [x.app-components.api     :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;  {:icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)}
  [_ {:keys [icon icon-family]}]
  (if icon [:i.e-chip--icon {:data-icon-family icon-family} icon]))

(defn- chip-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;  {:label (metamorphic-content)}
  [_ {:keys [label]}]
  [:div.e-chip--label (x.components/content label)])

(defn- chip-primary-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;  {:primary-button-event (metamorphic-event)(opt)
  ;   :primary-button-icon (keyword)}
  [chip-id {:keys [primary-button-event primary-button-icon] :as chip-props}]
  (if primary-button-event [:button.e-chip--primary-button (chip.helpers/primary-button-attributes chip-id chip-props)
                                                           [:i.e-chip--primary-button-icon {:data-icon-family :material-icons-filled}
                                                                                           (param primary-button-icon)]]
                           [:div.e-chip--primary-button--placeholder]))

(defn- chip-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  [chip-id chip-props]
  [:div.e-chip--body (chip.helpers/chip-body-attributes chip-id chip-props)
                     [chip-icon                         chip-id chip-props]
                     [chip-label                        chip-id chip-props]
                     [chip-primary-button               chip-id chip-props]])

(defn- chip
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  [chip-id chip-props]
  [:div.e-chip (chip.helpers/chip-attributes chip-id chip-props)
               [chip-body                    chip-id chip-props]])

(defn element
  ; @param (keyword)(opt) chip-id
  ; @param (map) chip-props
  ;  {:background-color (keyword or string)(opt)
  ;    :highlight, :muted, :primary, :secondary, :success, :warning
  ;    Default: :primary
  ;   :color (keyword or string)(opt)
  ;    :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;    Default: :default
  ;   :class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :icon (keyword)(opt)
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
  ;   :label (metamorphic-content)
  ;   :on-click (metamorphic-event)(opt)
  ;    TODO ... A chip elem egésze kattintható
  ;   :primary-button-icon (keyword)(opt)
  ;    Default: :close
  ;   :primary-button-event (metamorphic-event)(opt)
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [chip {...}]
  ;
  ; @usage
  ;  [chip :my-chip {...}]
  ([chip-props]
   [element (random/generate-keyword) chip-props])

  ([chip-id chip-props]
   (let [chip-props (chip.prototypes/chip-props-prototype chip-props)]
        [chip chip-id chip-props])))