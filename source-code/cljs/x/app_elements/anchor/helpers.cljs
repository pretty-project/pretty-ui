
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.anchor.helpers
    (:require [re-frame.api                   :as r]
              [x.app-elements.element.helpers :as element.helpers]
              [x.app-environment.api          :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn anchor-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) anchor-id
  ; @param (map) anchor-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [disabled? href on-click style]}]
  (if disabled? {:disabled       true
                 :style          style}
                {:data-clickable true
                 :href           href
                 :on-click       #(r/dispatch on-click)
                 :on-mouse-up    #(environment/blur-element!)
                 :style          style}))

(defn anchor-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) anchor-id
  ; @param (map) anchor-props
  ;  {:color (keyword or string)
  ;   :font-size (keyword)}
  ;
  ; @return (map)
  ;  {:data-font-size (keyword)}
  [anchor-id {:keys [color font-size] :as anchor-props}]
  (merge (element.helpers/element-default-attributes anchor-id anchor-props)
         (element.helpers/element-indent-attributes  anchor-id anchor-props)
         (element.helpers/apply-color {} :color :data-color color)
         {:data-font-size font-size}))
