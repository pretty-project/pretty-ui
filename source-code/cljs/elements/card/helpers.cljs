
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

(defn toggle-card-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [card-id {:keys [disabled? on-click style]}]
  (if disabled? {:disabled       true
                 :style          style}
                {:data-clickable true
                 :on-click      #(r/dispatch on-click)
                 :on-mouse-up   #(element.side-effects/blur-element! card-id)
                 :style          style}))

(defn static-card-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [style]}]
  {:style style})

(defn card-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [card-id {:keys [background-color border-color border-radius horizontal-align
                   hover-color min-width stretch-orientation] :as card-props}]
  (merge (element.helpers/element-default-attributes card-id card-props)
         (element.helpers/element-indent-attributes  card-id card-props)
         (element.helpers/apply-color {} :background-color :data-background-color background-color)
         (element.helpers/apply-color {} :border-color     :data-border-color     border-color)
         (element.helpers/apply-color {} :hover-color      :data-hover-color      hover-color)
         {:data-border-radius       border-radius
          :data-horizontal-align    horizontal-align
          :data-min-width           min-width
          :data-stretch-orientation stretch-orientation}))
