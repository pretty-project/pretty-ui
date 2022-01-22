
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.16
; Description:
; Version: v0.4.8
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.layout-b
    (:require [mid-fruits.candy     :refer [param return]]
              [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-elements.api   :as elements]
              [x.app-layouts.engine :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) layout-props
  ;
  ; @return (map)
  ;  {:horizontal-align (keyword)}
  [layout-props]
  (merge {:horizontal-align :center}
         (param layout-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- card-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ; @param (map) card-props
  ;  {:icon (keyword)(opt)
  ;   :label (metamorphic-content)}
  ;
  ; @return (component)
  [_ _ {:keys [icon label]}]
  [elements/label {:content label :icon icon :min-height :xxl}])

(defn- card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ; @param (map) card-props
  ;  {:badge-color (keyword)(opt)
  ;   :min-width (keyword)(opt)
  ;   :on-click (metamorphic-event)(opt)}
  ;
  ; @return (component)
  [layout-id layout-props {:keys [badge-color min-width on-click] :as card-props}]
  [elements/card {:content [card-label layout-id layout-props card-props]
                  :horizontal-align :left :on-click on-click :badge-color badge-color
                  :min-width (or min-width :m)}])

(defn- card-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;  {:cards (maps in vector)}
  ;
  ; @return (component)
  [layout-id {:keys [cards] :as layout-props}]
  (reduce (fn [card-list card-props]
              (conj card-list [card layout-id layout-props card-props]))
          [:div.x-layout-b--card-group]
          (param cards)))

(defn- layout-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;
  ; @return (component)
  [layout-id layout-props]
  [:div.x-body-b (engine/layout-body-attributes layout-id layout-props)
                 [card-list layout-id layout-props]])

(defn- layout-b
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;
  ; @return (component)
  [layout-id layout-props]
  [layout-body layout-id layout-props])

(defn layout
  ; @param (keyword)(opt) layout-id
  ; @param (map) layout-props
  ;  {:cards (maps in vector)
  ;    [{:badge-color (keyword)(opt)
  ;       :primary, :secondary, :warning, :success
  ;      :icon (keyword)(opt)
  ;      :icon-family (keyword)(opt)
  ;       Default: :material-icons-filled
  ;       Only w/ {:icon ...}
  ;      :label (metamorphic-content)
  ;      :min-width (keyword)(opt)
  ;       :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;       Default: :m
  ;      :on-click (metamorphic-event)(opt)}
  ;     {...}]
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center}
  ;
  ; @usage
  ;  [layouts/layout-b {...}]
  ;
  ; @usage
  ;  [layouts/layout-b :my-layout {...}]
  ;
  ; @return (component)
  ([layout-props]
   [layout (a/id) layout-props])

  ([layout-id layout-props]
   (let [layout-props (layout-props-prototype layout-props)]
        [layout-b layout-id layout-props])))
