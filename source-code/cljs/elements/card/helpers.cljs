
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.card.helpers
    (:require [elements.element.helpers      :as element.helpers]
              [elements.element.side-effects :as element.side-effects]
              [re-frame.api                  :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {:background-color (keyword or string)(opt)
  ;   :border-color (keyword or string)(opt)
  ;   :hover-color (keyword)(opt)
  ;   :style (map)(opt)}
  ;
  ; @return (map)
  ;  {:style (map)}
  [_ {:keys [background-color border-color hover-color style]}]
  (-> {:style style}
      (element.helpers/apply-color :background-color :data-background-color background-color)
      (element.helpers/apply-color :border-color     :data-border-color         border-color)
      (element.helpers/apply-color :hover-color      :data-hover-color           hover-color)))

(defn card-layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {:border-radius (keyword)(opt)
  ;   :horizontal-align (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:data-border-radius (keyword)
  ;   :data-horizontal-align (keyword)
  ;   :data-min-width (keyword)
  ;   :data-stretch-orientation (keyword)}
  [_ {:keys [border-radius horizontal-align min-width stretch-orientation]}]
  {:data-border-radius       border-radius
   :data-horizontal-align    horizontal-align
   :data-min-width           min-width
   :data-stretch-orientation stretch-orientation})

(defn toggle-card-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {:disabled? (boolean)(opt)
  ;   :on-click (metamorphic-event)}
  ;
  ; @return (map)
  ;  {:data-clickable (boolean)
  ;   :disabled (boolean)
  ;   :on-click (function)
  ;   :on-mouse-up (function)}
  [card-id {:keys [disabled? on-click] :as card-props}]
  (merge (card-style-attributes  card-id card-props)
         (card-layout-attributes card-id card-props)
         (if disabled? {:disabled       true}
                       {:data-clickable true
                        :on-click       #(r/dispatch on-click)
                        :on-mouse-up    #(element.side-effects/blur-element! card-id)})))

(defn static-card-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (map)
  [card-id card-props]
  (merge {}
         (card-style-attributes  card-id card-props)
         (card-layout-attributes card-id card-props)))

(defn card-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (map)
  [card-id card-props]
  (merge (element.helpers/element-default-attributes card-id card-props)
         (element.helpers/element-indent-attributes  card-id card-props)))
