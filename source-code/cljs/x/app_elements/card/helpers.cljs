
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.card.helpers
    (:require [x.app-core.api                 :as a]
              [x.app-elements.element.helpers :as element.helpers]
              [x.app-environment.api          :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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

(defn card-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [card-id {:keys [disabled? on-click]}]
  (if disabled? {:disabled       true}
                {:data-clickable true
                 :on-click      #(a/dispatch on-click)
                 :on-mouse-up   #(environment/blur-element!)}))
